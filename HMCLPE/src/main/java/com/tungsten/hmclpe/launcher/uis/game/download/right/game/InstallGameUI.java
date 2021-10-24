package com.tungsten.hmclpe.launcher.uis.game.download.right.game;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class InstallGameUI extends BaseUI implements View.OnClickListener {

    public LinearLayout installGameUI;

    public VersionManifest.Versions version;

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

        selectForgeVersion = activity.findViewById(R.id.select_forge_version);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.install_game_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(installGameUI,activity,context,true);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(installGameUI,activity,context,true);
    }

    @Override
    public void onClick(View v) {

    }
}
