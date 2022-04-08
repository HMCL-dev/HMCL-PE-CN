package com.tungsten.hmclpe.launcher.download.forge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tungsten.hmclpe.launcher.manifest.AppManifest;

public class InstallForgeService extends Service {

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
        InstallForgeTask installForgeTask = new InstallForgeTask(name,this);
        installForgeTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
