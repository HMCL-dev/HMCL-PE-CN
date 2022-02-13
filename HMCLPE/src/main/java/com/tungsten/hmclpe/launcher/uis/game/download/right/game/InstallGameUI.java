package com.tungsten.hmclpe.launcher.uis.game.download.right.game;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.install.DownloadDialog;
import com.tungsten.hmclpe.launcher.download.minecraft.forge.ForgeVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class InstallGameUI extends BaseUI implements View.OnClickListener {

    public LinearLayout installGameUI;

    public VersionManifest.Version version;
    public ForgeVersion forgeVersion = new ForgeVersion();

    private EditText editName;

    private TextView gameVersionText;
    private TextView forgeVersionText;
    private TextView liteLoaderVersionText;
    private TextView optiFineVersionText;
    private TextView fabricVersionText;

    private ImageButton deleteForgeVersion;
    private ImageButton deleteLiteLoaderVersion;
    private ImageButton deleteOptiFineVersion;
    private ImageButton deleteFabricVersion;

    private LinearLayout selectForgeVersion;
    private LinearLayout selectLiteLoaderVersion;
    private LinearLayout selectOptiFineVersion;
    private LinearLayout selectFabricVersion;

    private Button install;

    public InstallGameUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        installGameUI = activity.findViewById(R.id.ui_install_game);

        editName = activity.findViewById(R.id.edit_game_name);

        gameVersionText = activity.findViewById(R.id.minecraft_version_text);
        forgeVersionText = activity.findViewById(R.id.forge_version_text);
        liteLoaderVersionText = activity.findViewById(R.id.liteloader_version_text);
        optiFineVersionText = activity.findViewById(R.id.optifine_version_text);
        fabricVersionText = activity.findViewById(R.id.fabric_version_text);

        deleteForgeVersion = activity.findViewById(R.id.call_off_install_forge);
        deleteLiteLoaderVersion = activity.findViewById(R.id.call_off_install_liteloader);
        deleteOptiFineVersion = activity.findViewById(R.id.call_off_install_optifine);
        deleteFabricVersion = activity.findViewById(R.id.call_off_install_fabric);
        deleteForgeVersion.setOnClickListener(this);
        deleteLiteLoaderVersion.setOnClickListener(this);
        deleteOptiFineVersion.setOnClickListener(this);
        deleteOptiFineVersion.setOnClickListener(this);

        selectForgeVersion = activity.findViewById(R.id.select_forge_version);
        selectLiteLoaderVersion = activity.findViewById(R.id.select_liteloader_version);
        selectOptiFineVersion = activity.findViewById(R.id.select_optifine_version);
        selectFabricVersion = activity.findViewById(R.id.select_fabric_version);
        selectForgeVersion.setOnClickListener(this);
        selectLiteLoaderVersion.setOnClickListener(this);
        selectOptiFineVersion.setOnClickListener(this);
        selectFabricVersion.setOnClickListener(this);

        install = activity.findViewById(R.id.install_game);
        install.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.install_game_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(installGameUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(installGameUI,activity,context,true);
    }

    @Override
    public void onClick(View v) {
        if (v == deleteForgeVersion){
            if (!forgeVersion.getGameVersion().equals("")){
                forgeVersion = new ForgeVersion();
                init();
            }
        }
        if (v == deleteLiteLoaderVersion){

        }
        if (v == deleteOptiFineVersion){

        }
        if (v == deleteFabricVersion){

        }
        if (v == selectForgeVersion){
            activity.uiManager.downloadForgeUI.version = version.id;
            activity.uiManager.switchMainUI(activity.uiManager.downloadForgeUI);
        }
        if (v == selectLiteLoaderVersion){

        }
        if (v == selectOptiFineVersion){
            activity.uiManager.downloadOptifineUI.version = version.id;
            activity.uiManager.switchMainUI(activity.uiManager.downloadOptifineUI);
        }
        if (v == selectFabricVersion){

        }
        if (v == install){
            boolean exist = false;
            for (int i = 0;i < SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory,activity.publicGameSetting.currentVersion).size();i++){
                if (editName.getText().toString().equals(SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory,activity.publicGameSetting.currentVersion).get(i).name)){
                    exist = true;
                }
            }
            if (exist){
                Toast.makeText(context,context.getString(R.string.install_game_ui_exist),Toast.LENGTH_SHORT).show();
            }
            else {
                DownloadDialog downloadDialog = new DownloadDialog(context,activity,editName.getText().toString(),version,forgeVersion);
                downloadDialog.show();
            }
        }
    }

    private void init(){
        editName.setText(version.id);
        gameVersionText.setText(version.id);
        if (!forgeVersion.getGameVersion().equals("")){
            forgeVersionText.setText(forgeVersion.getVersion());
            fabricVersionText.setText(context.getString(R.string.install_game_ui_forge_not_compatible));
            deleteForgeVersion.setVisibility(View.VISIBLE);
        }
        else {
            forgeVersionText.setText(context.getString(R.string.install_game_ui_none));
            fabricVersionText.setText(context.getString(R.string.install_game_ui_none));
            deleteForgeVersion.setVisibility(View.GONE);
        }
    }
}
