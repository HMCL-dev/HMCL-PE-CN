package com.tungsten.hmclpe.launcher.uis.game.manager.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class VersionSettingUI extends BaseUI {

    public LinearLayout versionSettingUI;

    public VersionSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        versionSettingUI = activity.findViewById(R.id.ui_version_setting);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(versionSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.gameManagerUI.startGameSetting.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(versionSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.gameManagerUI.startGameSetting.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onLoaded() {
        activity.uiManager.gameManagerUI.startGameSetting.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
    }
}
