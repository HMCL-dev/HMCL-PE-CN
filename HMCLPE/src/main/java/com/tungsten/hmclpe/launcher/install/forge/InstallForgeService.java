package com.tungsten.hmclpe.launcher.install.forge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tungsten.hmclpe.launcher.download.minecraft.forge.ForgeVersion;
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
        ForgeVersion optifineVersion = (ForgeVersion) intent.getExtras().getSerializable("version");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
