package com.tungsten.hmclpe.launcher.download.forge;

import android.os.AsyncTask;

import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.InitializeSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.utils.file.FileStringUtils;

import java.util.ArrayList;

import cosine.boat.LoadMe;

public class InstallForgeTask extends AsyncTask<ForgeVersion,Integer,Boolean> {

    private String name;
    private InstallForgeService service;
    private LauncherSetting launcherSetting;

    public InstallForgeTask(String name, InstallForgeService service) {
        this.name = name;
        this.service = service;
        this.launcherSetting = InitializeSetting.initializeLauncherSetting();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(ForgeVersion... forgeVersions) {
        boolean success = true;
        String javaPath = AppManifest.JAVA_DIR + "/default";
        ArrayList<String> args = new ArrayList<>();
        args.add(javaPath + "/bin/java");
        args.add("-Djava.io.tmpdir=" + AppManifest.DEFAULT_CACHE_DIR);
        args.add("-Dos.name=Linux");
        args.add("-Djava.library.path=" + javaPath + "/lib/aarch64/jli:" + javaPath + "/lib/aarch64");
        args.add("-classpath");
        args.add(".:" + AppManifest.PLUGIN_DIR + "/installer/forge-install-bootstrapper.jar:" + AppManifest.INSTALL_DIR + "/forge/forge-installer.jar");
        args.add("com.bangbang93.ForgeInstaller");
        args.add(launcherSetting.gameFileDirectory);
        args.add("-Xms1024M");
        args.add("-Xmx1024M");
        int exitCode = LoadMe.launchJVM(javaPath,args,AppManifest.DEBUG_DIR);
        //int exitCode = JREUtils.launchAPIInstaller(activity,javaPath,args,AppManifest.DEBUG_DIR);
        if (exitCode != 0){
            success = false;
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            FileStringUtils.writeFile(AppManifest.INSTALL_DIR + "/forge/temp.json","success");
        }
        else {
            FileStringUtils.writeFile(AppManifest.INSTALL_DIR + "/forge/temp.json","failed");
        }
        System.out.println("-------------------------------------------------------------------------------------------------service destroyed!");
        service.stopSelf();
        super.onPostExecute(aBoolean);
    }

}
