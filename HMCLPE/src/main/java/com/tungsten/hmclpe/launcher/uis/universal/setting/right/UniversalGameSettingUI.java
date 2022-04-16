package com.tungsten.hmclpe.launcher.uis.universal.setting.right;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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

import androidx.appcompat.widget.SwitchCompat;

import com.tungsten.filepicker.Constants;
import com.tungsten.filepicker.FolderChooser;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.ControlPatternActivity;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.control.ControllerManagerDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;
import com.tungsten.hmclpe.utils.file.UriUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;
import com.tungsten.hmclpe.utils.platform.MemoryUtils;

import java.io.File;

public class UniversalGameSettingUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    public LinearLayout universalGameSettingUI;

    public static final int PICK_GAME_DIR_REQUEST = 1500;

    private LinearLayout showJavaSetting;
    private TextView javaPathText;
    private ImageView showJava;
    private LinearLayout javaSetting;
    private int javaSettingHeight;
    private LinearLayout showGameDirSetting;
    public TextView gameDirText;
    private ImageView showGameDir;
    private LinearLayout gameDirSetting;
    private int gameDirSettingHeight;
    private LinearLayout showControlSetting;
    private TextView controlTypeText;
    private ImageView showControl;
    private LinearLayout controlSetting;
    private int controlSettingHeight;

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

    private RadioButton checkJavaAuto;
    private RadioButton checkJava8;
    private RadioButton checkJava17;
    private TextView java8Path;
    private TextView java17Path;

    private RadioButton checkGameDirDefault;
    private RadioButton checkGameDirIsolate;
    private RadioButton checkGameDirCustom;
    private EditText editGameDir;
    private ImageButton selectGameDir;

    private RadioButton checkControlTypeTouch;
    private RadioButton checkControlTypeKeyboard;
    private RadioButton checkControlTypeHandle;

    private RadioButton launchByBoat;
    private RadioButton launchByPojav;

    private RadioButton boatRendererGL4ES112;
    private RadioButton boatRendererGL4ES115;
    private RadioButton boatRendererGL4ES114;
    private RadioButton boatRendererVGPU;

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

    private SwitchCompat checkLog;

    private SwitchCompat notCheckGameFile;
    private SwitchCompat notCheckForge;
    private SwitchCompat notCheckJVM;

    private EditText editServer;

    private Button manageController;
    private TextView currentControlPattern;
    private ControllerManagerDialog controllerManagerDialog;

    public UniversalGameSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate() {
        super.onCreate();
        universalGameSettingUI = activity.findViewById(R.id.ui_setting_global_game);

        showJavaSetting = activity.findViewById(R.id.show_java_selector);
        javaPathText = activity.findViewById(R.id.java_path_text);
        showJava = activity.findViewById(R.id.show_java);
        javaSetting = activity.findViewById(R.id.java_setting);
        showGameDirSetting = activity.findViewById(R.id.show_game_directory_selector);
        gameDirText = activity.findViewById(R.id.game_directory_text);
        showGameDir = activity.findViewById(R.id.show_game_dir);
        gameDirSetting = activity.findViewById(R.id.game_dir_setting);
        showControlSetting = activity.findViewById(R.id.show_control_type_selector);
        controlTypeText = activity.findViewById(R.id.control_type);
        showControl = activity.findViewById(R.id.show_control_type);
        controlSetting = activity.findViewById(R.id.control_type_setting);

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

        checkJavaAuto = activity.findViewById(R.id.check_java_path_auto);
        checkJava8 = activity.findViewById(R.id.check_java_path_8);
        checkJava17 = activity.findViewById(R.id.check_java_path_17);
        java8Path = activity.findViewById(R.id.java_8_path);
        java17Path = activity.findViewById(R.id.java_17_path);
        java8Path.setText(AppManifest.JAVA_DIR + "/default");
        java17Path.setText(AppManifest.JAVA_DIR + "/JRE17");

        checkGameDirDefault = activity.findViewById(R.id.check_default_game_dir);
        checkGameDirIsolate = activity.findViewById(R.id.check_isolate_game_dir);
        checkGameDirCustom = activity.findViewById(R.id.check_custom_game_dir);
        editGameDir = activity.findViewById(R.id.edit_game_dir_path);
        selectGameDir = activity.findViewById(R.id.select_game_dir_path);

        checkControlTypeTouch = activity.findViewById(R.id.control_type_touch);
        checkControlTypeKeyboard = activity.findViewById(R.id.control_type_keyboard);
        checkControlTypeHandle = activity.findViewById(R.id.control_type_handle);

        launchByBoat = activity.findViewById(R.id.launch_by_boat);
        launchByPojav = activity.findViewById(R.id.launch_by_pojav);

        boatRendererGL4ES112 = activity.findViewById(R.id.boat_renderer_gl4es_112);
        boatRendererGL4ES115 = activity.findViewById(R.id.boat_renderer_gl4es_115);
        boatRendererGL4ES114 = activity.findViewById(R.id.boat_renderer_gl4es_114);
        boatRendererVGPU = activity.findViewById(R.id.boat_renderer_vgpu);

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

        checkLog = activity.findViewById(R.id.switch_log);

        notCheckGameFile = activity.findViewById(R.id.switch_check_mc);
        notCheckForge = activity.findViewById(R.id.switch_check_forge);
        notCheckJVM = activity.findViewById(R.id.switch_check_runtime);

        manageController = activity.findViewById(R.id.manage_control_layout);
        manageController.setOnClickListener(this);
        currentControlPattern = activity.findViewById(R.id.control_layout);

        editServer = activity.findViewById(R.id.edit_mc_server);
        editServer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                activity.privateGameSetting.server = editServer.getText().toString();
                GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            }
        });

        checkLog.setOnCheckedChangeListener(this);

        notCheckGameFile.setOnCheckedChangeListener(this);
        notCheckForge.setOnCheckedChangeListener(this);
        notCheckJVM.setOnCheckedChangeListener(this);

        showJavaSetting.setOnClickListener(this);
        showJava.setOnClickListener(this);
        showGameDirSetting.setOnClickListener(this);
        showGameDir.setOnClickListener(this);
        showControlSetting.setOnClickListener(this);
        showControl.setOnClickListener(this);

        showGameLauncherSetting.setOnClickListener(this);
        showGameLauncher.setOnClickListener(this);
        showBoatRendererSetting.setOnClickListener(this);
        showBoatRenderer.setOnClickListener(this);
        showPojavRendererSetting.setOnClickListener(this);
        showPojavRenderer.setOnClickListener(this);

        checkJavaAuto.setOnClickListener(this);
        checkJava8.setOnClickListener(this);
        checkJava17.setOnClickListener(this);

        checkGameDirDefault.setOnClickListener(this);
        checkGameDirIsolate.setOnClickListener(this);
        checkGameDirCustom.setOnClickListener(this);
        selectGameDir.setOnClickListener(this);
        editGameDir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                activity.privateGameSetting.gameDirSetting.path = editGameDir.getText().toString();
                GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            }
        });

        checkControlTypeTouch.setOnClickListener(this);
        checkControlTypeKeyboard.setOnClickListener(this);
        checkControlTypeHandle.setOnClickListener(this);

        launchByBoat.setOnClickListener(this);
        launchByPojav.setOnClickListener(this);

        boatRendererGL4ES112.setOnClickListener(this);
        boatRendererGL4ES115.setOnClickListener(this);
        boatRendererGL4ES114.setOnClickListener(this);
        boatRendererVGPU.setOnClickListener(this);

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

        javaSetting.post(() -> {
            javaSettingHeight = javaSetting.getHeight();
            javaSetting.setVisibility(View.GONE);
        });
        gameDirSetting.post(() -> {
            gameDirSettingHeight = gameDirSetting.getHeight();
            gameDirSetting.setVisibility(View.GONE);
        });
        controlSetting.post(() -> {
            controlSettingHeight = controlSetting.getHeight();
            controlSetting.setVisibility(View.GONE);
        });
        gameLauncherSetting.post(() -> {
            gameLauncherSettingHeight = gameLauncherSetting.getHeight();
            gameLauncherSetting.setVisibility(View.GONE);
        });
        boatRendererSetting.post(() -> {
            boatRendererSettingHeight = boatRendererSetting.getHeight();
            boatRendererSetting.setVisibility(View.GONE);
        });
        pojavRendererSetting.post(() -> {
            pojavRendererSettingHeight = pojavRendererSetting.getHeight();
            pojavRendererSetting.setVisibility(View.GONE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_GAME_DIR_REQUEST && data != null) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                gameDirText.setText(UriUtils.getRealPathFromUri_AboveApi19(context,uri));
                editGameDir.setText(UriUtils.getRealPathFromUri_AboveApi19(context,uri));
                activity.privateGameSetting.gameDirSetting.path = UriUtils.getRealPathFromUri_AboveApi19(context,uri);
                GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            }
        }
        if (requestCode == ControlPatternActivity.CONTROL_PATTERN_REQUEST_CODE && controllerManagerDialog != null && data != null){
            Uri uri = data.getData();
            String pattern = uri.toString();
            currentControlPattern.setText(pattern);
            activity.privateGameSetting.controlLayout = pattern;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            controllerManagerDialog.currentPattern = pattern;
            controllerManagerDialog.loadList();
        }
        if (requestCode == ControllerManagerDialog.IMPORT_PATTERN_REQUEST_CODE && controllerManagerDialog != null && data != null){
            controllerManagerDialog.onResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkAutoRam){
            activity.privateGameSetting.ramSetting.autoRam = isChecked;
        }
        if (buttonView == checkLog){
            activity.privateGameSetting.log = isChecked;
        }
        if (buttonView == notCheckGameFile){
            activity.privateGameSetting.notCheckMinecraft = isChecked;
        }
        if (buttonView == notCheckForge){
            activity.privateGameSetting.notCheckForge = isChecked;
        }
        if (buttonView == notCheckJVM){
            activity.privateGameSetting.notCheckJvm = isChecked;
        }
        GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
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
        checkLog.setChecked(activity.privateGameSetting.log);
        notCheckGameFile.setChecked(activity.privateGameSetting.notCheckMinecraft);
        notCheckForge.setChecked(activity.privateGameSetting.notCheckForge);
        notCheckJVM.setChecked(activity.privateGameSetting.notCheckJvm);
        editGameDir.setText(activity.privateGameSetting.gameDirSetting.path);
        editServer.setText(activity.privateGameSetting.server);
        currentControlPattern.setText(activity.privateGameSetting.controlLayout);
        if (activity.privateGameSetting.javaSetting.autoSelect){
            javaPathText.setText(context.getString(R.string.game_setting_ui_java_path_auto));
            checkJavaAuto.setChecked(true);
            checkJava8.setChecked(false);
            checkJava17.setChecked(false);
        }
        else {
            if (activity.privateGameSetting.javaSetting.name.equals("default")){
                javaPathText.setText(AppManifest.JAVA_DIR + "/default");
                checkJavaAuto.setChecked(false);
                checkJava8.setChecked(true);
                checkJava17.setChecked(false);
            }
            if (activity.privateGameSetting.javaSetting.name.equals("JRE17")){
                javaPathText.setText(AppManifest.JAVA_DIR + "/JRE17");
                checkJavaAuto.setChecked(false);
                checkJava8.setChecked(false);
                checkJava17.setChecked(true);
            }
        }
        if (activity.privateGameSetting.gameDirSetting.type == 0){
            editGameDir.setEnabled(false);
            selectGameDir.setEnabled(false);
            gameDirText.setText(activity.launcherSetting.gameFileDirectory);
            checkGameDirDefault.setChecked(true);
            checkGameDirIsolate.setChecked(false);
            checkGameDirCustom.setChecked(false);
        }
        else if (activity.privateGameSetting.gameDirSetting.type == 1){
            editGameDir.setEnabled(false);
            selectGameDir.setEnabled(false);
            gameDirText.setText(activity.publicGameSetting.currentVersion);
            checkGameDirDefault.setChecked(false);
            checkGameDirIsolate.setChecked(true);
            checkGameDirCustom.setChecked(false);
        }
        else {
            editGameDir.setEnabled(true);
            selectGameDir.setEnabled(true);
            gameDirText.setText(activity.privateGameSetting.gameDirSetting.path);
            checkGameDirDefault.setChecked(false);
            checkGameDirIsolate.setChecked(false);
            checkGameDirCustom.setChecked(true);
        }
        if (activity.privateGameSetting.controlType == 0){
            controlTypeText.setText(context.getString(R.string.game_setting_ui_control_type_touch));
            checkControlTypeTouch.setChecked(true);
            checkControlTypeKeyboard.setChecked(false);
            checkControlTypeHandle.setChecked(false);
        }
        else if (activity.privateGameSetting.controlType == 1){
            controlTypeText.setText(context.getString(R.string.game_setting_ui_control_type_keyboard));
            checkControlTypeTouch.setChecked(false);
            checkControlTypeKeyboard.setChecked(true);
            checkControlTypeHandle.setChecked(false);
        }
        else {
            controlTypeText.setText(context.getString(R.string.game_setting_ui_control_type_handle));
            checkControlTypeTouch.setChecked(false);
            checkControlTypeKeyboard.setChecked(false);
            checkControlTypeHandle.setChecked(true);
        }
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
            boatRendererGL4ES114.setChecked(false);
            boatRendererVGPU.setChecked(false);
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_112));
        }
        else if (activity.privateGameSetting.boatLauncherSetting.renderer.equals("libGL115.so.1")){
            boatRendererGL4ES112.setChecked(false);
            boatRendererGL4ES115.setChecked(true);
            boatRendererGL4ES114.setChecked(false);
            boatRendererVGPU.setChecked(false);
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_115));
        }
        else if (activity.privateGameSetting.boatLauncherSetting.renderer.equals("libgl4es_114.so")){
            boatRendererGL4ES112.setChecked(false);
            boatRendererGL4ES115.setChecked(false);
            boatRendererGL4ES114.setChecked(true);
            boatRendererVGPU.setChecked(false);
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_114));
        }
        else if (activity.privateGameSetting.boatLauncherSetting.renderer.equals("libvgpu.so")){
            boatRendererGL4ES112.setChecked(false);
            boatRendererGL4ES115.setChecked(false);
            boatRendererGL4ES114.setChecked(false);
            boatRendererVGPU.setChecked(true);
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_vgpu));
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v == showJavaSetting || v == showJava){
            HiddenAnimationUtils.newInstance(context,javaSetting,showJava,javaSettingHeight).toggle();
        }
        if (v == showGameDirSetting || v == showGameDir){
            HiddenAnimationUtils.newInstance(context,gameDirSetting,showGameDir,gameDirSettingHeight).toggle();
        }
        if (v == showControlSetting || v == showControl){
            HiddenAnimationUtils.newInstance(context,controlSetting,showControl,controlSettingHeight).toggle();
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
        if (v == checkJavaAuto){
            javaPathText.setText(context.getString(R.string.game_setting_ui_java_path_auto));
            checkJava8.setChecked(false);
            checkJava17.setChecked(false);
            activity.privateGameSetting.javaSetting.autoSelect = true;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == checkJava8){
            javaPathText.setText(AppManifest.JAVA_DIR + "/default");
            checkJavaAuto.setChecked(false);
            checkJava17.setChecked(false);
            activity.privateGameSetting.javaSetting.autoSelect = false;
            activity.privateGameSetting.javaSetting.name = "default";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == checkJava17){
            javaPathText.setText(AppManifest.JAVA_DIR + "/JRE17");
            checkJavaAuto.setChecked(false);
            checkJava8.setChecked(false);
            activity.privateGameSetting.javaSetting.autoSelect = false;
            activity.privateGameSetting.javaSetting.name = "JRE17";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == checkGameDirDefault){
            editGameDir.setEnabled(false);
            selectGameDir.setEnabled(false);
            gameDirText.setText(activity.launcherSetting.gameFileDirectory);
            checkGameDirIsolate.setChecked(false);
            checkGameDirCustom.setChecked(false);
            activity.privateGameSetting.gameDirSetting.type = 0;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == checkGameDirIsolate){
            editGameDir.setEnabled(false);
            selectGameDir.setEnabled(false);
            gameDirText.setText(activity.publicGameSetting.currentVersion);
            checkGameDirDefault.setChecked(false);
            checkGameDirCustom.setChecked(false);
            activity.privateGameSetting.gameDirSetting.type = 1;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == checkGameDirCustom){
            editGameDir.setEnabled(true);
            selectGameDir.setEnabled(true);
            gameDirText.setText(activity.privateGameSetting.gameDirSetting.path);
            checkGameDirDefault.setChecked(false);
            checkGameDirIsolate.setChecked(false);
            activity.privateGameSetting.gameDirSetting.type = 2;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == selectGameDir){
            Intent intent = new Intent(context, FolderChooser.class);
            intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
            intent.putExtra(Constants.INITIAL_DIRECTORY, new File(AppManifest.DEFAULT_GAME_DIR).getAbsolutePath());
            activity.startActivityForResult(intent, PICK_GAME_DIR_REQUEST);
        }
        if (v == checkControlTypeTouch){
            controlTypeText.setText(context.getString(R.string.game_setting_ui_control_type_touch));
            checkControlTypeKeyboard.setChecked(false);
            checkControlTypeHandle.setChecked(false);
            activity.privateGameSetting.controlType = 0;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == checkControlTypeKeyboard){
            controlTypeText.setText(context.getString(R.string.game_setting_ui_control_type_keyboard));
            checkControlTypeTouch.setChecked(false);
            checkControlTypeHandle.setChecked(false);
            activity.privateGameSetting.controlType = 1;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        if (v == checkControlTypeHandle){
            controlTypeText.setText(context.getString(R.string.game_setting_ui_control_type_handle));
            checkControlTypeKeyboard.setChecked(false);
            checkControlTypeTouch.setChecked(false);
            activity.privateGameSetting.controlType = 2;
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
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
            boatRendererGL4ES114.setChecked(false);
            boatRendererVGPU.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.renderer = "libGL112.so.1";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_112));
        }
        if (v == boatRendererGL4ES115){
            boatRendererGL4ES112.setChecked(false);
            boatRendererGL4ES114.setChecked(false);
            boatRendererVGPU.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.renderer = "libGL115.so.1";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_115));
        }
        if (v == boatRendererGL4ES114){
            boatRendererGL4ES112.setChecked(false);
            boatRendererGL4ES115.setChecked(false);
            boatRendererVGPU.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.renderer = "libgl4es_114.so";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_gl4es_114));
        }
        if (v == boatRendererVGPU){
            boatRendererGL4ES112.setChecked(false);
            boatRendererGL4ES115.setChecked(false);
            boatRendererGL4ES114.setChecked(false);
            activity.privateGameSetting.boatLauncherSetting.renderer = "libvgpu.so";
            GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
            currentBoatRenderer.setText(context.getText(R.string.game_setting_ui_boat_renderer_vgpu));
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
        if (v == manageController){
            controllerManagerDialog = new ControllerManagerDialog(context,activity,activity.launcherSetting.fullscreen, activity.privateGameSetting.controlLayout, new ControllerManagerDialog.OnPatternChangeListener() {
                @Override
                public void onPatternChange(String pattern) {
                    currentControlPattern.setText(pattern);
                    activity.privateGameSetting.controlLayout = pattern;
                    GsonUtils.savePrivateGameSetting(activity.privateGameSetting, AppManifest.SETTING_DIR + "/private_game_setting.json");
                }
            },false);
            controllerManagerDialog.show();
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
