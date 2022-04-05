package com.tungsten.hmclpe.launcher.install;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.leo618.zip.IZipCallback;
import com.leo618.zip.ZipManager;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.optifine.OptifineVersion;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.service.install.InstallOptifineService;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.file.FileUtils;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.DownloadUtil;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

public class InstallOptifine {

    private Context context;
    private MainActivity activity;
    private OptifineVersion optifineVersion;
    private DownloadTaskListAdapter downloadTaskListAdapter;
    private String name;
    private InstallOptifineCallback callback;

    private Handler handler = new Handler();

    public InstallOptifine (Context context, MainActivity activity, OptifineVersion optifineVersion, DownloadTaskListAdapter downloadTaskListAdapter,String name, InstallOptifineCallback callback) {
        this.context = context;
        this.activity = activity;
        this.optifineVersion = optifineVersion;
        this.downloadTaskListAdapter = downloadTaskListAdapter;
        this.name = name;
        this.callback = callback;
    }

    public void install() {
        int source = DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource);
        String start;
        if (source == 2) {
            start = "https://download.mcbbs.net";
        }
        else {
            start = "https://bmclapi2.bangbang93.com";
        }
        String mirror = start + "/optifine/" + optifineVersion.mcVersion + "/" + optifineVersion.type + "/" + optifineVersion.patch;
        if (FileUtils.deleteDirectory(AppManifest.INSTALL_DIR)) {
            DownloadUtil.downloadSingleFile(context, new DownloadTaskListBean(optifineVersion.fileName, mirror, AppManifest.INSTALL_DIR + "/optifine/" + optifineVersion.fileName), new DownloadTask.Feedback() {
                @Override
                public void addTask(DownloadTaskListBean bean) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            downloadTaskListAdapter.addDownloadTask(bean);
                        }
                    });
                }

                @Override
                public void updateProgress(DownloadTaskListBean bean) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            downloadTaskListAdapter.onProgress(bean);
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
                            downloadTaskListAdapter.onComplete(bean);
                        }
                    });
                }

                @Override
                public void onFinished(Map<String, String> failedFile) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            unZipOptifineInstaller();
                        }
                    });
                }

                @Override
                public void onCancelled() {

                }
            });
        }
    }

    public void unZipOptifineInstaller() {
        downloadTaskListAdapter.addDownloadTask(new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_optifine),"",""));
        ZipManager.unzip(AppManifest.INSTALL_DIR + "/optifine/" + optifineVersion.fileName, AppManifest.INSTALL_DIR + "/optifine/installer", new IZipCallback() {
            @Override
            public void onStart() {
                try {
                    FileUtils.createFile(AppManifest.INSTALL_DIR + "/optifine/temp.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(int percentDone) {

            }

            @Override
            public void onFinish(boolean success) {
                if (success) {
                    downloadTaskListAdapter.getItem(0).progress = 50;
                    downloadTaskListAdapter.onProgress(downloadTaskListAdapter.getItem(0));
                    Intent service = new Intent(context, InstallOptifineService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putSerializable("version",optifineVersion);
                    service.putExtras(bundle);
                    getOptifineInstallResult();
                    context.startService(service);
                }
            }
        });
    }

    public void getOptifineInstallResult() {
        FileObserver fileObserver;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            fileObserver = new FileObserver(new File(AppManifest.INSTALL_DIR + "/optifine/temp.json"), FileObserver.ALL_EVENTS) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    AppManifest.initializeManifest(context);
                    System.out.println("-------------------------------------------message received!");
                    notifyListeners();
                    this.stopWatching();
                }
            };
        }
        else {
            fileObserver = new FileObserver(AppManifest.INSTALL_DIR + "/optifine/temp.json", FileObserver.MODIFY) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    AppManifest.initializeManifest(context);
                    System.out.println("-------------------------------------------message received!");
                    notifyListeners();
                    this.stopWatching();
                }
            };
        }
        fileObserver.startWatching();
    }

    public void notifyListeners(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                String string = FileStringUtils.getStringFromFile(AppManifest.INSTALL_DIR + "/optifine/temp.json");
                if (string.equals("failed")) {
                    callback.onFinish(false,null);
                }
                else {
                    Gson gson = JsonUtils.defaultGsonBuilder()
                            .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                            .registerTypeAdapter(Bits.class, new Bits.Serializer())
                            .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                            .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                            .create();
                    Version version = gson.fromJson(string,Version.class);
                    callback.onFinish(true,version);
                }
            }
        });
    }

    public interface InstallOptifineCallback{
        void onFinish(boolean success, Version patch);
    }

}
