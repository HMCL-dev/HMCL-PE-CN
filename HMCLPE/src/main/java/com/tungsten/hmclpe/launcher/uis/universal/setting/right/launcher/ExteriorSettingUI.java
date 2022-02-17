package com.tungsten.hmclpe.launcher.uis.universal.setting.right.launcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.tools.ColorSelectorDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

public class ExteriorSettingUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    public LinearLayout exteriorSettingUI;

    private LinearLayout selectTheme;
    private View colorView;
    private TextView colorText;
    private SwitchCompat transBarSwitch;
    private RadioButton defaultRadio;
    private RadioButton classicRadio;
    private RadioButton customRadio;
    private RadioButton onlineRadio;
    private EditText editBgPath;
    private EditText editBgUrl;
    private ImageButton selectBgPath;

    public ExteriorSettingUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        exteriorSettingUI = activity.findViewById(R.id.ui_setting_exterior);

        selectTheme = activity.findViewById(R.id.select_theme);
        colorView = activity.findViewById(R.id.theme_color_view);
        colorText = activity.findViewById(R.id.theme_color_text);
        transBarSwitch = activity.findViewById(R.id.switch_trans_bar);
        defaultRadio = activity.findViewById(R.id.select_bg_default);
        classicRadio = activity.findViewById(R.id.select_bg_classic);
        customRadio = activity.findViewById(R.id.select_bg_custom);
        onlineRadio = activity.findViewById(R.id.select_bg_online);
        editBgPath = activity.findViewById(R.id.edit_bg_path);
        editBgUrl = activity.findViewById(R.id.edit_bg_url);
        selectBgPath = activity.findViewById(R.id.select_bg_path);

        selectTheme.setOnClickListener(this);
        transBarSwitch.setOnCheckedChangeListener(this);
        defaultRadio.setOnCheckedChangeListener(this);
        classicRadio.setOnCheckedChangeListener(this);
        customRadio.setOnCheckedChangeListener(this);
        onlineRadio.setOnCheckedChangeListener(this);
        editBgPath.addTextChangedListener(this);
        editBgUrl.addTextChangedListener(this);
        selectBgPath.setOnClickListener(this);

        transBarSwitch.setChecked(activity.launcherSetting.transBar);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(exteriorSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startExteriorSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
        colorView.setBackgroundColor(Color.parseColor(getThemeColor(activity.launcherSetting.launcherTheme)));
        colorText.setText(getThemeColor(activity.launcherSetting.launcherTheme));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(exteriorSettingUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startExteriorSettingUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    private String getThemeColor(String color){
        if (color.equals("DEFAULT")){
            return "#" + Integer.toHexString(context.getColor(R.color.colorAccent));
        }else {
            return color;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == selectTheme){
            ColorSelectorDialog dialog = new ColorSelectorDialog(context,true,Color.parseColor(getThemeColor(activity.launcherSetting.launcherTheme)));
            dialog.setColorSelectorDialogListener(new ColorSelectorDialog.ColorSelectorDialogListener() {
                @Override
                public void onColorSelected(int color) {

                }

                @Override
                public void onPositive(int destColor) {

                }

                @Override
                public void onNegative(int initColor) {

                }
            });
            dialog.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == transBarSwitch){
            activity.launcherSetting.transBar = isChecked;
            GsonUtils.saveLauncherSetting(activity.launcherSetting,AppManifest.SETTING_DIR + "/launcher_setting.json");
            if (isChecked){
                activity.appBar.setBackgroundColor(context.getResources().getColor(R.color.launcher_ui_background));
            }
            else {
                activity.appBar.setBackgroundColor(Color.parseColor(getThemeColor(activity.launcherSetting.launcherTheme)));
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
