package com.tungsten.hmclpe.launcher.launch.pojav;

import android.content.Context;

import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;
import com.tungsten.hmclpe.launcher.launch.LaunchVersion;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;

import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.utils.JREUtils;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

public class PojavLauncher {

    public static Vector<String> getMcArgs(GameLaunchSetting gameLaunchSetting, Context context){
        try {
            JREUtils.jreReleaseList = JREUtils.readJREReleaseProperties(gameLaunchSetting.javaPath);
            LaunchVersion version = LaunchVersion.fromDirectory(new File(gameLaunchSetting.currentVersion));
            boolean highVersion = false;
            if (version.minimumLauncherVersion > 21){
                highVersion = true;
            }
            String javaPath = gameLaunchSetting.javaPath;
            JREUtils.relocateLibPath(context,javaPath);
            String libraryPath = javaPath + "/lib/aarch64/jli:" + javaPath + "/lib/aarch64:" + AppManifest.POJAV_LIB_DIR + "/lwjgl3:" + JREUtils.LD_LIBRARY_PATH + ":" + AppManifest.POJAV_LIB_DIR + "/lwjgl3";;
            String classPath = getLWJGL3ClassPath() + ":" + version.getClassPath(gameLaunchSetting.gameFileDirectory);
            Vector<String> args = new Vector<String>();
            String[] extraJavaFlags = gameLaunchSetting.extraJavaFlags.split(" ");
            Collections.addAll(args, extraJavaFlags);
            args.addAll(JREUtils.getJavaArgs());
            if (JREUtils.jreReleaseList.get("JAVA_VERSION").equals("1.8.0")) {
                Tools.getCacioJavaArgs(args, false);
            }
            args.add("-Dorg.lwjgl.opengl.libname=" + JREUtils.loadGraphicsLibrary(gameLaunchSetting.pojavRenderer));
            args.add("-Djava.home=" + javaPath);
            args.add("-Dfml.earlyprogresswindow=false");
            args.add("-Djava.io.tmpdir=" + AppManifest.DEFAULT_CACHE_DIR);
            args.add("-cp");
            args.add(classPath);

            args.add(version.mainClass);
            String[] minecraftArgs;
            minecraftArgs = version.getMinecraftArguments(gameLaunchSetting, highVersion);
            Collections.addAll(args, minecraftArgs);
            args.add("--width");
            args.add(Integer.toString(gameLaunchSetting.width));
            args.add("--height");
            args.add(Integer.toString(gameLaunchSetting.height));
            String[] extraMinecraftArgs = gameLaunchSetting.extraMinecraftFlags.split(" ");
            Collections.addAll(args, extraMinecraftArgs);
            return args;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String getLWJGL3ClassPath() {
        StringBuilder libStr = new StringBuilder();
        File lwjgl3Folder = new File(AppManifest.POJAV_LIB_DIR, "lwjgl3");
        if (/* info.arguments != null && */ lwjgl3Folder.exists()) {
            for (File file: lwjgl3Folder.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    libStr.append(file.getAbsolutePath() + ":");
                }
            }
        }
        // Remove the ':' at the end
        libStr.setLength(libStr.length() - 1);
        return libStr.toString();
    }

    public static boolean isHighVersion(GameLaunchSetting gameLaunchSetting){
        LaunchVersion version = LaunchVersion.fromDirectory(new File(gameLaunchSetting.currentVersion));
        boolean highVersion = false;
        if (version.minimumLauncherVersion > 21){
            highVersion = true;
        }
        return highVersion;
    }

}
