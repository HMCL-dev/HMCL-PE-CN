package com.tungsten.hmclpe.launcher.uis.game.manager.universal;

import static com.tungsten.hmclpe.launcher.uis.universal.setting.right.launcher.ExteriorSettingUI.getThemeColor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.game.World;
import com.tungsten.hmclpe.launcher.list.local.save.DatapackListAdapter;
import com.tungsten.hmclpe.launcher.mod.Datapack;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class PackMcManagerUI extends BaseUI implements View.OnClickListener {

    public LinearLayout packMcManagerUI;

    public World world;

    private LinearLayout toolbar;

    private LinearLayout refresh;
    private LinearLayout add;
    private LinearLayout delete;
    private LinearLayout enable;
    private LinearLayout disable;

    private ProgressBar progressBar;
    private ListView listView;
    private Datapack datapack;
    public Datapack.Pack pack;

    public PackMcManagerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        packMcManagerUI = activity.findViewById(R.id.ui_manage_datapack);

        toolbar = activity.findViewById(R.id.datapack_toolbar);

        refresh = activity.findViewById(R.id.refresh_datapack_list);
        add = activity.findViewById(R.id.add_datapack);
        delete = activity.findViewById(R.id.delete_datapack);
        enable = activity.findViewById(R.id.enable_datapack);
        disable = activity.findViewById(R.id.disable_datapack);
        refresh.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);

        progressBar = activity.findViewById(R.id.load_datapacks_progress);
        listView = activity.findViewById(R.id.datapack_list);
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
        if (view == refresh) {
            refresh();
        }
        if (view == add) {

        }
        if (view == delete) {

        }
        if (view == enable) {

        }
        if (view == disable) {

        }
    }

    private void init() {
        int themeColor = Color.parseColor(getThemeColor(context,activity.launcherSetting.launcherTheme));
        float[] hsv = new float[3];
        Color.colorToHSV(themeColor, hsv);
        hsv[1] -= (1 - hsv[1]) * 0.3f;
        hsv[2] += (1 - hsv[2]) * 0.3f;
        toolbar.setBackgroundColor(Color.HSVToColor(hsv));
        refresh();
    }

    private void refresh() {
        datapack = new Datapack(world.getFile().resolve("datapacks"));
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            datapack.loadFromDir();
            activity.runOnUiThread(() -> {
                DatapackListAdapter adapter = new DatapackListAdapter(context,datapack.getInfo(),PackMcManagerUI.this);
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }
}
