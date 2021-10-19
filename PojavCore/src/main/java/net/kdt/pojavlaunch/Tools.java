package net.kdt.pojavlaunch;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import com.google.gson.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.*;
import java.util.*;
import java.util.zip.*;
import net.kdt.pojavlaunch.prefs.*;
import net.kdt.pojavlaunch.utils.*;

import org.apache.commons.codec.binary.Hex;
import org.lwjgl.glfw.*;
import android.view.*;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.P;
import static net.kdt.pojavlaunch.prefs.LauncherPreferences.PREF_IGNORE_NOTCH;
import static net.kdt.pojavlaunch.prefs.LauncherPreferences.PREF_NOTCH_SIZE;

public final class Tools {

    public static String APP_NAME = "null";

    public static String DIR_DATA; //Initialized later to get context
    public static String MULTIRT_HOME;
    public static String LOCAL_RENDERER = null;
    public static int DEVICE_ARCHITECTURE;

    // New since 3.3.1
    public static String DIR_ACCOUNT_NEW;
    public static String DIR_GAME_HOME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/games/PojavLauncher";
    public static String DIR_GAME_NEW;

    // New since 3.0.0
    public static String DIR_HOME_JRE;
    public static String DIRNAME_HOME_JRE = "lib";

    // New since 2.4.2
    public static String DIR_HOME_VERSION;
    public static String DIR_HOME_LIBRARY;

    public static String DIR_HOME_CRASH;

    public static String ASSETS_PATH;
    public static String OBSOLETE_RESOURCES_PATH;
    public static String CTRLMAP_PATH;
    public static String CTRLDEF_FILE;

    /**
     * Since some constant requires the use of the Context object
     * You can call this function to initialize them.
     * Any value (in)directly dependant on DIR_DATA should be set only here.
     */
    public static void initContextConstants(Context ctx){
        DIR_DATA = ctx.getFilesDir().getParent();
        MULTIRT_HOME = DIR_DATA+"/runtimes";
        if(SDK_INT >= 29) {
            DIR_GAME_HOME = ctx.getExternalFilesDir(null).getAbsolutePath();
        }else{
            DIR_GAME_HOME = new File(Environment.getExternalStorageDirectory(),"games/PojavLauncher").getAbsolutePath();
        }
        DIR_GAME_NEW = DIR_GAME_HOME + "/.minecraft";
        DIR_HOME_VERSION = DIR_GAME_NEW + "/versions";
        DIR_HOME_LIBRARY = DIR_GAME_NEW + "/libraries";
        DIR_HOME_CRASH = DIR_GAME_NEW + "/crash-reports";
        ASSETS_PATH = DIR_GAME_NEW + "/assets";
        OBSOLETE_RESOURCES_PATH= DIR_GAME_NEW + "/resources";
        CTRLMAP_PATH = DIR_GAME_HOME + "/controlmap";
        CTRLDEF_FILE = DIR_GAME_HOME + "/controlmap/default.json";
    }


    public static void launchMinecraft(final LoggableActivity ctx,GameSetting gameSetting) throws Throwable {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(mi);
        if(LauncherPreferences.PREF_RAM_ALLOCATION > (mi.availMem/1048576L)) {
            Object memoryErrorLock = new Object();
            ctx.runOnUiThread(() -> {
                androidx.appcompat.app.AlertDialog.Builder b = new androidx.appcompat.app.AlertDialog.Builder(ctx)
                        .setMessage("")
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {synchronized(memoryErrorLock){memoryErrorLock.notifyAll();}})
                        .setOnCancelListener((i) -> {synchronized(memoryErrorLock){memoryErrorLock.notifyAll();}});
                b.show();
            });
            synchronized (memoryErrorLock) {
                memoryErrorLock.wait();
            }
        }
        JMinecraftVersionList.Version versionInfo = Tools.getVersionInfo(gameSetting);

        String[] launchArgs = getMinecraftArgs(gameSetting, versionInfo, gameSetting.gameDirectory);

        // ctx.appendlnToLog("Minecraft Args: " + Arrays.toString(launchArgs));

        String launchClassPath = generateLaunchClassPath(gameSetting,versionInfo,gameSetting.currentVersion.substring(gameSetting.currentVersion.lastIndexOf("/")+1));

