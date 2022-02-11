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

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.forge.ForgeVersion;
import com.tungsten.hmclpe.launcher.list.download.minecraft.forge.DownloadForgeListAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class DownloadForgeUI extends BaseUI implements View.OnClickListener {

    public LinearLayout downloadForgeUI;

    public static final String FORGE_VERSION_MANIFEST = "https://bmclapi2.bangbang93.com/forge/minecraft/";

    public String version;

    private LinearLayout hintLayout;

    private ListView forgeListView;
    private ProgressBar progressBar;

    public DownloadForgeUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadForgeUI = activity.findViewById(R.id.ui_install_forge_list);

        hintLayout = activity.findViewById(R.id.download_forge_hint_layout);
        hintLayout.setOnClickListener(this);

        forgeListView = activity.findViewById(R.id.forge_version_list);
        progressBar = activity.findViewById(R.id.loading_forge_list_progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.forge_list_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(downloadForgeUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadForgeUI,activity,context,true);
    }

    private void init(){
        new Thread(){
            @Override
            public void run() {
                String manifestUrl = FORGE_VERSION_MANIFEST + version;
                loadingHandler.sendEmptyMessage(0);
                try {
                    String response = NetworkUtils.doGet(NetworkUtils.toURL(manifestUrl));
                    Gson gson = new Gson();
                    ForgeVersion[] forgeVersion = gson.fromJson(response, ForgeVersion[].class);
                    ArrayList<ForgeVersion> list = new ArrayList<>();
                    list.addAll(Arrays.asList(forgeVersion));
                    Collections.sort(list,new ForgeCompareTool());
                    DownloadForgeListAdapter downloadForgeListAdapter = new DownloadForgeListAdapter(context,activity,list);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            forgeListView.setAdapter(downloadForgeListAdapter);
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
    public void onClick(View view) {
        if (view == hintLayout){
            Uri uri = Uri.parse("https://afdian.net/@bangbang93");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                forgeListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
            if (msg.what == 1){
                forgeListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private class ForgeCompareTool implements Comparator<ForgeVersion> {
        @Override
        public int compare(ForgeVersion versionPri, ForgeVersion versionSec) {
            if(versionPri.getBuild() > versionSec.getBuild()) {
                return -1;
            }
            else if(versionPri.getBuild() == versionSec.getBuild()) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }
}
