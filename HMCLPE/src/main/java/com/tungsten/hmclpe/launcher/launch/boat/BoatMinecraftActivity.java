package com.tungsten.hmclpe.launcher.launch.boat;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;

import cosine.boat.BoatActivity;

public class BoatMinecraftActivity extends BoatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout(R.layout.activity_minecraft);

        GameLaunchSetting gameLaunchSetting = GameLaunchSetting.getGameLaunchSetting(getIntent().getExtras().getString("setting_path"));

        this.setOnCursorChangeListener(new OnActivityChangeListener() {
            @Override
            public void onSurfaceTextureAvailable() {
                startGame(gameLaunchSetting.javaPath,
                        gameLaunchSetting.home,
                        BoatLauncher.isHighVersion(gameLaunchSetting),
                        BoatLauncher.getMcArgs(gameLaunchSetting,BoatMinecraftActivity.this));
            }

            @Override
            public void onCursorModeChange(int mode) {
                
            }
        });
    }
}
