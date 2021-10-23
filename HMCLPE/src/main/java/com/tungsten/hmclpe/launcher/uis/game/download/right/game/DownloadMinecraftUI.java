package com.tungsten.hmclpe.launcher.uis.game.download.right.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.list.download.minecraft.game.DownloadGameListAdapter;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class DownloadMinecraftUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public LinearLayout downloadMinecraftUI;

    private LinearLayout hintLayout;

    private CheckBox checkRelease;
    private CheckBox checkSnapshot;
    private CheckBox checkOld;

    private ProgressBar loadingProgress;
    private ListView mcList;

    public DownloadMinecraftUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadMinecraftUI = activity.findViewById(R.id.ui_download_minecraft);

        hintLayout = activity.findViewById(R.id.download_minecraft_hint_layout);
        hintLayout.setOnClickListener(this);

        checkRelease = activity.findViewById(R.id.checkbox_release);
        checkSnapshot = activity.findViewById(R.id.checkbox_snapshot);
        checkOld = activity.findViewById(R.id.checkbox_old);

        checkRelease.setChecked(true);

        checkRelease.setOnCheckedChangeListener(this);
        checkSnapshot.setOnCheckedChangeListener(this);
        checkOld.setOnCheckedChangeListener(this);

        loadingProgress = activity.findViewById(R.id.loading_minecraft_list_progress);
        mcList = activity.findViewById(R.id.download_minecraft_version_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(downloadMinecraftUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.downloadUI.startDownloadGameUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadMinecraftUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.downloadUI.startDownloadGameUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(){
        new Thread(){
            @Override
            public void run(){
                loadingHandler.sendEmptyMessage(0);
                try {
                    String response = NetworkUtils.doGet(NetworkUtils.toURL(DownloadUrlSource.getSubUrl(activity.launcherSetting.downloadUrlSource,DownloadUrlSource.VERSION_MANIFEST)));
                    Gson gson = new Gson();
                    VersionManifest versionManifest = gson.fromJson(response,VersionManifest.class);
                    ArrayList<VersionManifest.Versions> list = new ArrayList<>();
                    for (VersionManifest.Versions versions : versionManifest.versions){
                        if (checkRelease.isChecked() && versions.type.equals("release")){
                            list.add(versions);
                        }
                        if (checkSnapshot.isChecked() && versions.type.equals("snapshot")){
                            list.add(versions);
                        }
                        if (checkOld.isChecked() && (versions.type.equals("old_alpha") || versions.type.equals("old_beta"))){
                            list.add(versions);
                        }
                    }
                    DownloadGameListAdapter downloadGameListAdapter = new DownloadGameListAdapter(context,activity,list);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mcList.setAdapter(downloadGameListAdapter);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadingHandler.sendEmptyMessage(1);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v == hintLayout){
            Uri uri = Uri.parse("https://afdian.net/@bangbang93");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onLoaded() {
        activity.uiManager.downloadUI.startDownloadGameUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        init();
    }

    @SuppressLint("HandlerLeak")
    private final Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                mcList.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.VISIBLE);
            }
            if (msg.what == 1){
                mcList.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.GONE);
            }
        }
    };
}
