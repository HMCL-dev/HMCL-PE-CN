package com.tungsten.hmclpe.service.install;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tungsten.hmclpe.launcher.download.minecraft.optifine.OptifineVersion;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.task.install.InstallOptifineTask;

public class InstallOptifineService extends Service {

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
        OptifineVersion optifineVersion = (OptifineVersion) intent.getExtras().getSerializable("version");
        InstallOptifineTask task = new InstallOptifineTask(getApplicationContext(),name,this);
        task.execute(optifineVersion);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

}
