package com.tungsten.hmclpe.launcher.launch.pojav;

import android.os.Bundle;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;

import net.kdt.pojavlaunch.Account;
import net.kdt.pojavlaunch.BaseMainActivity;
import net.kdt.pojavlaunch.GameSetting;

public class PojavMinecraftActivity extends BaseMainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Account account = new Account(0,"","","mojang","0","Steve","00000000-0000-0000-0000-000000000000","0","0","");
        GameSetting gameSetting = new GameSetting(account,
                AppManifest.DEFAULT_RUNTIME_DIR + "/Internal",
                AppManifest.DEFAULT_GAME_DIR + "/versions/1.12.2",
                AppManifest.DEFAULT_GAME_DIR,
                AppManifest.DEFAULT_GAME_DIR,
                "opengles2",
                AppManifest.DEFAULT_CACHE_DIR,
                2048,
                2250,
                1038);
        initActivity(R.layout.activity_minecraft,gameSetting);
    }
}
