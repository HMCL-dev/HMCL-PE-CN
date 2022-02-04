package com.tungsten.hmclpe.launcher.uis.universal.setting.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

public class UniversalGameSettingUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public LinearLayout universalGameSettingUI;

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
        currentLauncher = activity.findViewById(R.id.current_launcher);
        showGameLauncher = activity.findViewById(R.id.show_game_launcher);
        gameLauncherSetting = activity.findViewById(R.id.game_launcher_selector);
        showBoatRendererSetting = activity.findViewById(R.id.show_boat_render_selector);
        currentBoatRenderer = activity.findViewById(R.id.current_boat_renderer);
        showBoatRenderer = activity.findViewById(R.id.show_boat_renderer);
        boatRendererSetting = activity.findViewById(R.id.boat_render_selector);
        showPojavRendererSetting = activity.findViewById(R.id.show_pojav_render_selector);
        currentPojavRenderer = activity.findViewById(R.id.current_pojav_renderer);
        showPojavRenderer = activity.findViewById(R.id.show_pojav_renderer);
        pojavRendererSetting = activity.findViewById(R.id.pojav_render_selector);

        launchByBoat = activity.findViewById(R.id.launch_by_boat);
        launchByPojav = activity.findViewById(R.id.launch_by_pojav);

        boatRendererGL4ES112 = activity.findViewById(R.id.boat_renderer_gl4es_112);
        boatRendererGL4ES115 = activity.findViewById(R.id.boat_renderer_gl4es_115);

        pojavRendererGL4ES114 = activity.findViewById(R.id.pojav_renderer_gl4es_114);
        pojavRendererGL4ES115 = activity.findViewById(R.id.pojav_renderer_gl4es_115);
        pojavRendererGL4ES115P = activity.findViewById(R.id.pojav_renderer_gl4es_115p);
        pojavRendererVGPU = activity.findViewById(R.id.pojav_renderer_vgpu);
        pojavRendererVirGL = activity.findViewById(R.id.pojav_renderer_virgl);

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
        CustomAnimationUtils.showViewFromLeft(universalGameSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startGlobalGameSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
        init();
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

    private void init(){
        if (activity.privateGameSetting.boatLauncherSetting.enable){
            launchByBoat.setChecked(true);
            launchByPojav.setChecked(false);
            currentLauncher.setText(context.getText(R.string.game_setting_ui_game_launcher_boat));
        }
        else {
            launchByBoat.setChecked(false);
            launchByPojav.setChecked(true);
            currentLauncher.setText(context.getText(R.string.game_setting_ui_game_launcher_pojav));
        }
        if (activity.privateGameSetting.boatLauncherSetting.renderer.equals("libGL112.so.1")){
            boatRendererGL4ES112.setChecked(true);
            boatRendererGL4ES115.setChecked(false);
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_112));
        }
        else if (activity.privateGameSetting.boatLauncherSetting.renderer.equals("libGL115.so.1")){
            boatRendererGL4ES112.setChecked(false);
            boatRendererGL4ES115.setChecked(true);
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_115));
        }
        if (activity.privateGameSetting.pojavLauncherSetting.renderer.equals("opengles2")){
            pojavRendererGL4ES114.setChecked(true);
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVGPU.setChecked(false);
            pojavRendererVirGL.setChecked(false);
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_gl4es_114));
        }
        else if (activity.privateGameSetting.pojavLauncherSetting.renderer.equals("opengles2_5")){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115.setChecked(true);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVGPU.setChecked(false);
            pojavRendererVirGL.setChecked(false);
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_gl4es_115));
        }
        else if (activity.privateGameSetting.pojavLauncherSetting.renderer.equals("opengles3")){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererGL4ES115P.setChecked(true);
            pojavRendererVGPU.setChecked(false);
            pojavRendererVirGL.setChecked(false);
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_gl4es_115p));
        }
        else if (activity.privateGameSetting.pojavLauncherSetting.renderer.equals("opengles3_vgpu")){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVGPU.setChecked(true);
            pojavRendererVirGL.setChecked(false);
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_vgpu));
        }
        else if (activity.privateGameSetting.pojavLauncherSetting.renderer.equals("opengles3_virgl")){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVGPU.setChecked(false);
            pojavRendererVirGL.setChecked(true);
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_virgl));
        }
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
            launchByPojav.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.enable = true;
            activity.privateGameSetting.pojavLauncherSetting.enable = false;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentLauncher.setText(context.getText(R.string.game_setting_ui_game_launcher_boat));
        }
        if (v == launchByPojav){
            launchByBoat.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.enable = false;
            activity.privateGameSetting.pojavLauncherSetting.enable = true;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentLauncher.setText(context.getText(R.string.game_setting_ui_game_launcher_pojav));
        }
        if (v == boatRendererGL4ES112){
            boatRendererGL4ES115.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.renderer = "libGL112.so.1";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_112));
        }
        if (v == boatRendererGL4ES115){
            boatRendererGL4ES112.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.renderer = "libGL115.so.1";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_115));
        }
        if (v == pojavRendererGL4ES114){
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVGPU.setChecked(false);
            pojavRendererVirGL.setChecked(false);
            activity.privateGameSetting.pojavLauncherSetting.renderer = "opengles2";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_gl4es_114));
        }
        if (v == pojavRendererGL4ES115){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVGPU.setChecked(false);
            pojavRendererVirGL.setChecked(false);
            activity.privateGameSetting.pojavLauncherSetting.renderer = "opengles2_5";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_gl4es_115));
        }
        if (v == pojavRendererGL4ES115P){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererVGPU.setChecked(false);
            pojavRendererVirGL.setChecked(false);
            activity.privateGameSetting.pojavLauncherSetting.renderer = "opengles3";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_gl4es_115p));
        }
        if (v == pojavRendererVGPU){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVirGL.setChecked(false);
            activity.privateGameSetting.pojavLauncherSetting.renderer = "opengles3_vgpu";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_vgpu));
        }
        if (v == pojavRendererVirGL){
            pojavRendererGL4ES114.setChecked(false);
            pojavRendererGL4ES115.setChecked(false);
            pojavRendererGL4ES115P.setChecked(false);
            pojavRendererVGPU.setChecked(false);
            activity.privateGameSetting.pojavLauncherSetting.renderer = "opengles3_virgl";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentPojavRenderer.setText(context.getText(R.string.game_setting_ui_pojav_renderer_virgl));
        }
    }
}
