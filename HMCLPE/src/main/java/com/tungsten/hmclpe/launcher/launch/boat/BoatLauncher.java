package com.tungsten.hmclpe.launcher.launch.boat;

import android.content.Context;

import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;
import com.tungsten.hmclpe.launcher.launch.LaunchVersion;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.string.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

public class BoatLauncher {

    public static Vector<String> getMcArgs(GameLaunchSetting gameLaunchSetting , Context context,int width,int height,String server){
        try {
            LaunchVersion version = LaunchVersion.fromDirectory(new File(gameLaunchSetting.currentVersion));
            String javaPath = gameLaunchSetting.javaPath;
            boolean highVersion = false;
            if (version.minimumLauncherVersion >= 21){
                highVersion = true;
            }
            String libraryPath;
            String classPath;
            if (!highVersion){
                libraryPath = javaPath + "/lib/aarch64/jli:" + javaPath + "/lib/aarch64:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-2:" + AppManifest.BOAT_LIB_DIR + "/renderer";
                classPath = AppManifest.BOAT_LIB_DIR + "/lwjgl-2/lwjgl.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-2/lwjgl_util.jar:" + version.getClassPath(gameLaunchSetting.gameFileDirectory,false);
            }
            else {
                boolean isJava17 = javaPath.endsWith("JRE17");
                if (isJava17) {
                    libraryPath = javaPath + "/lib:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3:" + AppManifest.BOAT_LIB_DIR + "/renderer";
                }
                else {
                    libraryPath = javaPath + "/lib/jli:" + javaPath + "/lib:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3:" + AppManifest.BOAT_LIB_DIR + "/renderer";
                }
                classPath = AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-jemalloc.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-tinyfd.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-opengl.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-openal.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-glfw.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-stb.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl.jar:" + version.getClassPath(gameLaunchSetting.gameFileDirectory,isJava17);
            }
            Vector<String> args = new Vector<String>();
            args.add(javaPath + "/bin/java");
            args.add("-cp");
            args.add(classPath);
            args.add("-Djava.library.path=" + libraryPath);
            args.add("-Dfml.earlyprogresswindow=false");
            args.add("-Dorg.lwjgl.util.DebugLoader=true");
            args.add("-Dorg.lwjgl.util.Debug=true");
            args.add("-Dorg.lwjgl.opengl.maxVersion=3.2");
            String[] JVMArgs;
            JVMArgs = version.getJVMArguments(gameLaunchSetting);
            for (int i = 0;i < JVMArgs.length;i++) {
                if (JVMArgs[i].startsWith("-DignoreList") && !JVMArgs[i].endsWith("," + new File(gameLaunchSetting.currentVersion).getName() + ".jar")) {
                    JVMArgs[i] = JVMArgs[i] + "," + new File(gameLaunchSetting.currentVersion).getName() + ".jar";
                }
            }
            Collections.addAll(args, JVMArgs);
            args.add("-Dos.name=Linux");
            args.add("-Dlwjgl.platform=Boat");
            args.add("-Dorg.lwjgl.opengl.libname=libGL112.so.1");
            args.add("-Dlwjgl.platform=Boat");
            args.add("-Dos.name=Linux");
            args.add("-Djava.io.tmpdir=" + AppManifest.DEFAULT_CACHE_DIR);
            args.add("-Xms" + gameLaunchSetting.minRam + "M");
            args.add("-Xmx" + gameLaunchSetting.maxRam + "M");
            args.add(version.mainClass);
            String[] minecraftArgs;
            minecraftArgs = version.getMinecraftArguments(gameLaunchSetting, highVersion);
            Collections.addAll(args, minecraftArgs);
            args.add("--width");
            args.add(Integer.toString(width));
            args.add("--height");
            args.add(Integer.toString(height));
            if (StringUtils.isNotBlank(server)) {
                String[] ser = server.split(":");
                args.add("--server");
                args.add(ser[0]);
                args.add("--port");
                args.add(ser.length > 1 ? ser[1] : "25565");
            }
            String[] extraJavaFlags = gameLaunchSetting.extraJavaFlags.split(" ");
            Collections.addAll(args, extraJavaFlags);
            String[] extraMinecraftArgs = gameLaunchSetting.extraMinecraftFlags.split(" ");
            Collections.addAll(args, extraMinecraftArgs);
            return args;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
