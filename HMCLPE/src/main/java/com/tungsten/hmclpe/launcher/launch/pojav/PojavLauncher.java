package com.tungsten.hmclpe.launcher.launch.pojav;

import static com.tungsten.hmclpe.launcher.launch.GameLaunchSetting.isHighVersion;

import android.content.Context;
import android.os.Build;

import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;
import com.tungsten.hmclpe.launcher.launch.LaunchVersion;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.manifest.info.AppInfo;
import com.tungsten.hmclpe.utils.string.StringUtils;

import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.utils.JREUtils;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

public class PojavLauncher {

    public static Vector<String> getMcArgs(GameLaunchSetting gameLaunchSetting, Context context,int width,int height,String server){
        try {
            JREUtils.jreReleaseList = JREUtils.readJREReleaseProperties(gameLaunchSetting.javaPath);
            LaunchVersion version = LaunchVersion.fromDirectory(new File(gameLaunchSetting.currentVersion));
            String javaPath = gameLaunchSetting.javaPath;
            JREUtils.relocateLibPath(context,javaPath);
            String libraryPath = javaPath + "/lib/aarch64/jli:" + javaPath + "/lib/aarch64:" + AppManifest.POJAV_LIB_DIR + "/lwjgl3:" + JREUtils.LD_LIBRARY_PATH + ":" + AppManifest.POJAV_LIB_DIR + "/lwjgl3";;
            boolean isJava17 = javaPath.endsWith("JRE17");
            String classPath = getLWJGL3ClassPath() + ":" + version.getClassPath(gameLaunchSetting.gameFileDirectory,isJava17);
            Vector<String> args = new Vector<String>();
            if (JREUtils.jreReleaseList.get("JAVA_VERSION").equals("1.8.0")) {
                Tools.getCacioJavaArgs(context,args, false);
            }
            args.add("-Djava.home=" + javaPath);
            args.add("-Djava.io.tmpdir=" + AppManifest.DEFAULT_CACHE_DIR);
            args.add("-Duser.home=" + new File(gameLaunchSetting.gameFileDirectory).getParent());
            args.add("-Duser.language=" + System.getProperty("user.language"));
            args.add("-Dos.name=Linux");
            args.add("-Dos.version=Android-" + Build.VERSION.RELEASE);
            args.add("-Dpojav.path.minecraft=" + gameLaunchSetting.gameFileDirectory);
            args.addAll(JREUtils.getJavaArgs(context));
            args.add("-Dnet.minecraft.clientmodname=" + AppInfo.APP_NAME);
            //args.add("-Dfml.earlyprogresswindow=false");
            String[] JVMArgs;
            JVMArgs = version.getJVMArguments(gameLaunchSetting);
            for (int i = 0;i < JVMArgs.length;i++) {
                if (JVMArgs[i].startsWith("-DignoreList") && !JVMArgs[i].endsWith("," + new File(gameLaunchSetting.currentVersion).getName() + ".jar")) {
                    JVMArgs[i] = JVMArgs[i] + "," + new File(gameLaunchSetting.currentVersion).getName() + ".jar";
                }
            }
            Collections.addAll(args, JVMArgs);
            args.add("-Xms" + gameLaunchSetting.minRam + "M");
            args.add("-Xmx" + gameLaunchSetting.maxRam + "M");
            args.add("-Dorg.lwjgl.opengl.libname=" + JREUtils.loadGraphicsLibrary(gameLaunchSetting.pojavRenderer));
            args.add("-cp");
            args.add(classPath);
            args.add(version.mainClass);
            String[] minecraftArgs;
            minecraftArgs = version.getMinecraftArguments(gameLaunchSetting, isHighVersion(gameLaunchSetting));
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

}
