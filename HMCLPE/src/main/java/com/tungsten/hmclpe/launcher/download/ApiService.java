package com.tungsten.hmclpe.launcher.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tungsten.hmclpe.manifest.AppManifest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import cosine.boat.LoadMe;

public class ApiService extends Service {

    public static final int API_SERVICE_PORT = 6868;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppManifest.initializeManifest(getApplicationContext());
        String javaPath = AppManifest.JAVA_DIR + "/default";
        ArrayList<String> commands = intent.getExtras().getStringArrayList("commands");
        String debugDir = AppManifest.DEBUG_DIR;
        new Thread(() -> {
            int exitCode = LoadMe.launchJVM(javaPath,commands,debugDir);
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.connect(new InetSocketAddress("127.0.0.1", API_SERVICE_PORT));
                // 发送数据
                byte[] data = (exitCode + "").getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.send(packet);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            stopSelf();
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
