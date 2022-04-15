package com.tungsten.hmclpe.launcher.uis.game.manager.right;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.control.ControllerManagerDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.io.File;
import java.util.ArrayList;

public class VersionSettingUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    public LinearLayout versionSettingUI;

    public static final int PICK_GAME_DIR_REQUEST_ISOLATED = 7300;

    public String versionName;

    private ImageView icon;
    private ImageButton editVersionIcon;
    private ImageButton deleteVersionIcon;

    private CheckBox checkIsolateSetting;
    private Button switchToGlobalSetting;

    private LinearLayout isolateSettingLayout;

    private PrivateGameSetting privateGameSetting;

    private LinearLayout showJavaSetting;
    private TextView javaPathText;
    private ImageView showJava;
    private LinearLayout javaSetting;
    private int javaSettingHeight;
    private LinearLayout showGameDirSetting;
    private TextView gameDirText;
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

    public VersionSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate() {
        super.onCreate();
        versionSettingUI = activity.findViewById(R.id.ui_version_setting);

        icon = activity.findViewById(R.id.version_icon_view);
        editVersionIcon = activity.findViewById(R.id.edit_version_icon);
        deleteVersionIcon = activity.findViewById(R.id.reset_version_icon);

        checkIsolateSetting = activity.findViewById(R.id.check_isolated_setting);
        switchToGlobalSetting = activity.findViewById(R.id.start_global_game_setting_from_private);
        isolateSettingLayout = activity.findViewById(R.id.isolate_game_setting);

        showJavaSetting = activity.findViewById(R.id.show_java_selector_isolate);
        javaPathText = activity.findViewById(R.id.java_path_text_isolate);
        showJava = activity.findViewById(R.id.show_java_isolate);
        javaSetting = activity.findViewById(R.id.java_setting_isolate);
        showGameDirSetting = activity.findViewById(R.id.show_game_directory_selector_isolate);
        gameDirText = activity.findViewById(R.id.game_directory_text_isolate);
        showGameDir = activity.findViewById(R.id.show_game_dir_isolate);
        gameDirSetting = activity.findViewById(R.id.game_dir_setting_isolate);
        showControlSetting = activity.findViewById(R.id.show_control_type_selector_isolate);
        controlTypeText = activity.findViewById(R.id.control_type_isolate);
        showControl = activity.findViewById(R.id.show_control_type_isolate);
        controlSetting = activity.findViewById(R.id.control_type_setting_isolate);

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

        checkJavaAuto = activity.findViewById(R.id.check_java_path_auto_isolate);
        checkJava8 = activity.findViewById(R.id.check_java_path_8_isolate);
        checkJava17 = activity.findViewById(R.id.check_java_path_17_isolate);
        java8Path = activity.findViewById(R.id.java_8_path_isolate);
        java17Path = activity.findViewById(R.id.java_17_path_isolate);
        java8Path.setText(AppManifest.JAVA_DIR + "/default");
        java17Path.setText(AppManifest.JAVA_DIR + "/JRE17");

        checkGameDirDefault = activity.findViewById(R.id.check_default_game_dir_isolate);
        checkGameDirIsolate = activity.findViewById(R.id.check_isolate_game_dir_isolate);
        checkGameDirCustom = activity.findViewById(R.id.check_custom_game_dir_isolate);
        editGameDir = activity.findViewById(R.id.edit_game_dir_path_isolate);
        selectGameDir = activity.findViewById(R.id.select_game_dir_path_isolate);

        checkControlTypeTouch = activity.findViewById(R.id.control_type_touch);
        checkControlTypeKeyboard = activity.findViewById(R.id.control_type_keyboard);
        checkControlTypeHandle = activity.findViewById(R.id.control_type_handle);

        launchByBoat = activity.findViewById(R.id.launch_by_boat_isolate);
        launchByPojav = activity.findViewById(R.id.launch_by_pojav_isolate);

        boatRendererGL4ES112 = activity.findViewById(R.id.boat_renderer_gl4es_112_isolate);
        boatRendererGL4ES115 = activity.findViewById(R.id.boat_renderer_gl4es_115_isolate);
        boatRendererGL4ES114 = activity.findViewById(R.id.boat_renderer_gl4es_114_isolate);
        boatRendererVGPU = activity.findViewById(R.id.boat_renderer_vgpu_isolate);

        pojavRendererGL4ES114 = activity.findViewById(R.id.pojav_renderer_gl4es_114_isolate);
        pojavRendererGL4ES115 = activity.findViewById(R.id.pojav_renderer_gl4es_115_isolate);
        pojavRendererGL4ES115P = activity.findViewById(R.id.pojav_renderer_gl4es_115p_isolate);
        pojavRendererVGPU = activity.findViewById(R.id.pojav_renderer_vgpu_isolate);
        pojavRendererVirGL = activity.findViewById(R.id.pojav_renderer_virgl_isolate);

        editVersionIcon.setOnClickListener(this);
        deleteVersionIcon.setOnClickListener(this);

        checkIsolateSetting.setOnCheckedChangeListener(this);
        switchToGlobalSetting.setOnClickListener(this);

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

    public void refresh(String versionName){
        this.versionName = versionName;
        String settingPath = activity.launcherSetting.gameFileDirectory + "/versions/" + versionName + "/hmclpe.cfg";
        if (new File(settingPath).exists() && (GsonUtils.getPrivateGameSettingFromFile(settingPath).forceEnable || GsonUtils.getPrivateGameSettingFromFile(settingPath).enable)) {
            checkIsolateSetting.setChecked(true);
            enableSettingLayout();
            privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(settingPath);
        }
        else {
            checkIsolateSetting.setChecked(false);
            disableSettingLayout();
            privateGameSetting = null;
        }
    }

    private void enableSettingLayout(){
        for (View view : getAllChild(isolateSettingLayout)) {
            view.setAlpha(1f);
            view.setEnabled(true);
        }
        Log.e("enable","true");
    }

    private void disableSettingLayout(){
        for (View view : getAllChild(isolateSettingLayout)) {
            if (!(view instanceof ViewGroup)) {
                view.setAlpha(0.4f);
            }
            view.setEnabled(false);
        }
        Log.e("disable","true");
    }

    private ArrayList<View> getAllChild(ViewGroup viewGroup) {
        ArrayList<View> list = new ArrayList<>();
        for (int i = 0;i < viewGroup.getChildCount();i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                list.addAll(getAllChild((ViewGroup) viewGroup.getChildAt(i)));
            }
            list.add(viewGroup.getChildAt(i));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v == editVersionIcon) {

        }
        if (v == deleteVersionIcon) {

        }
        if (v == switchToGlobalSetting) {
            activity.uiManager.switchMainUI(activity.uiManager.settingUI);
            activity.uiManager.settingUI.settingUIManager.switchSettingUIs(activity.uiManager.settingUI.settingUIManager.universalGameSettingUI);
        }
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
        if (v == launchByBoat){

        }
        if (v == launchByPojav){

        }
        if (v == boatRendererGL4ES112){

        }
        if (v == boatRendererGL4ES115){

        }
        if (v == boatRendererGL4ES114){

        }
        if (v == boatRendererVGPU){

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == checkIsolateSetting) {
            String settingPath = activity.launcherSetting.gameFileDirectory + "/versions/" + versionName + "/hmclpe.cfg";
            if (b) {
                enableSettingLayout();
                if (!new File(settingPath).exists()) {
                    GsonUtils.savePrivateGameSetting(activity.privateGameSetting,settingPath);
                }
                privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(settingPath);
                privateGameSetting.enable = true;
                GsonUtils.savePrivateGameSetting(privateGameSetting,settingPath);
            }
            else {
                disableSettingLayout();
                if (new File(settingPath).exists()) {
                    privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(settingPath);
                    privateGameSetting.enable = false;
                    GsonUtils.savePrivateGameSetting(privateGameSetting,settingPath);
                }
                privateGameSetting = null;
            }
            Log.e("check",Boolean.toString(b));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
