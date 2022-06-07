package com.tungsten.hmclpe.launcher.uis.game.manager.universal;

import static com.tungsten.hmclpe.launcher.uis.universal.setting.right.launcher.ExteriorSettingUI.getThemeColor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.game.World;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class PackMcManagerUI extends BaseUI implements View.OnClickListener {

    public LinearLayout packMcManagerUI;

    public World world;

    private LinearLayout toolbar;

    public PackMcManagerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        packMcManagerUI = activity.findViewById(R.id.ui_manage_datapack);

        toolbar = activity.findViewById(R.id.datapack_toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.manage_datapack_ui_title).replace("%w",world.getWorldName()),activity.uiManager.uis.get(activity.uiManager.uis.size() - 2) != activity.uiManager.mainUI,false);
        CustomAnimationUtils.showViewFromLeft(packMcManagerUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(packMcManagerUI,activity,context,true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {

    }

    private void init() {
        int themeColor = Color.parseColor(getThemeColor(context,activity.launcherSetting.launcherTheme));
        float[] hsv = new float[3];
        Color.colorToHSV(themeColor, hsv);
        hsv[1] -= (1 - hsv[1]) * 0.3f;
        hsv[2] += (1 - hsv[2]) * 0.3f;
        toolbar.setBackgroundColor(Color.HSVToColor(hsv));
    }
}
