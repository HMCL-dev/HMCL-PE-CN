package com.tungsten.hmclpe.launcher.uis.universal.setting.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class UniversalGameSettingUI extends BaseUI implements CompoundButton.OnCheckedChangeListener {

    public LinearLayout universalGameSettingUI;

    private LinearLayout showJavaSetting;

    public UniversalGameSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        universalGameSettingUI = activity.findViewById(R.id.ui_setting_global_game);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(universalGameSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startGlobalGameSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(universalGameSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startGlobalGameSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onLoaded() {
        activity.uiManager.settingUI.startGlobalGameSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
