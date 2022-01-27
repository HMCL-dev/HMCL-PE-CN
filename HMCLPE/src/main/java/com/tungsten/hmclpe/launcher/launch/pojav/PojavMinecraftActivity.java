package com.tungsten.hmclpe.launcher.launch.pojav;

import android.os.Bundle;

import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;

import net.kdt.pojavlaunch.BaseMainActivity;
import net.kdt.pojavlaunch.utils.MCOptionUtils;

import org.lwjgl.glfw.CallbackBridge;

public class PojavMinecraftActivity extends BaseMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameLaunchSetting gameLaunchSetting = GameLaunchSetting.getGameLaunchSetting(getIntent().getExtras().getString("setting_path"));

        CallbackBridge.windowWidth = gameLaunchSetting.width;
        CallbackBridge.windowHeight = gameLaunchSetting.height;

        MCOptionUtils.load(gameLaunchSetting.currentVersion);
        MCOptionUtils.set("overrideWidth", String.valueOf(CallbackBridge.windowWidth));
        MCOptionUtils.set("overrideHeight", String.valueOf(CallbackBridge.windowHeight));
        MCOptionUtils.save(gameLaunchSetting.currentVersion);

        initLayout(gameLaunchSetting.javaPath,
                gameLaunchSetting.home,
                PojavLauncher.isHighVersion(gameLaunchSetting),
                PojavLauncher.getMcArgs(gameLaunchSetting, PojavMinecraftActivity.this),
                gameLaunchSetting.pojavRenderer);
    }
}
