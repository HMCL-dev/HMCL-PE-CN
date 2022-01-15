package com.tungsten.hmclpe.launcher.uis.universal.setting.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;

public class UniversalGameSettingUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public LinearLayout universalGameSettingUI;

    private LinearLayout showJavaSetting;
    private ImageView showJava;
    private LinearLayout showGameDirSetting;
    private ImageView showGameDir;

    private LinearLayout showGameLauncherSetting;
    private ImageView showGameLauncher;
    private LinearLayout showBoatRendererSetting;
    private ImageView showBoatRenderer;
    private LinearLayout showPojavRendererSetting;
    private ImageView showPojavRenderer;

    public UniversalGameSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        universalGameSettingUI = activity.findViewById(R.id.ui_setting_global_game);

        showJavaSetting = activity.findViewById(R.id.show_java_selector);
        showJava = activity.findViewById(R.id.show_java);
        showGameDirSetting = activity.findViewById(R.id.show_game_directory_selector);
        showGameDir = activity.findViewById(R.id.show_game_dir);

        showGameLauncherSetting = activity.findViewById(R.id.show_game_launcher_selector);
        showGameLauncher = activity.findViewById(R.id.show_game_launcher);
        showBoatRendererSetting = activity.findViewById(R.id.show_boat_render_selector);
        showBoatRenderer = activity.findViewById(R.id.show_boat_renderer);
        showPojavRendererSetting = activity.findViewById(R.id.show_pojav_render_selector);
        showPojavRenderer = activity.findViewById(R.id.show_pojav_renderer);

        showJavaSetting.setOnClickListener(this);
        showGameDirSetting.setOnClickListener(this);

        showGameLauncherSetting.setOnClickListener(this);
        showBoatRendererSetting.setOnClickListener(this);
        showPojavRendererSetting.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v == showJavaSetting){
            //HiddenAnimationUtils.newInstance(context,updateSetting,showJava,updateSettingHeight).toggle();
        }
        if (v == showGameDirSetting){

        }
        if (v == showGameLauncherSetting){

        }
        if (v == showBoatRendererSetting){

        }
        if (v == showPojavRendererSetting){

        }
    }
}
