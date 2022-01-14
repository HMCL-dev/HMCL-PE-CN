package com.tungsten.hmclpe.launcher.launch.boat;

import android.content.Context;

import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

public class BoatLauncher {

    public static Vector<String> getMcArgs(GameLaunchSetting gameLaunchSetting , Context context){
        try {
            BoatVersion version = BoatVersion.fromDirectory(new File(gameLaunchSetting.currentVersion));
            String javaPath = gameLaunchSetting.javaPath;
            boolean highVersion = false;
            if (version.minimumLauncherVersion > 21){
                highVersion = true;
            }
            String libraryPath;
            String classPath;
            if (!highVersion){
                libraryPath = javaPath + "/lib/aarch64/jli:" + javaPath + "/lib/aarch64:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-2:" + javaPath;
                classPath = AppManifest.BOAT_LIB_DIR + "/lwjgl-2/lwjgl.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-2/lwjgl_util.jar:" + version.getClassPath(gameLaunchSetting.gameFileDirectory);
            }
            else {
                libraryPath = javaPath + "/lib/aarch64/jli:" + javaPath + "/lib/aarch64:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3:" + javaPath;
                classPath = AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-jemalloc.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-tinyfd.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-opengl.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-openal.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-glfw.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-stb.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl.jar:" +  version.getClassPath(gameLaunchSetting.gameFileDirectory);
            }
            Vector<String> args = new Vector<String>();
            args.add(javaPath +  "/bin/java");
            args.add("-cp");
            args.add(classPath);
            args.add("-Djava.library.path=" + libraryPath);
            args.add("-Dfml.earlyprogresswindow=false");
            args.add("-Djava.io.tmpdir=/data/user/0/tungsten.lava.boat/cache");
            String[] extraJavaFlags = gameLaunchSetting.extraJavaFlags.split(" ");
            Collections.addAll(args, extraJavaFlags);
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

    public static boolean isHighVersion(GameLaunchSetting gameLaunchSetting){
        BoatVersion version = BoatVersion.fromDirectory(new File(gameLaunchSetting.currentVersion));
        boolean highVersion = false;
        if (version.minimumLauncherVersion > 21){
            highVersion = true;
        }
        return highVersion;
    }

}
