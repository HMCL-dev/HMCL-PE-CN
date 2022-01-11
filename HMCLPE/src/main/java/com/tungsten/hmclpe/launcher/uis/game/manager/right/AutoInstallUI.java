package com.tungsten.hmclpe.launcher.uis.game.manager.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class AutoInstallUI extends BaseUI {

    public LinearLayout autoInstallUI;

    public AutoInstallUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        autoInstallUI = activity.findViewById(R.id.ui_auto_install);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(autoInstallUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.gameManagerUI.startAutoInstall.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(autoInstallUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.gameManagerUI.startAutoInstall.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

}
