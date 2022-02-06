package com.tungsten.hmclpe.launcher.dialogs.install;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.AssetIndex;
import com.tungsten.hmclpe.launcher.game.AssetObject;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;
import com.tungsten.hmclpe.utils.network.NetSpeed;
import com.tungsten.hmclpe.utils.network.NetSpeedTimer;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DownloadDialog extends Dialog implements View.OnClickListener, Handler.Callback {

    private Context context;
    private MainActivity activity;

    private int maxDownloadTask;

    private String name;
    private VersionManifest.Version version;

    private RecyclerView taskListView;
    private ArrayList<DownloadTaskListBean> allTaskList;
    private DownloadTaskListAdapter downloadTaskListAdapter;

    private DownloadTask downloadTask;

    private NetSpeedTimer netSpeedTimer;
    private TextView speedText;
    private Button cancelButton;

    public DownloadDialog(@NonNull Context context, MainActivity activity, String name, VersionManifest.Version version) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.maxDownloadTask = activity.launcherSetting.maxDownloadTask;
        this.name = name;
        this.version = version;
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
        allTaskList = new ArrayList<>();

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
        String versionJsonUrl = version.url;
        String gameFilePath = activity.launcherSetting.gameFileDirectory;
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    //getting tasks
                    String versionJson = NetworkUtils.doGet(NetworkUtils.toURL(versionJsonUrl));
                    //FileStringUtils.writeFile(gameFilePath + "/versions/" + name + "/" + name + ".json",response);
                    Gson gson = JsonUtils.defaultGsonBuilder()
                            .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                            .registerTypeAdapter(Bits.class, new Bits.Serializer())
                            .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                            .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                            .create();
                    Version version = gson.fromJson(versionJson, Version.class);
                    String assetIndexJson = NetworkUtils.doGet(NetworkUtils.toURL(version.getAssetIndex().getUrl()));
                    AssetIndex assetIndex = gson.fromJson(assetIndexJson,AssetIndex.class);
                    //version.json
                    allTaskList.add(new DownloadTaskListBean(name + ".json",
                            versionJsonUrl,
                            gameFilePath + "/versions/" + name + "/" + name + ".json"));
                    //assetIndex.json
                    allTaskList.add(new DownloadTaskListBean(version.getAssetIndex().id + ".json",
                            version.getAssetIndex().getUrl(),
                            gameFilePath + "/assets/indexes/" + version.getAssetIndex().id + ".json"));
                    //client.jar
                    allTaskList.add(new DownloadTaskListBean(name + ".jar",
                            version.getDownloadInfo().getUrl(),
                            gameFilePath + "/versions/" + name + "/" + name + ".jar"));
                    //libraries
                    for (Library library : version.getLibraries()){
                        if (library.getDownload().getUrl() != null && !library.getDownload().getUrl().equals("")) {
                            DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                                    library.getDownload().getUrl(),
                                    activity.launcherSetting.gameFileDirectory + "/libraries/" +library.getPath());
                            allTaskList.add(bean);
                        }
                    }
                    //assets
                    for (AssetObject object : assetIndex.getObjects().values()){
                        DownloadTaskListBean bean = new DownloadTaskListBean(object.getHash(),
                                DownloadUrlSource.getSubUrl(activity.launcherSetting.downloadUrlSource,DownloadUrlSource.ASSETS_OBJ) + "/" + object.getLocation(),
                                gameFilePath + "/assets/objects/" + object.getLocation());
                        allTaskList.add(bean);
                    }
                    ArrayMap<String,String> map = new ArrayMap<>();
                    for (DownloadTaskListBean bean : allTaskList){
                        map.put(bean.url,bean.path);
                    }
                    downloadTask = new DownloadTask(context, new DownloadTask.Feedback() {

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
                            DownloadDialog.this.dismiss();
                        }

                        @Override
                        public void onCancelled() {

                        }
                    });
                    downloadTask.setMaxTask(activity.launcherSetting.maxDownloadTask);
                    downloadTask.execute(new Map[]{map});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    @SuppressLint("HandlerLeak")
    private final Handler downloadHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){

            }
            if (msg.what == 1){

            }
            if (msg.what == 2){

            }
        }
    };

}
