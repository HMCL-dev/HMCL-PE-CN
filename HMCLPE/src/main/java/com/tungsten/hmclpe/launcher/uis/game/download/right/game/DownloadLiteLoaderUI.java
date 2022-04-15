package com.tungsten.hmclpe.launcher.uis.game.download.right.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.liteloader.LiteLoaderGameVersions;
import com.tungsten.hmclpe.launcher.download.liteloader.LiteLoaderVersion;
import com.tungsten.hmclpe.launcher.download.liteloader.LiteLoaderVersionsRoot;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.list.download.minecraft.liteloader.DownloadLiteLoaderListAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class DownloadLiteLoaderUI extends BaseUI implements View.OnClickListener {

    public LinearLayout downloadLiteLoaderUI;

    public String version;

    private LinearLayout hintLayout;

    private ListView liteLoaderListView;
    private ProgressBar progressBar;
    private TextView back;

    public static final String LITELOADER_LIST = "http://dl.liteloader.com/versions/versions.json";

    public DownloadLiteLoaderUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadLiteLoaderUI = activity.findViewById(R.id.ui_install_lite_loader_list);

        hintLayout = activity.findViewById(R.id.download_lite_loader_hint_layout);
        hintLayout.setOnClickListener(this);

        liteLoaderListView = activity.findViewById(R.id.lite_loader_version_list);
        progressBar = activity.findViewById(R.id.loading_lite_loader_list_progress);
        back = activity.findViewById(R.id.back_to_install_ui_lite_loader);

        back.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.lite_loader_list_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(downloadLiteLoaderUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadLiteLoaderUI,activity,context,true);
    }

    private void init(){
        new Thread(){
            @Override
            public void run() {
                loadingHandler.sendEmptyMessage(0);
                ArrayList<LiteLoaderVersion> list = new ArrayList<>();
                try {
                    String response = NetworkUtils.doGet(NetworkUtils.toURL(LITELOADER_LIST));
                    Gson gson = JsonUtils.defaultGsonBuilder()
                            .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                            .create();
                    LiteLoaderVersionsRoot liteLoaderVersionsRoot = gson.fromJson(response, LiteLoaderVersionsRoot.class);
                    ArrayList<String> gameVersions = new ArrayList<>();
                    for (Map.Entry<String, LiteLoaderGameVersions> entry : liteLoaderVersionsRoot.getVersions().entrySet()) {
                        String gameVersion = entry.getKey();
                        gameVersions.add(gameVersion);
                    }
                    if (!gameVersions.contains(version)){
                        loadingHandler.sendEmptyMessage(2);
                    }
                    else {
                        LiteLoaderGameVersions liteLoaderGameVersions = liteLoaderVersionsRoot.getVersions().get(version);
                        Map<String, LiteLoaderVersion> liteLoader = liteLoaderGameVersions.getArtifacts() == null ? liteLoaderGameVersions.getSnapshots().getLiteLoader() : liteLoaderGameVersions.getArtifacts().getLiteLoader();
                        for (Map.Entry<String, LiteLoaderVersion> loaderVersionEntry : liteLoader.entrySet()) {
                            if (!loaderVersionEntry.getKey().equals("latest")){
                                list.add(loaderVersionEntry.getValue());
                            }
                        }
                        Collections.sort(list,new LiteLoaderCompareTool());
                        DownloadLiteLoaderListAdapter downloadLiteLoaderListAdapter = new DownloadLiteLoaderListAdapter(context,activity,version,list);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                liteLoaderListView.setAdapter(downloadLiteLoaderListAdapter);
                            }
                        });
                        loadingHandler.sendEmptyMessage(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        if (view == hintLayout){
            Uri uri = Uri.parse("https://afdian.net/@bangbang93");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
        if (view == back){
            activity.backToLastUI();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                liteLoaderListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 1){
                liteLoaderListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 2){
                liteLoaderListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        }
    };

    private class LiteLoaderCompareTool implements Comparator<LiteLoaderVersion> {
        @Override
        public int compare(LiteLoaderVersion versionPri, LiteLoaderVersion versionSec) {
            int timePri = Integer.parseInt(versionPri.getTimestamp());
            int timeSec = Integer.parseInt(versionSec.getTimestamp());
            if(timePri > timeSec) {
                return -1;
            }
            else if(timePri == timeSec) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }
}
