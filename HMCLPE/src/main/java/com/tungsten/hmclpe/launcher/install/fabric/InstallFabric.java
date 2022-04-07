package com.tungsten.hmclpe.launcher.install.fabric;

import android.content.Context;
import android.os.Handler;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.fabric.FabricLoaderVersion;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class InstallFabric {

    private Context context;
    private MainActivity activity;
    private FabricLoaderVersion fabricVersion;
    private String mcVersion;
    private DownloadTaskListAdapter adapter;
    private InstallFabricCallback callback;

    private Handler handler = new Handler();

    private DownloadTaskListBean bean;

    public InstallFabric (Context context, MainActivity activity, FabricLoaderVersion fabricVersion, String mcVersion, DownloadTaskListAdapter adapter, InstallFabricCallback callback) {
        this.context = context;
        this.activity = activity;
        this.fabricVersion = fabricVersion;
        this.mcVersion = mcVersion;
        this.adapter = adapter;
        this.callback = callback;
    }

    public void install() {
        bean = new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_fabric),"","");
        adapter.addDownloadTask(bean);
        new Thread(() -> {
            String url = "https://meta.fabricmc.net/v2/versions/loader/" + mcVersion + "/" + fabricVersion.version + "/profile/json";
            try {
                String patch = NetworkUtils.doGet(NetworkUtils.toURL(url));
                handler.post(() -> {
                    downloadLibs(patch);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void downloadLibs(String patchStr) {
        Gson gson = JsonUtils.defaultGsonBuilder()
                .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                .registerTypeAdapter(Bits.class, new Bits.Serializer())
                .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                .create();
        Version patch = gson.fromJson(patchStr,Version.class);
        ArrayList<DownloadTaskListBean> list = new ArrayList<>();
        String head;
        for (Library library : patch.getLibraries()){
            if (DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource) == 0) {
                head = library.getDownload().getUrl();
            }
            else if (DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource) == 1) {
                head = DownloadUrlSource.getSubUrl(1,DownloadUrlSource.LIBRARIES) + "/";
            }
            else {
                head = DownloadUrlSource.getSubUrl(2,DownloadUrlSource.LIBRARIES) + "/";
            }
            String url = head + library.getPath();
            DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                    url,
                    activity.launcherSetting.gameFileDirectory + "/libraries/" + library.getPath());
            list.add(bean);
        }
        startDownloadTask(list, () -> {
            adapter.onComplete(bean);
            callback.onFinish(true,patch.setId("fabric").setVersion(fabricVersion.version).setPriority(30000));
        });
    }

    public void startDownloadTask(ArrayList<DownloadTaskListBean> tasks, OnDownloadFinishListener onDownloadFinishListener) {
        ArrayMap<String,String> map = new ArrayMap<>();
        for (DownloadTaskListBean bean : tasks){
            map.put(bean.url,bean.path);
        }
        DownloadTask downloadTask = new DownloadTask(context, new DownloadTask.Feedback() {
            @Override
            public void addTask(DownloadTaskListBean bean) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addDownloadTask(bean);
                    }
                });
            }

            @Override
            public void updateProgress(DownloadTaskListBean bean) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.onProgress(bean);
                    }
                });
            }

            @Override
            public void updateSpeed(String speed) {

            }

            @Override
            public void removeTask(DownloadTaskListBean bean) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.onComplete(bean);
                    }
                });
            }

            @Override
            public void onFinished(Map<String, String> failedFile) {
                onDownloadFinishListener.onFinish();
            }

            @Override
            public void onCancelled() {

            }
        });
        int maxDownloadTask = activity.launcherSetting.maxDownloadTask;
        if (activity.launcherSetting.autoDownloadTaskQuantity) {
            maxDownloadTask = 64;
        }
        downloadTask.setMaxTask(maxDownloadTask);
        downloadTask.execute(new Map[]{map});
    }

    public interface InstallFabricCallback{
        void onFinish(boolean success, Version patch);
    }

    public interface OnDownloadFinishListener{
        void onFinish();
    }

}
