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
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.game.ClassicVersion;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;
import com.tungsten.hmclpe.utils.network.NetSpeed;
import com.tungsten.hmclpe.utils.network.NetSpeedTimer;

import java.io.IOException;
import java.util.ArrayList;

public class DownloadDialog extends Dialog implements View.OnClickListener, Handler.Callback {

    private Context context;
    private MainActivity activity;
    private String name;
    private VersionManifest.Version version;

    private ListView taskListView;
    private ArrayList<DownloadTaskListBean> taskList;
    private DownloadTaskListAdapter downloadTaskListAdapter;

    private NetSpeedTimer netSpeedTimer;
    private TextView speedText;
    private Button cancelButton;

    public DownloadDialog(@NonNull Context context, MainActivity activity, String name, VersionManifest.Version version) {
        super(context);
        this.context = context;
        this.activity = activity;
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
        taskList = new ArrayList<>();
        downloadTaskListAdapter = new DownloadTaskListAdapter(context,taskList);
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    String response = NetworkUtils.doGet(NetworkUtils.toURL(versionJsonUrl));
                    FileStringUtils.writeFile(gameFilePath + "/versions/" + name + "/" + name + ".json",response);
                    Gson gson = new Gson();
                    Version version = gson.fromJson(response, Version.class);

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
        }
    };

}
