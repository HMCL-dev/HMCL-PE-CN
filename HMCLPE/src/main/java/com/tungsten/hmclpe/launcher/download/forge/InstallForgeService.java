package com.tungsten.hmclpe.launcher.download.forge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.utils.SocketServer;

public class InstallForgeService extends Service {
    SocketServer server;
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
        String name = intent.getExtras().getString("name");
        //ForgeVersion forgeVersion = (ForgeVersion) intent.getExtras().getSerializable("version");
        server=new SocketServer(new SocketServer.Listerner() {
            @Override
            public void onReceive(String msg) {
                Log.e("SocketServer",msg);
            }
        });
        server.start();
        InstallForgeTask installForgeTask = new InstallForgeTask(name,this);
        installForgeTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        server.stop();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
