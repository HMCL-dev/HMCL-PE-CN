package com.tungsten.hmclpe.launcher.uis.game.download.right.resource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.string.ModTranslations;

public class BaseDownloadUI extends BaseUI {

    public ModListBean.Mod bean;
    public ModTranslations.Mod modTranslation;
    public boolean isMod;
    public boolean isFirst = true;

    public LinearLayout baseDownloadUI;

    public BaseDownloadUI(Context context, MainActivity activity, ModListBean.Mod bean,boolean isMod) {
        super(context, activity);
        this.bean = bean;
        this.modTranslation = ModTranslations.getModBySlug(bean.getSlug());
        this.isMod = isMod;
        onCreate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseDownloadUI = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.ui_download_resource,null);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.uiContainer.addView(baseDownloadUI);
        ViewGroup.LayoutParams layoutParams = baseDownloadUI.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        baseDownloadUI.setLayoutParams(layoutParams);
        activity.showBarTitle(bean.getTitle(),activity.uiManager.uis.get(activity.uiManager.uis.size() - 2) != activity.uiManager.mainUI,false);
        CustomAnimationUtils.showViewFromLeft(baseDownloadUI,activity,context,true);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(baseDownloadUI,activity,context,true);
        activity.uiContainer.removeView(baseDownloadUI);
    }

    public <T> T findViewById(int id) {
        return (T) baseDownloadUI.findViewById(id);
    }

}
