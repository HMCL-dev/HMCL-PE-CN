package com.tungsten.hmclpe.launcher.dialogs.install;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
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
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;
import com.tungsten.hmclpe.utils.network.NetSpeed;
import com.tungsten.hmclpe.utils.network.NetSpeedTimer;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.IOException;
import java.util.ArrayList;

public class DownloadDialog extends Dialog implements View.OnClickListener, Handler.Callback {

    private Context context;
    private MainActivity activity;

    private int maxDownloadTask;

    private String name;
    private VersionManifest.Version version;

    private ListView taskListView;
    private ArrayList<DownloadTaskListBean> allTaskList;
    private ArrayList<DownloadTaskListBean> taskList;
    private DownloadTaskListAdapter downloadTaskListAdapter;

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
        taskList = new ArrayList<>();
        downloadTaskListAdapter = new DownloadTaskListAdapter(context,taskList,this);
        taskListView.setAdapter(downloadTaskListAdapter);

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
                            gameFilePath + "/versions/" + name + "/" + name + ".json",
                            ""));
                    //assetIndex.json
                    allTaskList.add(new DownloadTaskListBean(version.getAssetIndex().id + ".json",
                            version.getAssetIndex().getUrl(),
                            gameFilePath + "/assets/indexes/" + version.getAssetIndex().id + ".json",
                            version.getAssetIndex().getSha1()));
                    //client.jar
                    allTaskList.add(new DownloadTaskListBean(name + ".jar",
                            version.getDownloadInfo().getUrl(),
                            gameFilePath + "/versions/" + name + "/" + name + ".jar",
                            version.getDownloadInfo().getSha1()));
                    //libraries
                    for (Library library : version.getLibraries()){
                        DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                                library.getDownload().getUrl(),
                                activity.launcherSetting.gameFileDirectory + "/libraries/" +library.getPath(),
                                library.getDownload().getSha1());
                        allTaskList.add(bean);
                    }
                    //assets
                    for (AssetObject object : assetIndex.getObjects().values()){
                        DownloadTaskListBean bean = new DownloadTaskListBean(object.getHash(),
                                DownloadUrlSource.getSubUrl(activity.launcherSetting.downloadUrlSource,DownloadUrlSource.ASSETS_OBJ) + "/" + object.getLocation(),
                                gameFilePath + "/assets/objects/" + object.getLocation(),
                                object.getHash());
                        allTaskList.add(bean);
                    }
                    FileDownloader fileDownloader = new FileDownloader();
                    fileDownloader.setup(context);
                    fileDownloader.setMaxNetworkThreadCount(12);
                    final FileDownloadListener downloadListener = new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            taskList.add((DownloadTaskListBean) task.getTag());
                            downloadHandler.sendEmptyMessage(0);
                        }

                        @Override
                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            long soFar = soFarBytes;
                            long total = totalBytes;
                            long progress = 100 * soFar / total;
                            for (DownloadTaskListBean bean : taskList){
                                if (((DownloadTaskListBean) task.getTag()).name.equals(bean.name)){
                                    bean.progress = (int) progress;
                                }
                            }
                            downloadHandler.sendEmptyMessage(0);
                        }

                        @Override
                        protected void blockComplete(BaseDownloadTask task) {
                        }

                        @Override
                        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            taskList.remove((DownloadTaskListBean) task.getTag());
                            downloadHandler.sendEmptyMessage(0);
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {
                        }
                    };
                    final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);
                    final ArrayList<BaseDownloadTask> tasks = new ArrayList<>();
                    for (int i = 0; i < allTaskList.size(); i++) {
                        tasks.add(fileDownloader.getImpl().create(allTaskList.get(i).url).setTag(allTaskList.get(i)).setPath(allTaskList.get(i).path));
                    }
                    queueSet.disableCallbackProgressTimes();
                    queueSet.setAutoRetryTimes(1);
                    queueSet.downloadTogether(tasks);
                    queueSet.start();
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
                downloadTaskListAdapter.notifyDataSetChanged();
            }
            if (msg.what == 1){
                dismiss();
            }
        }
    };

}
