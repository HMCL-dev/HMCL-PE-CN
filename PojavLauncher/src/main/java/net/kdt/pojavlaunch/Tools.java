package net.kdt.pojavlaunch;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import net.kdt.pojavlaunch.utils.*;

import org.apache.commons.codec.binary.Hex;
import org.lwjgl.glfw.CallbackBridge;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public final class Tools {

    public static String CACIO_DIR = "/data/data/com.tungsten.hmclpe/app_runtime/caciocavallo/";

    public static void launchMinecraft(final Activity activity,String javaPath,String home,String renderer, Vector<String> args) throws Throwable {

        String[] launchArgs = new String[args.size()];
        for (int i = 0; i < args.size(); i++) {
            if (!args.get(i).equals(" ")) {
                launchArgs[i] = args.get(i);
                System.out.println("Minecraft Args:" + launchArgs[i]);
                Logger.getInstance().appendToLog("Minecraft Args:" + launchArgs[i]);
            }
        }

        List<String> javaArgList = new ArrayList<String>();

        javaArgList.addAll(Arrays.asList(launchArgs));
        JREUtils.launchJavaVM(activity,javaPath,home,renderer, javaArgList);
    }
    
    public static void getCacioJavaArgs(List<String> javaArgList, boolean isHeadless) {
        javaArgList.add("-Djava.awt.headless="+isHeadless);
        // Caciocavallo config AWT-enabled version
        javaArgList.add("-Dcacio.managed.screensize=" + CallbackBridge.physicalWidth + "x" + CallbackBridge.physicalHeight);
        // javaArgList.add("-Dcacio.font.fontmanager=net.java.openjdk.cacio.ctc.CTCFontManager");
        javaArgList.add("-Dcacio.font.fontmanager=sun.awt.X11FontManager");
        javaArgList.add("-Dcacio.font.fontscaler=sun.font.FreetypeFontScaler");
        javaArgList.add("-Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel");
        javaArgList.add("-Dawt.toolkit=net.java.openjdk.cacio.ctc.CTCToolkit");
        javaArgList.add("-Djava.awt.graphicsenv=net.java.openjdk.cacio.ctc.CTCGraphicsEnvironment");

        StringBuilder cacioClasspath = new StringBuilder();
        cacioClasspath.append("-Xbootclasspath/p");
        File cacioDir = new File(CACIO_DIR);
        if (cacioDir.exists() && cacioDir.isDirectory()) {
            for (File file : cacioDir.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    cacioClasspath.append(":" + file.getAbsolutePath());
                }
            }
        }
        javaArgList.add(cacioClasspath.toString());
    }

    public static void write(String path, byte[] content) throws IOException
    {
        File outPath = new File(path);
        outPath.getParentFile().mkdirs();
        outPath.createNewFile();

        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(path));
        fos.write(content, 0, content.length);
        fos.close();
    }

    public static void write(String path, String content) throws IOException {
        write(path, content.getBytes());
    }

}
