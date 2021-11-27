package com.tungsten.hmclpe.launcher.uis.universal.setting.right.launcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class ExteriorSettingUI extends BaseUI {

    public LinearLayout exteriorSettingUI;

    public ExteriorSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        exteriorSettingUI = activity.findViewById(R.id.ui_setting_exterior);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(exteriorSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startExteriorSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(exteriorSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startExteriorSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }
}
