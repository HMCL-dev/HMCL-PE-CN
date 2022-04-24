package com.tungsten.hmclpe.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.tungsten.hmclpe.launcher.game.AssetIndex;
import com.tungsten.hmclpe.launcher.game.AssetObject;
import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;
import com.tungsten.hmclpe.launcher.launch.LaunchVersion;
import com.tungsten.hmclpe.utils.file.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameFileCheckTask extends AsyncTask<GameLaunchSetting, Integer, Boolean> {

    public interface Callback{
        int ERROR_VERSION_NULL = 0;
        int ERROR_FILE_MISSING = 1;
        int ERROR_INDEXES_FILE_MISSING = 2;
        void onStart();
        void onError(int code,List<String> libs,List<String> objects);
        void onFinish(boolean isValidated);
    }

    private WeakReference<Activity> activity;
    private GameFileCheckTask.Callback callback;

    public GameFileCheckTask(Activity activity, GameFileCheckTask.Callback callback) {
        this.activity = new WeakReference<>(activity);
        this.callback = callback;
    }

    @Override
    public void onPreExecute() {
        activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onStart();
            }
        });
    }

    @Override
    public Boolean doInBackground(GameLaunchSetting... args) {
        String libPath = new File(args[0].gameFileDirectory,"libraries").getAbsolutePath();
        String indexesPath = new File(args[0].gameFileDirectory,"assets/indexes").getAbsolutePath();
        String objectsPath = new File(args[0].gameFileDirectory,"assets/objects").getAbsolutePath();
        List<String> libs = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        LaunchVersion version = LaunchVersion.fromDirectory(new File(args[0].currentVersion));
        if (version == null) {
            callback.onError(Callback.ERROR_VERSION_NULL,null,null);
        }
        for (String lib : version.getLibraries()) {
            String sha1 = FileUtils.getFileSha1(new File(libPath, lib).getAbsolutePath());
            if (sha1 != null) {
                if (!sha1.equals(version.getSHA1(lib))) {
                    if (version.getSHA1(lib) != null) {
                        new File(libPath, lib).delete();
                    }
                }
            }
            if (!new File(libPath, lib).exists()) {
                libs.add(lib);
            }
        }
        if (new File(indexesPath, version.assets + ".json").exists()) {
            for (String obj : getObjects(new File(indexesPath, version.assets + ".json").getAbsolutePath())) {
                if (new File(objectsPath, obj).exists()) {
                    String sha1 = FileUtils.getFileSha1(new File(objectsPath, obj).getAbsolutePath());
                    if (sha1 != null) {
                        if (!sha1.equals(new File(objectsPath, obj).getName())) {
                            new File(objectsPath, obj).delete();
                        }
                    }
                }
                if (!new File(objectsPath, obj).exists()) {
                    objects.add(obj);
                }
            }
        } else {
            callback.onError(Callback.ERROR_INDEXES_FILE_MISSING,null,null);
        }
        if (libs.size() == 0 && objects.size() == 0){
            return true;
        }else {
            callback.onError(Callback.ERROR_FILE_MISSING,libs,objects);
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... p1) {

    }

    @Override
    public void onPostExecute(Boolean result) {
        activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onFinish(result);
            }
        });
    }

    public List<String> getObjects(String filePath) {
        AssetIndex mAssetsJson;
        List<String> assets = new ArrayList<>();
        try {
            FileReader reader = new FileReader(filePath);
            mAssetsJson = new Gson().fromJson(reader, AssetIndex.class);
            reader.close();
        } catch (Exception e) {
            return null;
        }
        Map<String, AssetObject> objects = mAssetsJson.getObjects();
        Set<String> keySet = objects.keySet();
        for (String key : keySet) {
            AssetObject assetObject = objects.get(key);
            String path = assetObject.getHash().substring(0, 2);
            String url = path + "/" + assetObject.getHash();
            assets.add(url);
        }
        return assets;
    }
}