        List<String> javaArgList = new ArrayList<String>();
        if (ctx.jreReleaseList.get("JAVA_VERSION").equals("1.8.0")) {
            getCacioJavaArgs(gameSetting.runtimePath,javaArgList, false);
        }
        javaArgList.add("-cp");
        javaArgList.add(getLWJGL3ClassPath(gameSetting.runtimePath) + ":" + launchClassPath);

        javaArgList.add(versionInfo.mainClass);
        javaArgList.addAll(Arrays.asList(launchArgs));
        // ctx.appendlnToLog("full args: "+javaArgList.toString());
        JREUtils.launchJavaVM(ctx, javaArgList,gameSetting);
    }
    
    public static void getCacioJavaArgs(String path,List<String> javaArgList, boolean isHeadless) {
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
        File cacioDir = new File(path + "/caciocavallo");
        if (cacioDir.exists() && cacioDir.isDirectory()) {
            for (File file : cacioDir.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    cacioClasspath.append(":" + file.getAbsolutePath());
                }
            }
        }
        javaArgList.add(cacioClasspath.toString());
    }

    private static String getLWJGL3ClassPath(String path) {
        StringBuilder libStr = new StringBuilder();
        File lwjgl3Folder = new File(path, "lwjgl3");
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

    private static boolean isClientFirst = false;
    public static String generateLaunchClassPath(GameSetting gameSetting,JMinecraftVersionList.Version info,String actualname) {
        StringBuilder libStr = new StringBuilder(); //versnDir + "/" + version + "/" + version + ".jar:";

        String[] classpath = generateLibClasspath(gameSetting,info);

        if (isClientFirst) {
            libStr.append(getPatchedFile(gameSetting,actualname));
        }
        for (String perJar : classpath) {
            if (!new File(perJar).exists()) {
                Log.d(APP_NAME, "Ignored non-exists file: " + perJar);
                continue;
            }
            libStr.append((isClientFirst ? ":" : "") + perJar + (!isClientFirst ? ":" : ""));
        }
        if (!isClientFirst) {
            libStr.append(getPatchedFile(gameSetting,actualname));
        }

        return libStr.toString();
    }

    public static String[] generateLibClasspath(GameSetting gameSetting,JMinecraftVersionList.Version info) {
        List<String> libDir = new ArrayList<String>();

        for (DependentLibrary libItem: info.libraries) {
            String[] libInfos = libItem.name.split(":");
            libDir.add(gameSetting.gameFileDirectory + "/libraries/" + Tools.artifactToPath(libInfos[0], libInfos[1], libInfos[2]));
        }
        return libDir.toArray(new String[0]);
    }

    public static String artifactToPath(String group, String artifact, String version) {
        return group.replaceAll("\\.", "/") + "/" + artifact + "/" + version + "/" + artifact + "-" + version + ".jar";
    }

    public static String getPatchedFile(GameSetting gameSetting,String version) {
        return gameSetting.gameFileDirectory + "/versions/" + version + "/" + version + ".jar";
    }

    public static String[] getMinecraftArgs(GameSetting gameSetting, JMinecraftVersionList.Version versionInfo, String strGameDir) {
        String username = gameSetting.account.auth_player_name;
        String versionName = versionInfo.id;
        if (versionInfo.inheritsFrom != null) {
            versionName = versionInfo.inheritsFrom;
        }

        String userType = "mojang";

        File gameDir = new File(strGameDir);
        gameDir.mkdirs();

        Map<String, String> varArgMap = new ArrayMap<>();
        varArgMap.put("auth_access_token", gameSetting.account.auth_access_token);
        varArgMap.put("auth_player_name", username);
        varArgMap.put("auth_uuid", gameSetting.account.auth_uuid);
        varArgMap.put("assets_root", gameSetting.gameFileDirectory + "/assets");
        varArgMap.put("assets_index_name", versionInfo.assets);
        varArgMap.put("game_assets", gameSetting.gameFileDirectory + "/assets");
        varArgMap.put("game_directory", gameDir.getAbsolutePath());
        varArgMap.put("user_properties", "{}");
        varArgMap.put("user_type", userType);
        varArgMap.put("version_name", versionName);
        varArgMap.put("version_type", versionInfo.type);

        List<String> minecraftArgs = new ArrayList<String>();
        if (versionInfo.arguments != null) {
            // Support Minecraft 1.13+
            for (Object arg : versionInfo.arguments.game) {
                if (arg instanceof String) {
                    minecraftArgs.add((String) arg);
                } else {
                    /*
                    JMinecraftVersionList.Arguments.ArgValue argv = (JMinecraftVersionList.Arguments.ArgValue) arg;
                    if (argv.values != null) {
                        minecraftArgs.add(argv.values[0]);
                    } else {

                         for (JMinecraftVersionList.Arguments.ArgValue.ArgRules rule : arg.rules) {
                         // rule.action = allow
                         // TODO implement this
                         }

                    }
                    */
                }
            }
        }
        minecraftArgs.add("--width");
        minecraftArgs.add(Integer.toString(CallbackBridge.windowWidth));
        minecraftArgs.add("--height");
        minecraftArgs.add(Integer.toString(CallbackBridge.windowHeight));
        minecraftArgs.add("--fullscreenWidth");
        minecraftArgs.add(Integer.toString(CallbackBridge.windowWidth));
        minecraftArgs.add("--fullscreenHeight");
        minecraftArgs.add(Integer.toString(CallbackBridge.windowHeight));

        String[] argsFromJson = JSONUtils.insertJSONValueList(
                splitAndFilterEmpty(
                        versionInfo.minecraftArguments == null ?
                                fromStringArray(minecraftArgs.toArray(new String[0])):
                                versionInfo.minecraftArguments
                ), varArgMap
        );
        // Tools.dialogOnUiThread(this, "Result args", Arrays.asList(argsFromJson).toString());
        return argsFromJson;
    }

    private static String[] splitAndFilterEmpty(String argStr) {
        List<String> strList = new ArrayList<String>();
        for (String arg : argStr.split(" ")) {
            if (!arg.isEmpty()) {
                strList.add(arg);
            }
        }
        strList.add("--fullscreen");
        return strList.toArray(new String[0]);
    }

    public static String fromStringArray(String[] strArr) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            if (i > 0) builder.append(" ");
            builder.append(strArr[i]);
        }

        return builder.toString();
    }

    public static DisplayMetrics getDisplayMetrics(Activity ctx) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if(SDK_INT >= Build.VERSION_CODES.N && (ctx.isInMultiWindowMode() || ctx.isInPictureInPictureMode())
        || PREF_NOTCH_SIZE == -1 ){
            //For devices with free form/split screen, we need window size, not screen size.
            displayMetrics = ctx.getResources().getDisplayMetrics();
        }else{
            if (SDK_INT >= Build.VERSION_CODES.R) {
                ctx.getDisplay().getRealMetrics(displayMetrics);
            } else {
                 ctx.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            }
            if(!PREF_IGNORE_NOTCH){
                //Remove notch width when it isn't ignored.
                displayMetrics.widthPixels -= PREF_NOTCH_SIZE;
            }
        }
        return displayMetrics;
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

    public static void updateWindowSize(Activity ctx) {
        currentDisplayMetrics = getDisplayMetrics(ctx);

        CallbackBridge.physicalWidth = (int) (currentDisplayMetrics.widthPixels);
        CallbackBridge.physicalHeight = (int) (currentDisplayMetrics.heightPixels);
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

    public static final String LIBNAME_OPTIFINE = "optifine:OptiFine";

    public static JMinecraftVersionList.Version getVersionInfo(GameSetting gameSetting) {
        String currentVersion = gameSetting.currentVersion;
        String gameFileDirectory = gameSetting.gameFileDirectory;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JMinecraftVersionList.Version customVer = gson.fromJson(read(currentVersion + "/" + currentVersion.substring(currentVersion.lastIndexOf("/")+1) + ".json"), JMinecraftVersionList.Version.class);
            for (DependentLibrary lib : customVer.libraries) {
                if (lib.name.startsWith(LIBNAME_OPTIFINE)) {
                    customVer.optifineLib = lib;
                }
            }
            if (customVer.inheritsFrom == null || customVer.inheritsFrom.equals(customVer.id)) {
                return customVer;
            } else {
                JMinecraftVersionList.Version inheritsVer = null;
                try{
                    inheritsVer = gson.fromJson(read(gameFileDirectory + "/versions/" + customVer.inheritsFrom + "/" + customVer.inheritsFrom + ".json"), JMinecraftVersionList.Version.class);
                }catch(IOException e) {
                    throw new RuntimeException("Can't find the source version for "+ currentVersion.substring(currentVersion.lastIndexOf("/")+1) +" (req version="+customVer.inheritsFrom+")");
                }
                inheritsVer.inheritsFrom = inheritsVer.id;
                insertSafety(inheritsVer, customVer,
                        "assetIndex", "assets", "id",
                        "mainClass", "minecraftArguments",
                        "optifineLib", "releaseTime", "time", "type"
                );

                List<DependentLibrary> libList = new ArrayList<DependentLibrary>(Arrays.asList(inheritsVer.libraries));
                try {
                    loop_1:
                    for (DependentLibrary lib : customVer.libraries) {
                        String libName = lib.name.substring(0, lib.name.lastIndexOf(":"));
                        for (int i = 0; i < libList.size(); i++) {
                            DependentLibrary libAdded = libList.get(i);
                            String libAddedName = libAdded.name.substring(0, libAdded.name.lastIndexOf(":"));

                            if (libAddedName.equals(libName)) {
                                Log.d(APP_NAME, "Library " + libName + ": Replaced version " +
                                        libName.substring(libName.lastIndexOf(":") + 1) + " with " +
                                        libAddedName.substring(libAddedName.lastIndexOf(":") + 1));
                                libList.set(i, lib);
                                continue loop_1;
                            }
                        }

                        libList.add(lib);
                    }
                } finally {
                    inheritsVer.libraries = libList.toArray(new DependentLibrary[0]);
                }

                // Inheriting Minecraft 1.13+ with append custom args
                if (inheritsVer.arguments != null && customVer.arguments != null) {
                    List totalArgList = new ArrayList();
                    totalArgList.addAll(Arrays.asList(inheritsVer.arguments.game));

                    int nskip = 0;
                    for (int i = 0; i < customVer.arguments.game.length; i++) {
                        if (nskip > 0) {
                            nskip--;
                            continue;
                        }

                        Object perCustomArg = customVer.arguments.game[i];
                        if (perCustomArg instanceof String) {
                            String perCustomArgStr = (String) perCustomArg;
                            // Check if there is a duplicate argument on combine
                            if (perCustomArgStr.startsWith("--") && totalArgList.contains(perCustomArgStr)) {
                                perCustomArg = customVer.arguments.game[i + 1];
                                if (perCustomArg instanceof String) {
                                    perCustomArgStr = (String) perCustomArg;
                                    // If the next is argument value, skip it
                                    if (!perCustomArgStr.startsWith("--")) {
                                        nskip++;
                                    }
                                }
                            } else {
                                totalArgList.add(perCustomArgStr);
                            }
                        } else if (!totalArgList.contains(perCustomArg)) {
                            totalArgList.add(perCustomArg);
                        }
                    }

                    inheritsVer.arguments.game = totalArgList.toArray(new Object[0]);
                }

                return inheritsVer;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertSafety(JMinecraftVersionList.Version targetVer, JMinecraftVersionList.Version fromVer, String... keyArr) {
        for (String key : keyArr) {
            Object value = null;
            try {
                Field fieldA = fromVer.getClass().getDeclaredField(key);
                value = fieldA.get(fromVer);
                if (((value instanceof String) && !((String) value).isEmpty()) || value != null) {
                    Field fieldB = targetVer.getClass().getDeclaredField(key);
                    fieldB.set(targetVer, value);
                }
            } catch (Throwable th) {
                Log.w(Tools.APP_NAME, "Unable to insert " + key + "=" + value, th);
            }
        }
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

    public static void ignoreNotch(boolean shouldIgnore, Activity ctx){
        if (SDK_INT >= P) {
            if (shouldIgnore) {
                ctx.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                ctx.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
            ctx.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            Tools.updateWindowSize(ctx);
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
