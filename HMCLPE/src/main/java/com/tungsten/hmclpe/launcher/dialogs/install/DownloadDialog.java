package com.tungsten.hmclpe.launcher.dialogs.install;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.fabric.FabricLoaderVersion;
import com.tungsten.hmclpe.launcher.download.fabric.InstallFabricAPI;
import com.tungsten.hmclpe.launcher.download.forge.ForgeVersion;
import com.tungsten.hmclpe.launcher.download.game.VersionManifest;
import com.tungsten.hmclpe.launcher.download.liteloader.LiteLoaderVersion;
import com.tungsten.hmclpe.launcher.download.optifine.OptifineVersion;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Arguments;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.download.fabric.InstallFabric;
import com.tungsten.hmclpe.launcher.download.forge.InstallForge;
import com.tungsten.hmclpe.launcher.download.liteloader.InstallLiteLoader;
import com.tungsten.hmclpe.launcher.download.optifine.InstallOptifine;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.launcher.download.game.DownloadMinecraftTask;
import com.tungsten.hmclpe.utils.Lang;
import com.tungsten.hmclpe.utils.file.AssetsUtils;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.NetSpeed;
import com.tungsten.hmclpe.utils.io.NetSpeedTimer;
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
            downloadLiteLoader();
        });
    }

    public void downloadLiteLoader(){
        if (liteLoaderVersion != null) {
            InstallLiteLoader installLiteLoader = new InstallLiteLoader(context, activity, downloadTaskListAdapter, liteLoaderVersion, (success, patch) -> {
                mergePatch(patch);
                downloadForge();
            });
            installLiteLoader.install();
        }
        else {
            downloadForge();
        }
    }

    public void downloadForge(){
        if (forgeVersion != null) {
            InstallForge installForge = new InstallForge(context, activity, downloadTaskListAdapter, forgeVersion,name, (success, patch) -> {
                mergePatch(patch);
                downloadOptifine();
            });
            installForge.install();
        }
        else {
            downloadOptifine();
        }
    }

    public void downloadOptifine() {
        if (optifineVersion != null) {
            DownloadTaskListBean bean = new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_optifine),"","","");
            downloadTaskListAdapter.addDownloadTask(bean);
            InstallOptifine installOptifine = new InstallOptifine(context, activity, optifineVersion, name, new InstallOptifine.InstallOptifineCallback() {
                @Override
                public void onProgress(int progress) {
                    bean.progress = progress;
                    downloadTaskListAdapter.onProgress(bean);
                }

                @Override
                public void onFinish(boolean success,Version patch) {
                    downloadTaskListAdapter.onComplete(bean);
                    mergeOptifinePatch(patch);
                    downloadFabric();
                }
            });
            installOptifine.install();
        }
        else {
            downloadFabric();
        }
    }

    public void downloadFabric(){
        if (fabricVersion != null) {
            InstallFabric installFabric = new InstallFabric(context,activity,fabricVersion, version.id,downloadTaskListAdapter, (success, patch) -> {
                mergePatch(patch);
                downloadFabricAPI();
            });
            installFabric.install();
        }
        else {
            downloadFabricAPI();
        }
    }

    public void downloadFabricAPI(){
        if (fabricAPIVersion != null) {
            InstallFabricAPI installFabricAPI = new InstallFabricAPI(context, activity,name, downloadTaskListAdapter, fabricAPIVersion, success -> {
                installJson();
            });
            installFabricAPI.install();
        }
        else {
            installJson();
        }
    }

    public void installJson(){
        String gameFilePath = activity.launcherSetting.gameFileDirectory;
        Gson gson = JsonUtils.defaultGsonBuilder()
                .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                .registerTypeAdapter(Bits.class, new Bits.Serializer())
                .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                .create();
        String string = gson.toJson(gameVersionJson);
        FileStringUtils.writeFile(gameFilePath + "/versions/" + name + "/" + name + ".json",string);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialog_install_success_title));
        builder.setMessage(context.getString(R.string.dialog_install_success_text));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.dialog_install_success_positive), (dialogInterface, i) -> {
            activity.backToLastUI();
            new Thread(() -> {
                activity.uiManager.versionListUI.refreshVersionList();
            }).start();
        });
        dismiss();
        builder.create().show();
    }

    public void mergePatch(Version patch) {
        gameVersionJson = gameVersionJson.addPatch(patch);
        gameVersionJson = gameVersionJson.setMainClass(patch.getMainClass());
        if (patch.getMinecraftArguments().isPresent()) {
            gameVersionJson = gameVersionJson.setMinecraftArguments(patch.getMinecraftArguments().get());
        }
        if (patch.getArguments().isPresent()) {
            if (gameVersionJson.getArguments().isPresent()) {
                gameVersionJson = gameVersionJson.setArguments(Arguments.merge(gameVersionJson.getArguments().get(),patch.getArguments().get()));
            }
            else {
                gameVersionJson = gameVersionJson.setArguments(patch.getArguments().get());
            }
        }
        List<Library> libraries = new ArrayList<>(Lang.merge(gameVersionJson.getLibraries(), patch.getLibraries()));
        for (Library library : gameVersionJson.getLibraries()) {
            for (Library lib : patch.getLibraries()) {
                if (library.equals(lib)) {
                    libraries.remove(lib);
                }
                if (library.getArtifactId().equals(lib.getArtifactId()) && !library.getVersion().equals(lib.getVersion())) {
                    libraries.remove(library);
                }
            }
        }
        gameVersionJson = gameVersionJson.setLibraries(libraries);
    }

    public void mergeOptifinePatch(Version patch) {
        boolean forge = false;
        for (Version v : gameVersionJson.getPatches()) {
            if (v.getId().equals("forge")) {
                forge = true;
                break;
            }
        }
        gameVersionJson = gameVersionJson.addPatch(patch);
        if (patch.getMinecraftArguments().isPresent()) {
            gameVersionJson = gameVersionJson.setMinecraftArguments(patch.getMinecraftArguments().get());
        }
        if (forge) {
            if (patch.getArguments().isPresent()) {
                if (gameVersionJson.getArguments().isPresent()) {
                    gameVersionJson = gameVersionJson.setArguments(Arguments.merge(gameVersionJson.getArguments().get(),new Arguments().addGameArguments("--tweakClass", "optifine.OptiFineForgeTweaker")));
                }
                else {
                    gameVersionJson = gameVersionJson.setArguments(new Arguments().addGameArguments("--tweakClass", "optifine.OptiFineForgeTweaker"));
                }
            }
        }
        else {
            gameVersionJson = gameVersionJson.setMainClass(patch.getMainClass());
            if (patch.getArguments().isPresent()) {
                if (gameVersionJson.getArguments().isPresent()) {
                    gameVersionJson = gameVersionJson.setArguments(Arguments.merge(gameVersionJson.getArguments().get(),patch.getArguments().get()));
                }
                else {
                    gameVersionJson = gameVersionJson.setArguments(patch.getArguments().get());
                }
            }
        }
        List<Library> libraries = new ArrayList<>(Lang.merge(gameVersionJson.getLibraries(), patch.getLibraries()));
        for (Library library : gameVersionJson.getLibraries()) {
            for (Library lib : patch.getLibraries()) {
                if (library.equals(lib)) {
                    libraries.remove(lib);
                }
                if (library.getArtifactId().equals(lib.getArtifactId()) && !library.getVersion().equals(lib.getVersion())) {
                    libraries.remove(library);
                }
            }
        }
        gameVersionJson = gameVersionJson.setLibraries(libraries);
    }

    public void startDownloadTask(ArrayList<DownloadTaskListBean> tasks,OnDownloadFinishListener onDownloadFinishListener) {
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
            public void onFinished(ArrayList<DownloadTaskListBean> failedFile) {
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
        downloadTask.execute(tasks);
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
