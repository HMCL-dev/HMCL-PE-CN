package com.tungsten.hmclpe.launcher.uis.game.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class GameManagerUI extends BaseUI implements View.OnClickListener {

    public LinearLayout gameManagerUI;
    public GameManagerUIManager gameManagerUIManager;
    public String versionPath;
    public String versionName;

    public LinearLayout startGameSetting;
    public LinearLayout startModManager;
    public LinearLayout startAutoInstall;
    public LinearLayout startWorldManager;

    private LinearLayout testGame;
    private LinearLayout browse;
    private LinearLayout manage;

    public GameManagerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gameManagerUI = activity.findViewById(R.id.ui_game_manager);

        startGameSetting = activity.findViewById(R.id.game_manager_game_setting);
        startModManager = activity.findViewById(R.id.game_manager_manage_mod);
        startAutoInstall = activity.findViewById(R.id.game_manager_auto_install);
        startWorldManager = activity.findViewById(R.id.game_manager_world);

        testGame = activity.findViewById(R.id.game_manager_test_game);
        browse = activity.findViewById(R.id.game_manager_browse);
        manage = activity.findViewById(R.id.game_manager_manage);

        startGameSetting.setOnClickListener(this);
        startModManager.setOnClickListener(this);
        startAutoInstall.setOnClickListener(this);
        startWorldManager.setOnClickListener(this);

        testGame.setOnClickListener(this);
        browse.setOnClickListener(this);
        manage.setOnClickListener(this);

        gameManagerUIManager = new GameManagerUIManager(context,activity);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameManagerUIManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onClick(View v) {
        if (v == startGameSetting){
            gameManagerUIManager.switchGameManagerUIs(gameManagerUIManager.versionSettingUI);
        }
        if (v == startModManager){
            gameManagerUIManager.switchGameManagerUIs(gameManagerUIManager.modManagerUI);
        }
        if (v == startAutoInstall){
            gameManagerUIManager.switchGameManagerUIs(gameManagerUIManager.autoInstallUI);
        }
        if (v == startWorldManager){
            gameManagerUIManager.switchGameManagerUIs(gameManagerUIManager.worldManagerUI);
        }
        if (v == testGame){

        }
        if (v == browse){

        }
        if (v == manage){

        }
    }

    private void init(){
        String newVersionPath = activity.launcherSetting.gameFileDirectory + "/versions/" + versionName;
        if (!newVersionPath.equals(versionPath)) {
            gameManagerUIManager.versionSettingUI.refresh(versionName);
            gameManagerUIManager.modManagerUI.refresh(versionName);
            gameManagerUIManager.autoInstallUI.refresh(versionName);
            gameManagerUIManager.worldManagerUI.refresh(versionName);
            versionPath = newVersionPath;
            Log.e("gameManager","refresh!");
        }
    }
}
