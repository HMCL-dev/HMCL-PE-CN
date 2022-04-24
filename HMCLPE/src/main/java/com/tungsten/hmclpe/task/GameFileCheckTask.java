package com.tungsten.hmclpe.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.tungsten.hmclpe.launcher.game.AssetIndex;
import com.tungsten.hmclpe.launcher.game.AssetObject;
import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;
import com.tungsten.hmclpe.launcher.launch.LaunchVersion;

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
        int ERROR_VERSION_NULL=0;
        int ERROR_FILE_MISSING=1;
        int ERROR_INDEXES_FILE_MISSING=2;
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
        String libPath=new File(args[0].game_directory,"libraries").getAbsolutePath();
        String indexesPath=new File(args[0].game_directory,"assets/indexes").getAbsolutePath();
        String objectsPath=new File(args[0].game_directory,"assets/objects").getAbsolutePath();
        List<String> libs = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        LaunchVersion version = LaunchVersion.fromDirectory(new File(args[0].currentVersion));
        if (version == null) {
            callback.onError(Callback.ERROR_VERSION_NULL,null,null);
        }
        for (String lib : version.getLibraries()) {
            String sha1 = getFileSha1(new File(libPath, lib).getAbsolutePath());
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
                    String sha1 = getFileSha1(new File(objectsPath, obj).getAbsolutePath());
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
        if (libs.size()==0&&objects.size()==0){
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
    private String getFileSha1(String path) {

        try {
            File file=new File(path);

            FileInputStream in = new FileInputStream(file);

            MessageDigest messagedigest;
            messagedigest = MessageDigest.getInstance("SHA-1");

            byte[] buffer = new byte[1024];

            int len = 0;

            while ((len = in.read(buffer)) >0) {
//该对象通过使用 update()方法处理数据

                messagedigest.update(buffer, 0, len);

            }

//对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。

            return bytesToHex(messagedigest.digest()).toLowerCase();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;

    }
    /**
     * 字节数组转Hex
     * @param bytes 字节数组
     * @return Hex
     */
    private String bytesToHex(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        if (bytes != null && bytes.length > 0)
        {
            for (int i = 0; i < bytes.length; i++) {
                String hex = byteToHex(bytes[i]);
                sb.append(hex);
            }
        }
        return sb.toString();
    }
    /**
     * Byte字节转Hex
     * @param b 字节
     * @return Hex
     */
    private String byteToHex(byte b)
    {
        String hexString = Integer.toHexString(b & 0xFF);
        //由于十六进制是由0~9、A~F来表示1~16，所以如果Byte转换成Hex后如果是<16,就会是一个字符（比如A=10），通常是使用两个字符来表示16进制位的,
        //假如一个字符的话，遇到字符串11，这到底是1个字节，还是1和1两个字节，容易混淆，如果是补0，那么1和1补充后就是0101，11就表示纯粹的11
        if (hexString.length() < 2)
        {
            hexString = new StringBuilder(String.valueOf(0)).append(hexString).toString();
        }
        return hexString.toUpperCase();
    }
}


