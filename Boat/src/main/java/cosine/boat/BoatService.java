package cosine.boat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.app.Notification;
import android.os.Build;
import android.os.IBinder;
import android.app.PendingIntent;

import androidx.annotation.RequiresApi;

public class BoatService extends Service
{
    public static final String STOP_BOAT_SERVICE = "stop_boat_service";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("virglChannel", "virglChannel", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_craft_table);
        builder.setContentTitle("HMCL-PE");
        builder.setContentText("VirGL is running");
        builder.setOngoing(true);
        Intent exitIntent = new Intent(this, this.getClass()).setAction(STOP_BOAT_SERVICE);
        builder.setContentIntent(PendingIntent.getService(this, 0, exitIntent, 0));
        Notification notification = builder.build();
        notificationManager.notify(8888, notification);
    }

    public void dismissForegroundNotification() {
        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissForegroundNotification();
        System.exit(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> LoadMe.startVirGLService(this,getExternalFilesDir("debug").getAbsolutePath(),getCacheDir().getAbsolutePath())).start();
        showForegroundNotification();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service doesn't support to be bound.
        return null;
    }
}