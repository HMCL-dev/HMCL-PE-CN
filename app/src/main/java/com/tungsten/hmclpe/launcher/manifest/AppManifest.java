package com.tungsten.hmclpe.launcher.manifest;

import android.content.Context;
import android.os.Environment;

import com.tungsten.hmclpe.utils.file.FileUtils;

public class AppManifest {

    public static String LAUNCHER_DIR = Environment.getExternalStorageDirectory() + "/HMCLPE";
    public static String DEFAULT_GAME_DIR = LAUNCHER_DIR + "/.minecraft";

    public static String INNER_DIR;
    public static String INNER_FILE_DIR;

    public static String EXTERNAL_DIR;

    public static String ACCOUNT_DIR;
    public static String GAME_FILE_DIRECTORY_DIR;
    public static String SETTING_DIR;
    public static String DEBUG_DIR;
    public static String INNER_GAME_DIR;

    public static String DEFAULT_CACHE_DIR;
    public static String DEFAULT_RUNTIME_DIR;

    public static void initializeManifest (Context context){
        INNER_DIR = context.getFilesDir().getParent();
        INNER_FILE_DIR = context.getFilesDir().getAbsolutePath();

        EXTERNAL_DIR = context.getExternalCacheDir().getParent();

        ACCOUNT_DIR = INNER_FILE_DIR + "/accounts";
        GAME_FILE_DIRECTORY_DIR = INNER_FILE_DIR + "/paths";
        SETTING_DIR = INNER_FILE_DIR + "/settings";
        DEBUG_DIR = context.getExternalFilesDir("debug").getAbsolutePath();
        INNER_GAME_DIR = context.getExternalFilesDir(".minecraft").getAbsolutePath();

        DEFAULT_CACHE_DIR = context.getCacheDir().getAbsolutePath();
        DEFAULT_RUNTIME_DIR = context.getDir("runtime",0).getAbsolutePath();

        FileUtils.createDirectory(LAUNCHER_DIR);
        FileUtils.createDirectory(DEFAULT_GAME_DIR);

        FileUtils.createDirectory(INNER_DIR);
        FileUtils.createDirectory(INNER_FILE_DIR);

        FileUtils.createDirectory(EXTERNAL_DIR);

        FileUtils.createDirectory(ACCOUNT_DIR);
        FileUtils.createDirectory(GAME_FILE_DIRECTORY_DIR);
        FileUtils.createDirectory(SETTING_DIR);
        FileUtils.createDirectory(DEBUG_DIR);
        FileUtils.createDirectory(INNER_GAME_DIR);

        FileUtils.createDirectory(DEFAULT_CACHE_DIR);
        FileUtils.createDirectory(DEFAULT_RUNTIME_DIR);
    }

}
