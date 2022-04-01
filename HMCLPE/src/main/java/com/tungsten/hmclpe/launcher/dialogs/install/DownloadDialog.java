package com.tungsten.hmclpe.launcher.dialogs.install;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.gson.Gson;
import com.leo618.zip.IZipCallback;
import com.leo618.zip.ZipManager;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.fabric.FabricLoaderVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.forge.ForgeVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.download.minecraft.liteloader.LiteLoaderVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.optifine.OptifineVersion;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Arguments;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.task.install.DownloadMinecraftTask;
import com.tungsten.hmclpe.task.install.InstallOptifineTask;
import com.tungsten.hmclpe.utils.Lang;
import com.tungsten.hmclpe.utils.file.AssetsUtils;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.file.FileUtils;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.DownloadUtil;
import com.tungsten.hmclpe.utils.network.NetSpeed;
import com.tungsten.hmclpe.utils.network.NetSpeedTimer;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DownloadDialog extends Dialog implements View.OnClickListener, Handler.Callback {

    private Context context;
    private MainActivity activity;

    public int maxDownloadTask;

    public String name;
    public VersionManifest.Version version;
    public ForgeVersion forgeVersion;
    public OptifineVersion optifineVersion;
    public LiteLoaderVersion liteLoaderVersion;
    public FabricLoaderVersion fabricVersion;
    public ModListBean.Version fabricAPIVersion;

    public Version gameVersionJson;

    private RecyclerView taskListView;
    public DownloadTaskListAdapter downloadTaskListAdapter;

    private DownloadTask downloadTask;

    private NetSpeedTimer netSpeedTimer;
    private TextView speedText;
    private Button cancelButton;

    public DownloadDialog(@NonNull Context context, MainActivity activity, String name, VersionManifest.Version version,ForgeVersion forgeVersion,OptifineVersion optifineVersion,LiteLoaderVersion liteLoaderVersion,FabricLoaderVersion fabricVersion,ModListBean.Version fabricAPIVersion) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.maxDownloadTask = activity.launcherSetting.maxDownloadTask;
        this.name = name;
        this.version = version;
        this.forgeVersion = forgeVersion;
        this.optifineVersion = optifineVersion;
        this.liteLoaderVersion = liteLoaderVersion;
        this.fabricVersion = fabricVersion;
        this.fabricAPIVersion = fabricAPIVersion;
        setContentView(R.layout.dialog_install_game);
        setCancelable(false);
        init();
    }

    @Override
    public void onClick(View v) {
        if (v == cancelButton){
            netSpeedTimer.stopSpeedTimer();
            this.dismiss();
        }
    }

    private void init(){
        taskListView = findViewById(R.id.download_task_list);

        taskListView.setLayoutManager(new LinearLayoutManager(context));
        downloadTaskListAdapter = new DownloadTaskListAdapter(context);
        taskListView.setAdapter(downloadTaskListAdapter);
        Objects.requireNonNull(taskListView.getItemAnimator()).setAddDuration(0L);
        taskListView.getItemAnimator().setChangeDuration(0L);
        taskListView.getItemAnimator().setMoveDuration(0L);
        taskListView.getItemAnimator().setRemoveDuration(0L);
        ((SimpleItemAnimator)taskListView.getItemAnimator()).setSupportsChangeAnimations(false);

        speedText = findViewById(R.id.download_speed_text);
        cancelButton = findViewById(R.id.cancel_install_game);
        cancelButton.setOnClickListener(this);

        Handler handler = new Handler(this);
        netSpeedTimer = new NetSpeedTimer(context, new NetSpeed(), handler).setDelayTime(0).setPeriodTime(1000);
        netSpeedTimer.startSpeedTimer();

        startDownloadTasks();
    }

    private void startDownloadTasks(){
        System.out.println("---------------------------------------------------------------source:" + DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource));
        if (!new File(activity.launcherSetting.gameFileDirectory + "/launcher_profiles.json").exists()) {
            AssetsUtils.getInstance(activity.getApplicationContext()).copyAssetsToSD("launcher_profiles.json", activity.launcherSetting.gameFileDirectory + "/launcher_profiles.json");
        }
        DownloadMinecraftTask downloadMinecraftTask = new DownloadMinecraftTask(context,activity,this);
        downloadMinecraftTask.execute(version);
    }

    public void downloadMinecraft(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, () -> {
            downloadTaskListAdapter.onComplete(downloadTaskListAdapter.getItem(0));
            if (optifineVersion != null) {
                String mirror = "https://bmclapi2.bangbang93.com/optifine/" + optifineVersion.mcVersion + "/" + optifineVersion.type + "/" + optifineVersion.patch;
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
            else {
                if (forgeVersion != null) {

                }
                else {
                    if (liteLoaderVersion != null) {

                    }
                    else {
                        if (fabricVersion != null) {

                        }
                        else {
                            if (fabricAPIVersion != null) {

                            }
                            else {
                                installJson();
                            }
                        }
                    }
                }
            }
        });
    }

    public void unZipOptifineInstaller() {
        downloadTaskListAdapter.addDownloadTask(new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_optifine),"",""));
        ZipManager.unzip(AppManifest.INSTALL_DIR + "/optifine/" + optifineVersion.fileName, AppManifest.INSTALL_DIR + "/optifine/installer", new IZipCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int percentDone) {

            }

            @Override
            public void onFinish(boolean success) {
                if (success) {
                    downloadTaskListAdapter.getItem(0).progress = 50;
                    downloadTaskListAdapter.onProgress(downloadTaskListAdapter.getItem(0));
                    InstallOptifineTask task = new InstallOptifineTask(context,activity,DownloadDialog.this);
                    task.execute(optifineVersion);
                }
            }
        });
    }

    public void downloadForge(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, () -> {
            if (liteLoaderVersion != null) {

            }
            else {
                installJson();
            }
        });
    }

    public void downloadLiteLoader(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, this::installJson);
    }

    public void downloadFabric(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, () -> {
            if (liteLoaderVersion != null) {

            }
            else {
                installJson();
            }
        });
    }

    public void downloadFabricAPI(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, this::installJson);
    }

    public void installJson(){
        String gameFilePath = activity.launcherSetting.gameFileDirectory;
        String mainClass = gameVersionJson.getMainClass();
        String minecraftArgument = null;
        int mainClassPriority = 0;
        int minecraftArgumentPriority = 0;
        for (Version v : gameVersionJson.getPatches()) {
            if (v.getPriority() > mainClassPriority) {
                mainClass = v.getMainClass();
                mainClassPriority = v.getPriority();
            }
        }
        for (Version v : gameVersionJson.getPatches()) {
            if (v.getMinecraftArguments().isPresent() && v.getPriority() >= minecraftArgumentPriority) {
                minecraftArgument = v.getMinecraftArguments().get();
                minecraftArgumentPriority = v.getPriority();
            }
        }
        gameVersionJson.setMinecraftArguments(minecraftArgument);
        gameVersionJson = gameVersionJson.setMainClass(mainClass);
        for (Version v : gameVersionJson.getPatches()) {
            if (gameVersionJson.getArguments().isPresent() && v.getArguments().isPresent() && !v.getId().equals("game")) {
                gameVersionJson = gameVersionJson.setArguments(Arguments.merge(gameVersionJson.getArguments().get(),v.getArguments().get()));
            }
            if (!gameVersionJson.getArguments().isPresent() && v.getArguments().isPresent()) {
                gameVersionJson = gameVersionJson.setArguments(v.getArguments().get());
            }
        }
        for (Version v : gameVersionJson.getPatches()) {
            if (!v.getId().equals("game")) {
                gameVersionJson = gameVersionJson.setLibraries(Lang.merge(gameVersionJson.getLibraries(), v.getLibraries()));
            }
        }
        Gson gson = JsonUtils.defaultGsonBuilder()
                .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                .registerTypeAdapter(Bits.class, new Bits.Serializer())
                .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                .create();
        String string = gson.toJson(gameVersionJson);
        FileStringUtils.writeFile(gameFilePath + "/versions/" + name + "/" + name + ".json",string);
        dismiss();
    }

    public void startDownloadTask(ArrayList<DownloadTaskListBean> tasks,OnDownloadFinishListener onDownloadFinishListener) {
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
                onDownloadFinishListener.onFinish();
            }

            @Override
            public void onCancelled() {

            }
        });
        if (activity.launcherSetting.autoDownloadTaskQuantity) {
            maxDownloadTask = 64;
        }
        downloadTask.setMaxTask(maxDownloadTask);
        downloadTask.execute(new Map[]{map});
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case NetSpeedTimer.NET_SPEED_TIMER_DEFAULT:
                String speed = (String)msg.obj;
                speedText.setText(speed);
                break;
            default:
                break;
        }
        return false;
    }

    public interface OnDownloadFinishListener{
        void onFinish();
    }

}
