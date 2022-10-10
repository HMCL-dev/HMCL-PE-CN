package net.matrix.mobile_hiper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.system.OsConstants;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import mobileHiPer.CIDR;

public class HiPerVpnService extends VpnService {

    private static String TAG = "HiPerVpnService";

    private static final int FOREGROUND_ID = 1008;
    private static final String CHANNEL_ID = "HIPER_CHANNEL";

    private static boolean running = false;
    private static Sites.Site site = null;
    private static mobileHiPer.HiPer hiper = null;
    private static ParcelFileDescriptor vpnInterface = null;
    private NetworkCallback networkCallback = new NetworkCallback();
    private boolean didSleep = false;

    private static HiPerCallback callback;

    public static boolean isRunning() {
        return running;
    }

    public static Sites.Site getSite() {
        return site;
    }

    public static void setHiPerCallback(HiPerCallback callback) {
        HiPerVpnService.callback = callback;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Objects.equals(intent.getStringExtra("COMMAND"), "STOP")) {
            stopVpn();
            return Service.START_NOT_STICKY;
        }

        if (running) {
            //TODO: can we signal failure?
            return super.onStartCommand(intent, flags, startId);
        }

        //TODO: if we fail to start, android will attempt a restart lacking all the intent data we need.
        // Link active site config in Main to avoid this
        site = Sites.Site.fromFile(getApplicationContext());

        if (site == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        if (site.getCert() == null) {
            announceExit("Site is missing a certificate");
            //TODO: can we signal failure?
            return super.onStartCommand(intent, flags, startId);
        }

        startForeground(this, "HiPer", "HiPer service is running!", "HiPer", "HiPer service is running!");
        startVpn();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopVpn();
        super.onDestroy();
    }

    public static void startForeground(Service service, String channelName, String channelDesc, String contentTitle, String contentText) {
        NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        notification = getNotification(service, manager, channelName, channelDesc, contentTitle, contentText);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        service.startForeground(FOREGROUND_ID, notification);
    }

    private static Notification getNotification(Context context, NotificationManager manager, String name, String desc, String contentTitle, String contentText) {
        Notification.Builder builder;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(desc);

        manager.createNotificationChannel(channel);
        builder = new Notification.Builder(context, CHANNEL_ID);
        builder.setCategory(Notification.CATEGORY_RECOMMENDATION)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_craft_table);

        return builder.build();
    }

    private void startVpn() {
        CIDR ipNet;

        try {
            ipNet = mobileHiPer.MobileHiPer.parseCIDR(site.getCert().getCert().getDetails().getIps().get(0));
        } catch (Exception e) {
            announceExit(e.toString());
            return;
        }

        Builder builder = new Builder()
                .addAddress(ipNet.getIp(), (int) ipNet.getMaskSize())
                .addRoute(ipNet.getNetwork(), (int) ipNet.getMaskSize())
                .setMtu(site.getMtu())
                .setSession(TAG)
                .allowFamily(OsConstants.AF_INET)
                .allowFamily(OsConstants.AF_INET6);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder.setMetered(false);
        }

        // Add our unsafe routes
        for (Sites.UnsafeRoute unsafeRoute : site.getUnsafeRoutes()) {
            try {
                CIDR cidr = mobileHiPer.MobileHiPer.parseCIDR(unsafeRoute.getRoute());
                builder.addRoute(cidr.getNetwork(), (int) cidr.getMaskSize());
            } catch (Exception e) {
                announceExit(e.toString());
                e.printStackTrace();
            }
        }

        try {
            vpnInterface = builder.establish();
            hiper = mobileHiPer.MobileHiPer.newHiPer(site.getConfig(), site.getKey(this), site.getLogFile(), vpnInterface.getFd());
        } catch (Exception e) {
            Log.e(TAG, "Got an error " + e);
            try {
                vpnInterface.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            announceExit(e.toString());
            stopSelf();
            return;
        }

        registerNetworkCallback();
        //TODO: There is an open discussion around sleep killing tunnels or just changing mobile to tear down stale tunnels
        //registerSleep()

        hiper.start();
        running = true;
        sendSimple(1);
    }

    private void stopVpn() {
        unregisterNetworkCallback();
        hiper.stop();
        try {
            vpnInterface.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
        announceExit(null);
    }

    // Used to detect network changes (wifi -> cell or vice versa) and rebinds the udp socket/updates LH
    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

        connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
    }

    private void unregisterNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    public class NetworkCallback extends ConnectivityManager.NetworkCallback {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            hiper.rebind("network change");
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            hiper.rebind("network change");
        }
    }

    private void registerSleep() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                if (pm.isDeviceIdleMode()) {
                    if (!didSleep) {
                        hiper.sleep();
                        //TODO: we may want to shut off our network change listener like we do with iOS, I haven't observed any issues with it yet though
                    }
                    didSleep = true;
                } else {
                    hiper.rebind("android wake");
                    didSleep = false;
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED));
    }

    private void sendSimple(int code) {
        callback.run(code);
    }

    private void announceExit(String err) {
        callback.onExit(err);
    }

}
