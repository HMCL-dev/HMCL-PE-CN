package com.tungsten.hmclpe.launcher.uis.game.download.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class DownloadMinecraftUI extends BaseUI implements View.OnClickListener {

    public LinearLayout downloadMinecraftUI;

    private LinearLayout hintLayout;

    public DownloadMinecraftUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadMinecraftUI = activity.findViewById(R.id.ui_download_minecraft);

        hintLayout = activity.findViewById(R.id.download_minecraft_hint_layout);
        hintLayout.setOnClickListener(this);
    }

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

    private void init(){

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
}
