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
                classPath = AppManifest.BOAT_LIB_DIR + "/lwjgl-2/lwjgl.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-2/lwjgl_util.jar:" + version.getClassPath(gameLaunchSetting.gameFileDirectory);
            }
            else {
                boolean isJava17 = javaPath.endsWith("JRE17");
                if (isJava17) {
                    libraryPath = javaPath + "/lib:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3:" + AppManifest.BOAT_LIB_DIR + "/renderer";
                }
                else {
                    libraryPath = javaPath + "/lib/jli:" + javaPath + "/lib:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3:" + AppManifest.BOAT_LIB_DIR + "/renderer";
                }
                classPath = AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-jemalloc.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-tinyfd.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-opengl.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-openal.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-glfw.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl-stb.jar:" + AppManifest.BOAT_LIB_DIR + "/lwjgl-3/lwjgl.jar:" +  version.getClassPath(gameLaunchSetting.gameFileDirectory);
            }
            Vector<String> args = new Vector<String>();
            args.add(javaPath + "/bin/java");
            args.add("-cp");
            args.add(classPath);
            args.add("-Djava.library.path=" + libraryPath+":/storage/emulated/0/HMCLPE/.minecraft/versions/1.17.1-forge/1.17.1-forge.jar");
            args.add("-Dfml.earlyprogresswindow=false");
            args.add("-Dorg.lwjgl.util.DebugLoader=true");
            args.add("-Dorg.lwjgl.util.Debug=true");
            args.add("-Dorg.lwjgl.opengl.maxVersion=3.2");
            args.add("-DignoreList=bootstraplauncher,securejarhandler,asm-commons,asm-util,asm-analysis,asm-tree,asm,client-extra,fmlcore,javafmllanguage,mclanguage,forge-,1.17.1.jar,1.17.1-forge.jar");
            args.add("-DmergeModules=jna-5.8.0.jar,jna-platform-58.0.jar,java-objc-bridge-1.0.0.jar");
            args.add("-DlibraryDirectory=/storage/emulated/0/HMCLPE/.minecraft/libraries");
            args.add("-p");
            args.add("/storage/emulated/0/HMCLPE/.minecraft/libraries/cpw/mods/bootstraplauncher/0.1.17/bootstraplauncher-0.1.17.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/cpw/mods/securejarhandler/0.9.54/securejarhandler-0.9.54.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/ow2/asm/asm-commons/9.1/asm-commons-9.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/ow2/asm/asm-util/9.1/asm-util-9.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/ow2/asm/asm-analysis/9.1/asm-analysis-9.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/ow2/asm/asm-tree/9.1/asm-tree-9.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/ow2/asm/asm/9.1/asm-9.1.jar");
            args.add("--add-modules");
            args.add("ALL-MODULE-PATH");
            args.add("--add-opens");
            args.add("java.base/java.util.jar=cpw.mods.securejarhandler");
            args.add("--add-exports");
            args.add("java.base/sun.security.util=cpw.mods.securejarhandler");
            args.add("--add-exports");
            args.add("jdk.naming.dns/com.sun.jndi.dns=java.naming");
            args.add("-Dos.name=Linux");
            args.add("-Dlwjgl.platform=Boat");
            args.add("-Dorg.lwjgl.opengl.libname=" + gameLaunchSetting.boatRenderer);
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
