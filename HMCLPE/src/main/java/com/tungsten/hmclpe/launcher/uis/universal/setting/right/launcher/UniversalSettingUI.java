package com.tungsten.hmclpe.launcher.uis.universal.setting.right.launcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class UniversalSettingUI extends BaseUI implements View.OnClickListener {

    public LinearLayout universalSettingUI;

    public UniversalSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        universalSettingUI = activity.findViewById(R.id.ui_setting_universal);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(universalSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startUniversalSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(universalSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startUniversalSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    @Override
    public void onClick(View v) {

    }
}
