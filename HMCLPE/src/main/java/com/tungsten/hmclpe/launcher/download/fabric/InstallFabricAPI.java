package com.tungsten.hmclpe.launcher.download.fabric;

import android.content.Context;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.gson.GsonUtils;
import com.tungsten.hmclpe.utils.io.DownloadUtil;

import java.util.ArrayList;
import java.util.Map;

public class InstallFabricAPI {

    private Context context;
    private MainActivity activity;
    private String name;
    private DownloadTaskListAdapter adapter;
    private ModListBean.Version fabricAPIVersion;
    private InstallFabricAPICallback callback;

    private DownloadTaskListBean bean;

    public InstallFabricAPI (Context context, MainActivity activity,String name, DownloadTaskListAdapter adapter, ModListBean.Version fabricAPIVersion,InstallFabricAPICallback callback) {
        this.context = context;
        this.activity = activity;
        this.name = name;
        this.adapter = adapter;
        this.fabricAPIVersion = fabricAPIVersion;
        this.callback = callback;
    }

    public void install(){
        bean = new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_fabric_api),"","","");
        adapter.addDownloadTask(bean);
        String path;
        if (PublicGameSetting.isUsingIsolateSetting(activity.launcherSetting.gameFileDirectory + "/versions/" + name)) {
            path = PrivateGameSetting.getGameDir(activity.launcherSetting.gameFileDirectory,activity.launcherSetting.gameFileDirectory + "/versions/" + name,GsonUtils.getPrivateGameSettingFromFile(activity.launcherSetting.gameFileDirectory + "/versions/" + name + "/hmclpe.cfg").gameDirSetting);
        }
        else {
            path = PrivateGameSetting.getGameDir(activity.launcherSetting.gameFileDirectory,activity.launcherSetting.gameFileDirectory + "/versions/" + name,GsonUtils.getPrivateGameSettingFromFile(AppManifest.SETTING_DIR + "/private_game_setting.json").gameDirSetting);
        }
        String modPath = path + "/mods/" + fabricAPIVersion.getFile().getFilename();
        String url = fabricAPIVersion.getFile().getUrl();
        DownloadUtil.downloadSingleFile(context, new DownloadTaskListBean(fabricAPIVersion.getFile().getFilename(), url, modPath,null), new DownloadTask.Feedback() {
            @Override
            public void addTask(DownloadTaskListBean downloadTaskListBean) {
                activity.runOnUiThread(() -> {
                    adapter.addDownloadTask(downloadTaskListBean);
                });
            }

            @Override
            public void updateProgress(DownloadTaskListBean downloadTaskListBean) {
                activity.runOnUiThread(() -> {
                    adapter.onProgress(downloadTaskListBean);
                });
            }

            @Override
            public void updateSpeed(String speed) {

            }

            @Override
            public void removeTask(DownloadTaskListBean downloadTaskListBean) {
                activity.runOnUiThread(() -> {
                    adapter.onComplete(downloadTaskListBean);
                });
            }

            @Override
            public void onFinished(ArrayList<DownloadTaskListBean> failedFile) {
                adapter.onComplete(bean);
                callback.onFinish(failedFile.size() == 0);
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    public interface InstallFabricAPICallback{
        void onFinish(boolean success);
    }

}
