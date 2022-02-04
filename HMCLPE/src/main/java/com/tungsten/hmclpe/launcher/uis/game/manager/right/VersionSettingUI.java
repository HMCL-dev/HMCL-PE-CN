package com.tungsten.hmclpe.launcher.uis.game.manager.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;

public class VersionSettingUI extends BaseUI implements View.OnClickListener {

    public LinearLayout versionSettingUI;

    private LinearLayout showJavaSetting;
    private ImageView showJava;
    private LinearLayout javaSetting;
    private int javaSettingHeight;
    private LinearLayout showGameDirSetting;
    private ImageView showGameDir;
    private LinearLayout gameDirSetting;
    private int gameDirSettingHeight;

    private LinearLayout showGameLauncherSetting;
    private TextView currentLauncher;
    private ImageView showGameLauncher;
    private LinearLayout gameLauncherSetting;
    private int gameLauncherSettingHeight;
    private LinearLayout showBoatRendererSetting;
    private TextView currentBoatRenderer;
    private ImageView showBoatRenderer;
    private LinearLayout boatRendererSetting;
    private int boatRendererSettingHeight;
    private LinearLayout showPojavRendererSetting;
    private TextView currentPojavRenderer;
    private ImageView showPojavRenderer;
    private LinearLayout pojavRendererSetting;
    private int pojavRendererSettingHeight;

    private RadioButton launchByBoat;
    private RadioButton launchByPojav;

    private RadioButton boatRendererGL4ES112;
    private RadioButton boatRendererGL4ES115;

    private RadioButton pojavRendererGL4ES114;
    private RadioButton pojavRendererGL4ES115;
    private RadioButton pojavRendererGL4ES115P;
    private RadioButton pojavRendererVGPU;
    private RadioButton pojavRendererVirGL;

    public VersionSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        versionSettingUI = activity.findViewById(R.id.ui_version_setting);

        showJavaSetting = activity.findViewById(R.id.show_java_selector_isolate);
        showJava = activity.findViewById(R.id.show_java_isolate);
        showGameDirSetting = activity.findViewById(R.id.show_game_directory_selector_isolate);
        showGameDir = activity.findViewById(R.id.show_game_dir_isolate);

        showGameLauncherSetting = activity.findViewById(R.id.show_game_launcher_selector_isolate);
        currentLauncher = activity.findViewById(R.id.current_launcher_isolate);
        showGameLauncher = activity.findViewById(R.id.show_game_launcher_isolate);
        gameLauncherSetting = activity.findViewById(R.id.game_launcher_selector_isolate);
        showBoatRendererSetting = activity.findViewById(R.id.show_boat_render_selector_isolate);
        currentBoatRenderer = activity.findViewById(R.id.current_boat_renderer_isolate);
        showBoatRenderer = activity.findViewById(R.id.show_boat_renderer_isolate);
        boatRendererSetting = activity.findViewById(R.id.boat_render_selector_isolate);
        showPojavRendererSetting = activity.findViewById(R.id.show_pojav_render_selector_isolate);
        currentPojavRenderer = activity.findViewById(R.id.current_pojav_renderer_isolate);
        showPojavRenderer = activity.findViewById(R.id.show_pojav_renderer_isolate);
        pojavRendererSetting = activity.findViewById(R.id.pojav_render_selector_isolate);

        launchByBoat = activity.findViewById(R.id.launch_by_boat_isolate);
        launchByPojav = activity.findViewById(R.id.launch_by_pojav_isolate);

        boatRendererGL4ES112 = activity.findViewById(R.id.boat_renderer_gl4es_112_isolate);
        boatRendererGL4ES115 = activity.findViewById(R.id.boat_renderer_gl4es_115_isolate);

        pojavRendererGL4ES114 = activity.findViewById(R.id.pojav_renderer_gl4es_114_isolate);
        pojavRendererGL4ES115 = activity.findViewById(R.id.pojav_renderer_gl4es_115_isolate);
        pojavRendererGL4ES115P = activity.findViewById(R.id.pojav_renderer_gl4es_115p_isolate);
        pojavRendererVGPU = activity.findViewById(R.id.pojav_renderer_vgpu_isolate);
        pojavRendererVirGL = activity.findViewById(R.id.pojav_renderer_virgl_isolate);

        showJavaSetting.setOnClickListener(this);
        showJava.setOnClickListener(this);
        showGameDirSetting.setOnClickListener(this);
        showGameDir.setOnClickListener(this);

        showGameLauncherSetting.setOnClickListener(this);
        showGameLauncher.setOnClickListener(this);
        showBoatRendererSetting.setOnClickListener(this);
        showBoatRenderer.setOnClickListener(this);
        showPojavRendererSetting.setOnClickListener(this);
        showPojavRenderer.setOnClickListener(this);

        launchByBoat.setOnClickListener(this);
        launchByPojav.setOnClickListener(this);

        boatRendererGL4ES112.setOnClickListener(this);
        boatRendererGL4ES115.setOnClickListener(this);

        pojavRendererGL4ES114.setOnClickListener(this);
        pojavRendererGL4ES115.setOnClickListener(this);
        pojavRendererGL4ES115P.setOnClickListener(this);
        pojavRendererVGPU.setOnClickListener(this);
        pojavRendererVirGL.setOnClickListener(this);

        gameLauncherSetting.post(new Runnable() {
            @Override
            public void run() {
                gameLauncherSettingHeight = gameLauncherSetting.getHeight();
                gameLauncherSetting.setVisibility(View.GONE);
            }
        });
        boatRendererSetting.post(new Runnable() {
            @Override
            public void run() {
                boatRendererSettingHeight = boatRendererSetting.getHeight();
                boatRendererSetting.setVisibility(View.GONE);
            }
        });
        pojavRendererSetting.post(new Runnable() {
            @Override
            public void run() {
                pojavRendererSettingHeight = pojavRendererSetting.getHeight();
                pojavRendererSetting.setVisibility(View.GONE);
            }
        });
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

    @Override
    public void onClick(View v) {
        if (v == showJavaSetting || v == showJava){

        }
        if (v == showGameDirSetting || v == showGameDir){

        }
        if (v == showGameLauncherSetting || v == showGameLauncher){
            HiddenAnimationUtils.newInstance(context,gameLauncherSetting,showGameLauncher,gameLauncherSettingHeight).toggle();
        }
        if (v == showBoatRendererSetting || v == showBoatRenderer){
            HiddenAnimationUtils.newInstance(context,boatRendererSetting,showBoatRenderer,boatRendererSettingHeight).toggle();
        }
        if (v == showPojavRendererSetting || v == showPojavRenderer){
            HiddenAnimationUtils.newInstance(context,pojavRendererSetting,showPojavRenderer,pojavRendererSettingHeight).toggle();
        }
        if (v == launchByBoat){

        }
        if (v == launchByPojav){

        }
        if (v == boatRendererGL4ES112){

        }
        if (v == boatRendererGL4ES115){

        }
        if (v == pojavRendererGL4ES114){

        }
        if (v == pojavRendererGL4ES115){

        }
        if (v == pojavRendererGL4ES115P){

        }
        if (v == pojavRendererVGPU){

        }
        if (v == pojavRendererVirGL){

        }
    }
}
