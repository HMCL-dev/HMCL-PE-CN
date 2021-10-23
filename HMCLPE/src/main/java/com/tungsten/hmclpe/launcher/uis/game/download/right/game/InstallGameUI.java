package com.tungsten.hmclpe.launcher.uis.game.download.right.game;

import android.content.Context;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class InstallGameUI extends BaseUI {

    public LinearLayout installGameUI;

    public VersionManifest.Versions version;

    public InstallGameUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        installGameUI = activity.findViewById(R.id.ui_install_game);
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

}
