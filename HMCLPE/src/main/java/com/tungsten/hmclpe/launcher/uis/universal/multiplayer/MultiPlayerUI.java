package com.tungsten.hmclpe.launcher.uis.universal.multiplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.launch.check.LaunchTools;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.io.File;

public class MultiPlayerUI extends BaseUI implements View.OnClickListener {

    public LinearLayout multiPlayerUI;

    public MultiPlayerUIManager multiPlayerUIManager;

    private LinearLayout launch;

    private LinearLayout hiPer;
    private LinearLayout hin2n;

    private LinearLayout help;
    private LinearLayout feedback;

    public MultiPlayerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        multiPlayerUI = activity.findViewById(R.id.ui_multi_player);

        launch = activity.findViewById(R.id.launch_game_from_multiplayer);
        hiPer = activity.findViewById(R.id.multiplayer_hiper);
        hin2n = activity.findViewById(R.id.multiplayer_hin2n);
        help = activity.findViewById(R.id.multiplayer_help);
        feedback = activity.findViewById(R.id.multiplayer_feedback);

        launch.setOnClickListener(this);
        hiPer.setOnClickListener(this);
        hin2n.setOnClickListener(this);
        help.setOnClickListener(this);
        feedback.setOnClickListener(this);

        multiPlayerUIManager = new MultiPlayerUIManager(context, activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.multi_player_ui_title),activity.uiManager.uis.get(activity.uiManager.uis.size() - 2) != activity.uiManager.mainUI,false);
        CustomAnimationUtils.showViewFromLeft(multiPlayerUI,activity,context,true);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(multiPlayerUI,activity,context,true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        multiPlayerUIManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onPause() {
        super.onPause();
        multiPlayerUIManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        multiPlayerUIManager.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view == launch) {
            String settingPath = activity.publicGameSetting.currentVersion + "/hmclpe.cfg";
            String finalPath;
            if (new File(settingPath).exists() && GsonUtils.getPrivateGameSettingFromFile(settingPath) != null && (GsonUtils.getPrivateGameSettingFromFile(settingPath).forceEnable || GsonUtils.getPrivateGameSettingFromFile(settingPath).enable)) {
                finalPath = settingPath;
            }
            else {
                finalPath = AppManifest.SETTING_DIR + "/private_game_setting.json";
            }
            Bundle bundle = new Bundle();
            bundle.putString("setting_path",finalPath);
            bundle.putBoolean("test",false);
            LaunchTools.launch(context, activity, activity.publicGameSetting.currentVersion, bundle);
        }
        if (view == hiPer) {
            multiPlayerUIManager.switchMultiPlayerUIs(multiPlayerUIManager.hiPerUI);
        }
        if (view == hin2n) {
            multiPlayerUIManager.switchMultiPlayerUIs(multiPlayerUIManager.hin2nUI);
        }
        if (view == help) {
            Uri uri = Uri.parse("https://tungstend.github.io/pages/documentation.html?path=/_multiplayer/multiplayer.md");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
        if (view == feedback) {
            Uri uri = Uri.parse("https://github.com/huanghongxun/HMCL-PE-CN/issues");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }
}
