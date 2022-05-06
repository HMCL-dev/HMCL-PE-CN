package com.tungsten.hmclpe.launcher.download.fabric;

import android.os.AsyncTask;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.gson.GsonUtils;
import com.tungsten.hmclpe.utils.io.DownloadUtil;

import java.io.IOException;

public class InstallFabricAPITask extends AsyncTask<ModListBean.Version,Integer,Exception> {

    private MainActivity activity;
    private String name;
    private DownloadTaskListAdapter adapter;
    private InstallFabricAPICallback callback;

    public InstallFabricAPITask (MainActivity activity,String name,DownloadTaskListAdapter adapter,InstallFabricAPICallback callback) {
        this.activity = activity;
        this.name = name;
        this.adapter = adapter;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onStart();
    }

    @Override
    protected Exception doInBackground(ModListBean.Version... versions) {
        ModListBean.Version fabricAPIVersion = versions[0];
        String path;
        if (PublicGameSetting.isUsingIsolateSetting(activity.launcherSetting.gameFileDirectory + "/versions/" + name)) {
            path = PrivateGameSetting.getGameDir(activity.launcherSetting.gameFileDirectory,activity.launcherSetting.gameFileDirectory + "/versions/" + name, GsonUtils.getPrivateGameSettingFromFile(activity.launcherSetting.gameFileDirectory + "/versions/" + name + "/hmclpe.cfg").gameDirSetting);
        }
        else {
            path = PrivateGameSetting.getGameDir(activity.launcherSetting.gameFileDirectory,activity.launcherSetting.gameFileDirectory + "/versions/" + name,GsonUtils.getPrivateGameSettingFromFile(AppManifest.SETTING_DIR + "/private_game_setting.json").gameDirSetting);
        }
        String modPath = path + "/mods/" + fabricAPIVersion.getFile().getFilename();
        String url = fabricAPIVersion.getFile().getUrl();
        DownloadTaskListBean bean = new DownloadTaskListBean(fabricAPIVersion.getFile().getFilename(), url, modPath,null);
        DownloadTask.DownloadFeedback feedback = new DownloadTask.DownloadFeedback() {
            @Override
            public void updateProgress(long curr, long max) {
                long progress = 100 * curr / max;
                bean.progress = (int) progress;
                activity.runOnUiThread(() -> {
                    adapter.onProgress(bean);
                });
            }

            @Override
            public void updateSpeed(String speed) {

            }
        };
        for (int i = 0;i < 5;i++) {
            try {
                activity.runOnUiThread(() -> {
                    adapter.addDownloadTask(bean);
                });
                if (DownloadUtil.downloadFile(url,modPath,null,feedback)) {
                    activity.runOnUiThread(() -> {
                        adapter.onComplete(bean);
                    });
                    return null;
                }
                else {
                    activity.runOnUiThread(() -> {
                        adapter.onComplete(bean);
                    });
                    if (i == 4) {
                        return new Exception("Failed to download " + fabricAPIVersion.getFile().getFilename());
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
                    adapter.onComplete(bean);
                });
                if (i == 4) {
                    return e;
                }
            }
        }
        return new Exception("Unknown error");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        callback.onFinish(e);
    }

    public interface InstallFabricAPICallback{
        void onStart();
        void onFinish(Exception e);
    }
}
