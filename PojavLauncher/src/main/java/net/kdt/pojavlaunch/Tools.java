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

    public static String POJAV_LIB_DIR = "/data/data/com.tungsten.hmclpe/app_runtime/pojav/lib";

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
        File cacioDir = new File(POJAV_LIB_DIR + "/caciocavallo");
        if (cacioDir.exists() && cacioDir.isDirectory()) {
            for (File file : cacioDir.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    cacioClasspath.append(":" + file.getAbsolutePath());
                }
            }
        }
        javaArgList.add(cacioClasspath.toString());
    }

    public static String fromStringArray(String[] strArr) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            if (i > 0) builder.append(" ");
            builder.append(strArr[i]);
        }

        return builder.toString();
    }

    public static void setFullscreen(Activity act) {
        final View decorView = act.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                }
            });
    }

    public static DisplayMetrics currentDisplayMetrics;

    public static void copyAssetFile(Context ctx, String fileName, String output, boolean overwrite) throws IOException {
        copyAssetFile(ctx, fileName, output, new File(fileName).getName(), overwrite);
    }

    public static void copyAssetFile(Context ctx, String fileName, String output, String outputName, boolean overwrite) throws IOException
    {
        File file = new File(output);
        if(!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(output, outputName);
        if(!file2.exists() || overwrite){
            write(file2.getAbsolutePath(), loadFromAssetToByte(ctx, fileName));
        }
    }

    public static void moveInside(String from, String to) {
        File fromFile = new File(from);
        for (File fromInside : fromFile.listFiles()) {
            moveRecursive(fromInside.getAbsolutePath(), to);
        }
        fromFile.delete();
    }

    public static void moveRecursive(String from, String to) {
        moveRecursive(new File(from), new File(to));
    }

    public static void moveRecursive(File from, File to) {
        File toFrom = new File(to, from.getName());
        try {
            if (from.isDirectory()) {
                for (File child : from.listFiles()) {
                    moveRecursive(child, toFrom);
                }
            }
        } finally {
            from.getParentFile().mkdirs();
            from.renameTo(toFrom);
        }
    }

    public static File lastFileModified(String dir) {
        File fl = new File(dir);

        File[] files = fl.listFiles(new FileFilter() {          
                public boolean accept(File file) {
                    return file.isFile();
                }
            });

        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }

        return choice;
    }


    public static String read(InputStream is) throws IOException {
        String out = "";
        int len;
        byte[] buf = new byte[512];
        while((len = is.read(buf))!=-1) {
            out += new String(buf,0,len);
        }
        return out;
    }

    public static String read(String path) throws IOException {
        return read(new FileInputStream(path));
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

    public static byte[] loadFromAssetToByte(Context ctx, String inFile) {
        byte[] buffer = null;

        try {
            InputStream stream = ctx.getAssets().open(inFile);

            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
        } catch (IOException e) {
            // Handle exceptions here
            e.printStackTrace();
        }
        return buffer;
    }

    public abstract static class DownloaderFeedback {
        public abstract void updateProgress(int curr, int max);
    }

    public static boolean compareSHA1(File f, String sourceSHA) {
        try {
            String sha1_dst;
            try (InputStream is = new FileInputStream(f)) {
                 sha1_dst = new String(Hex.encodeHex(org.apache.commons.codec.digest.DigestUtils.sha1(is)));
            }
            if(sha1_dst != null && sourceSHA != null) {
                return sha1_dst.equalsIgnoreCase(sourceSHA);
            } else{
                return true; // fake match
            }
        }catch (IOException e) {
            Log.i("SHA1","Fake-matching a hash due to a read error",e);
            return true;
        }
    }

    public static int getTotalDeviceMemory(Context ctx){
        ActivityManager actManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return (int) (memInfo.totalMem / 1048576L);
    }

    public static int getFreeDeviceMemory(Context ctx){
        ActivityManager actManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return (int) (memInfo.availMem / 1048576L);
    }

    public static int getDisplayFriendlyRes(int displaySideRes, float scaling){
        displaySideRes *= scaling;
        if(displaySideRes % 2 != 0) displaySideRes ++;
        return displaySideRes;
    }

}
