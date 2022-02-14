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

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class DownloadFabricAPIUI extends BaseUI implements View.OnClickListener {

    public LinearLayout downloadFabricAPIUI;

    public String version;

    private LinearLayout hintLayout;

    private ListView fabricAPIListView;
    private ProgressBar progressBar;
    private TextView back;

    public DownloadFabricAPIUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadFabricAPIUI = activity.findViewById(R.id.ui_install_fabric_api_list);

        hintLayout = activity.findViewById(R.id.download_fabric_api_hint_layout);
        hintLayout.setOnClickListener(this);

        fabricAPIListView = activity.findViewById(R.id.fabric_api_version_list);
        progressBar = activity.findViewById(R.id.loading_fabric_api_list_progress);
        back = activity.findViewById(R.id.back_to_install_ui_fabric_api);

        back.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.fabric_api_list_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(downloadFabricAPIUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadFabricAPIUI,activity,context,true);
    }

    private void init(){

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
                fabricAPIListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 1){
                fabricAPIListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 2){
                fabricAPIListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        }
    };
}
