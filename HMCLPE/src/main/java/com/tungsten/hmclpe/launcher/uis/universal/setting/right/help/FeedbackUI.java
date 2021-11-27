package com.tungsten.hmclpe.launcher.uis.universal.setting.right.help;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class FeedbackUI extends BaseUI {

    public LinearLayout feedbackUI;

    public FeedbackUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        feedbackUI = activity.findViewById(R.id.ui_feedback);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(feedbackUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startFeedbackUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(feedbackUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startFeedbackUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

}
