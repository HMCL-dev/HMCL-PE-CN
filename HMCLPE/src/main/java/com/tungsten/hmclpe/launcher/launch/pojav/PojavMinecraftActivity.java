package com.tungsten.hmclpe.launcher.launch.pojav;

import android.os.Bundle;

import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;

import net.kdt.pojavlaunch.BaseMainActivity;

public class PojavMinecraftActivity extends BaseMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameLaunchSetting gameLaunchSetting = GameLaunchSetting.getGameLaunchSetting(getIntent().getExtras().getString("setting_path"));

        initLayout(gameLaunchSetting.javaPath,
                gameLaunchSetting.home,
                PojavLauncher.isHighVersion(gameLaunchSetting),
                PojavLauncher.getMcArgs(gameLaunchSetting, PojavMinecraftActivity.this),
                gameLaunchSetting.pojavRenderer);
    }
}
