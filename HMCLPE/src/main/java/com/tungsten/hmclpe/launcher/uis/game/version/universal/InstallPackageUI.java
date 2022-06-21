package com.tungsten.hmclpe.launcher.uis.game.version.universal;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class InstallPackageUI extends BaseUI implements View.OnClickListener {

    public LinearLayout installPackageUI;

    private LinearLayout installLocal;
    private LinearLayout installOnline;

    public InstallPackageUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        installPackageUI = activity.findViewById(R.id.ui_install_package);

        installLocal = activity.findViewById(R.id.install_package_local);
        installOnline = activity.findViewById(R.id.install_package_online);
        installLocal.setOnClickListener(this);
        installOnline.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.install_package_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(installPackageUI,activity,context,true);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(installPackageUI,activity,context,true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view == installLocal) {

        }
        if (view == installOnline) {

        }
    }
}
