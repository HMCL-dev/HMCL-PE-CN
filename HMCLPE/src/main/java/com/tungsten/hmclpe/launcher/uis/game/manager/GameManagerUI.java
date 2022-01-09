package com.tungsten.hmclpe.launcher.uis.game.manager;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class GameManagerUI extends BaseUI implements View.OnClickListener {

    public LinearLayout gameManagerUI;
    public String versionName;

    public GameManagerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gameManagerUI = activity.findViewById(R.id.ui_game_manager);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.game_manager_ui_title) + " - " + versionName,activity.uiManager.uis.get(activity.uiManager.uis.size() - 2) != activity.uiManager.mainUI,false);
        CustomAnimationUtils.showViewFromLeft(gameManagerUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(gameManagerUI,activity,context,true);
    }

    @Override
    public void onClick(View v) {

    }

    private void init(){

    }
}
