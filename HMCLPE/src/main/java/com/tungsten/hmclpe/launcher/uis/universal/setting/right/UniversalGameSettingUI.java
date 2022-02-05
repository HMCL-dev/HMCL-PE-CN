package com.tungsten.hmclpe.launcher.uis.universal.setting.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;
import com.tungsten.hmclpe.utils.platform.MemoryUtils;

public class UniversalGameSettingUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    public LinearLayout universalGameSettingUI;

    private LinearLayout showJavaSetting;
    private ImageView showJava;
    private LinearLayout javaSetting;
    private int javaSettingHeight;
    private LinearLayout showGameDirSetting;
    private TextView gameDirText;
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

    private RadioButton checkGameDirDefault;
    private RadioButton checkGameDirIsolate;
    private RadioButton checkGameDirCustom;
    private EditText editGameDir;
    private ImageButton selectGameDir;

    private RadioButton launchByBoat;
    private RadioButton launchByPojav;

    private RadioButton boatRendererGL4ES112;
    private RadioButton boatRendererGL4ES115;

    private RadioButton pojavRendererGL4ES114;
    private RadioButton pojavRendererGL4ES115;
    private RadioButton pojavRendererGL4ES115P;
    private RadioButton pojavRendererVGPU;
    private RadioButton pojavRendererVirGL;

    private CheckBox checkAutoRam;
    private SeekBar ramSeekBar;
    private EditText editRam;
    private ProgressBar ramProgressBar;
    private TextView usedRamText;
    private TextView actualRamText;

    private SeekBar scaleFactorSeekBar;
    private EditText editScaleFactor;

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
        gameDirText = activity.findViewById(R.id.game_directory_text);
        showGameDir = activity.findViewById(R.id.show_game_dir);
        gameDirSetting = activity.findViewById(R.id.game_dir_setting);

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

        checkGameDirDefault = activity.findViewById(R.id.check_default_game_dir);
        checkGameDirIsolate = activity.findViewById(R.id.check_isolate_game_dir);
        checkGameDirCustom = activity.findViewById(R.id.check_custom_game_dir);
        editGameDir = activity.findViewById(R.id.edit_game_dir_path);
        selectGameDir = activity.findViewById(R.id.select_game_dir_path);

        launchByBoat = activity.findViewById(R.id.launch_by_boat);
        launchByPojav = activity.findViewById(R.id.launch_by_pojav);

        boatRendererGL4ES112 = activity.findViewById(R.id.boat_renderer_gl4es_112);
        boatRendererGL4ES115 = activity.findViewById(R.id.boat_renderer_gl4es_115);

        pojavRendererGL4ES114 = activity.findViewById(R.id.pojav_renderer_gl4es_114);
        pojavRendererGL4ES115 = activity.findViewById(R.id.pojav_renderer_gl4es_115);
        pojavRendererGL4ES115P = activity.findViewById(R.id.pojav_renderer_gl4es_115p);
        pojavRendererVGPU = activity.findViewById(R.id.pojav_renderer_vgpu);
        pojavRendererVirGL = activity.findViewById(R.id.pojav_renderer_virgl);

        checkAutoRam = activity.findViewById(R.id.check_auto_ram);
        ramSeekBar = activity.findViewById(R.id.ram_seek_bar);
        editRam = activity.findViewById(R.id.edit_ram);
        usedRamText = activity.findViewById(R.id.used_ram_text);
        actualRamText = activity.findViewById(R.id.actual_ram_text);
        ramSeekBar.setMax(MemoryUtils.getTotalDeviceMemory(context));

        ramProgressBar = activity.findViewById(R.id.ram_progress_bar);
        ramProgressBar.setMax(MemoryUtils.getTotalDeviceMemory(context));

        scaleFactorSeekBar = activity.findViewById(R.id.edit_scale_factor);
        editScaleFactor = activity.findViewById(R.id.edit_scale_factor_text);
        scaleFactorSeekBar.setMax(750);

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

        checkAutoRam.setOnCheckedChangeListener(this);
        ramSeekBar.setOnSeekBarChangeListener(this);
        editRam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editRam.getText().toString().equals("")){
                    activity.privateGameSetting.ramSetting.minRam = Integer.parseInt(editRam.getText().toString());
                    activity.privateGameSetting.ramSetting.maxRam = Integer.parseInt(editRam.getText().toString());
                    ramSeekBar.setProgress(Integer.parseInt(editRam.getText().toString()));
                }
                else {
                    activity.privateGameSetting.ramSetting.minRam = 0;
                    activity.privateGameSetting.ramSetting.maxRam = 0;
                    ramSeekBar.setProgress(0);
                }
                GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            }
        });

        scaleFactorSeekBar.setOnSeekBarChangeListener(this);
        editScaleFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editScaleFactor.getText().toString().equals("")){
                    activity.privateGameSetting.scaleFactor = Integer.parseInt(editScaleFactor.getText().toString()) / 100F;
                    scaleFactorSeekBar.setProgress((Integer.parseInt(editScaleFactor.getText().toString()) * 10) - 250);
                }
                else {
                    activity.privateGameSetting.scaleFactor = 0.25F;
                    scaleFactorSeekBar.setProgress(0);
                }
                GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            }
        });

        gameDirSetting.post(new Runnable() {
            @Override
            public void run() {
                gameDirSettingHeight = gameDirSetting.getHeight();
                gameDirSetting.setVisibility(View.GONE);
            }
        });
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
        if (buttonView == checkAutoRam){
            activity.privateGameSetting.ramSetting.autoRam = isChecked;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        checkAutoRam.setChecked(activity.privateGameSetting.ramSetting.autoRam);
        ramProgressBar.setProgress(MemoryUtils.getTotalDeviceMemory(context) - MemoryUtils.getFreeDeviceMemory(context));
        ramSeekBar.setProgress(activity.privateGameSetting.ramSetting.minRam);
        editRam.setText(activity.privateGameSetting.ramSetting.minRam + "");
        usedRamText.setText(context.getText(R.string.game_setting_ui_used_ram) + " " + (float) Math.round(((MemoryUtils.getTotalDeviceMemory(context) - MemoryUtils.getFreeDeviceMemory(context)) / 1024F) * 10) / 10 + " GB / " + (float) Math.round((MemoryUtils.getTotalDeviceMemory(context) / 1024F) * 10) / 10 + " GB");
        actualRamText.setText(context.getText(R.string.game_setting_ui_min_distribution) + " " + (float) Math.round(((activity.privateGameSetting.ramSetting.minRam) / 1024F) * 10) / 10 + " GB / " + context.getText(R.string.game_setting_ui_actual_distribution) + " " + (float) Math.round(((activity.privateGameSetting.ramSetting.minRam) / 1024F) * 10) / 10 + " GB");
        scaleFactorSeekBar.setProgress((int) (activity.privateGameSetting.scaleFactor * 1000) - 250);
        editScaleFactor.setText(((int) (activity.privateGameSetting.scaleFactor * 100)) + "");
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
            HiddenAnimationUtils.newInstance(context,gameDirSetting,showGameDir,gameDirSettingHeight).toggle();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == ramSeekBar && fromUser){
            activity.privateGameSetting.ramSetting.minRam = progress;
            activity.privateGameSetting.ramSetting.maxRam = progress;
            editRam.setText(progress + "");
        }
        if (seekBar == scaleFactorSeekBar && fromUser){
            activity.privateGameSetting.scaleFactor = (progress + 250.0F) / 1000F;
            editScaleFactor.setText(((progress / 10) + 25) + "");
        }
        GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
