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
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.fabric.FabricLoaderVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.forge.ForgeVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.download.minecraft.liteloader.LiteLoaderVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.optifine.OptifineVersion;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.task.install.DownloadMinecraftTask;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.network.NetSpeed;
import com.tungsten.hmclpe.utils.network.NetSpeedTimer;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DownloadDialog extends Dialog implements View.OnClickListener, Handler.Callback {

    private Context context;
    private MainActivity activity;

    public int maxDownloadTask;

    private String name;
    private VersionManifest.Version version;
    private ForgeVersion forgeVersion;
    private OptifineVersion optifineVersion;
    private LiteLoaderVersion liteLoaderVersion;
    private FabricLoaderVersion fabricVersion;
    private ModListBean.Version fabricAPIVersion;

    public Version gameVersionJson;
    public Version optifineVersionJson;
    public Version forgeVersionJson;
    public Version liteLoaderVersionJson;
    public Version fabricVersionJson;

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
        DownloadMinecraftTask downloadMinecraftTask = new DownloadMinecraftTask(context,activity,this);
        downloadMinecraftTask.execute(version);
    }

    public void downloadMinecraft(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, () -> {
            if (optifineVersion != null) {

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
                                installClient();
                            }
                        }
                    }
                }
            }
        });
    }

    public void downloadOptifine(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, () -> {
            if (forgeVersion != null) {

            }
            else {
                if (liteLoaderVersion != null) {

                }
                else {
                    installClient();
                }
            }
        });
    }

    public void downloadForge(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, () -> {
            if (liteLoaderVersion != null) {

            }
            else {
                installClient();
            }
        });
    }

    public void downloadLiteLoader(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, this::installClient);
    }

    public void downloadFabric(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, () -> {
            if (liteLoaderVersion != null) {

            }
            else {
                installClient();
            }
        });
    }

    public void downloadFabricAPI(ArrayList<DownloadTaskListBean> tasks){
        startDownloadTask(tasks, this::installClient);
    }

    public void installClient(){
        ArrayList<DownloadTaskListBean> tasks = new ArrayList<>();
        String gameFilePath = activity.launcherSetting.gameFileDirectory;
        if (forgeVersion == null) {
            tasks.add(new DownloadTaskListBean(name + ".jar",
                    gameVersionJson.getDownloadInfo().getUrl(),
                    gameFilePath + "/versions/" + name + "/" + name + ".jar"));
            startDownloadTask(tasks, this::installJson);
        }
        else {

        }
    }

    public void installJson(){
        String gameFilePath = activity.launcherSetting.gameFileDirectory;
        Version resultVersionJson = gameVersionJson.addPatch(gameVersionJson.setId("game").setVersion(version.id).setPriority(0));
        Gson gson = JsonUtils.defaultGsonBuilder()
                .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                .registerTypeAdapter(Bits.class, new Bits.Serializer())
                .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                .create();
        String string = gson.toJson(resultVersionJson);
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
