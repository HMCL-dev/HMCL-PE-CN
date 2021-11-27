package com.tungsten.hmclpe.launcher.uis.universal.setting.right.launcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class DownloadSettingUI extends BaseUI {

    public LinearLayout downloadSettingUI;

    public DownloadSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadSettingUI = activity.findViewById(R.id.ui_setting_download);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(downloadSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startDownloadSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startDownloadSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

}
