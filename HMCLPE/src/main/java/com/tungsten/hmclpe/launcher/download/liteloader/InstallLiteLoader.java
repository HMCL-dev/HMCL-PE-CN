package com.tungsten.hmclpe.launcher.download.liteloader;

import android.content.Context;
import android.util.ArrayMap;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.game.Arguments;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.download.LibraryAnalyzer;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.Lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class InstallLiteLoader {

    private Context context;
    private MainActivity activity;
    private DownloadTaskListAdapter adapter;
    private LiteLoaderVersion liteLoaderVersion;
    private InstallLiteLoaderCallback callback;

    private DownloadTaskListBean bean;

    public InstallLiteLoader (Context context, MainActivity activity, DownloadTaskListAdapter adapter, LiteLoaderVersion liteLoaderVersion, InstallLiteLoaderCallback callback) {
        this.context = context;
        this.activity = activity;
        this.adapter = adapter;
        this.liteLoaderVersion = liteLoaderVersion;
        this.callback = callback;
    }

    public void install(){
        bean = new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_lite_loader),"","","");
        adapter.addDownloadTask(bean);
        Library library = new Library(
                new Artifact("com.mumfrey", "liteloader", liteLoaderVersion.getVersion()),
                "http://dl.liteloader.com/versions/"
        );
        Version patch = new Version(LibraryAnalyzer.LibraryType.LITELOADER.getPatchId(),
                liteLoaderVersion.getVersion(),
                60000,
                new Arguments().addGameArguments("--tweakClass", "com.mumfrey.liteloader.launch.LiteLoaderTweaker"),
                LibraryAnalyzer.LAUNCH_WRAPPER_MAIN,
                Lang.merge(liteLoaderVersion.getLibraries(), Collections.singleton(library)))
                .setLogging(Collections.emptyMap());
        downloadLibs(patch);
    }

    public void downloadLibs(Version patch) {
        ArrayList<DownloadTaskListBean> list = new ArrayList<>();
        String head;
        for (Library library : patch.getLibraries()){
            String url;
            if (library.getDownload().getUrl() == null || library.getDownload().getUrl().equals("") || library.getDownload().getUrl().startsWith("https://libraries.minecraft.net")) {
                if (DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource) == 1) {
                    head = "https://bmclapi2.bangbang93.com/maven/";
                }
                else {
                    head = "https://download.mcbbs.net/maven/";
                }
                url = head + library.getPath();
            }
            else if (library.getDownload().getUrl().startsWith("http://dl.liteloader.com/versions/")) {
                url = "https://bmclapi2.bangbang93.com/liteloader/download?version=" + liteLoaderVersion.getVersion();
            }
            else {
                url = library.getDownload().getUrl();
            }
            DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                    url,
                    activity.launcherSetting.gameFileDirectory + "/libraries/" + library.getPath(),
                    library.getDownload().getSha1());
            list.add(bean);
        }
        startDownloadTask(list, () -> {
            adapter.onComplete(bean);
            callback.onFinish(true,patch);
        });
    }

    public void startDownloadTask(ArrayList<DownloadTaskListBean> tasks, OnDownloadFinishListener onDownloadFinishListener) {
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
            public void onFinished(ArrayList<DownloadTaskListBean> failedFile) {
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
        downloadTask.execute(tasks);
    }

    public interface InstallLiteLoaderCallback{
        void onFinish(boolean success, Version patch);
    }

    public interface OnDownloadFinishListener{
        void onFinish();
    }

}
