package com.tungsten.hmclpe.launcher.launch;

import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

public class GameLaunchSetting {

    public Account account;
    public String home;
    public String currentVersion;

    public String javaPath;
    public String extraJavaFlags;
    public String extraMinecraftFlags;
    public String game_directory;
    public String renderer;
    public int width;
    public int height;

    public String gameFileDirectory;

    public GameLaunchSetting(Account account,String home,String currentVersion,String javaPath,String extraJavaFlags,String extraMinecraftFlags,String game_directory,String renderer,int width,int height,String gameFileDirectory){
        this.account = account;
        this.home = home;
        this.currentVersion = currentVersion;

        this.javaPath = javaPath;
        this.extraJavaFlags = extraJavaFlags;
        this.extraMinecraftFlags = extraMinecraftFlags;
        this.game_directory = game_directory;
        this.renderer = renderer;
        this.width = width;
        this.height = height;
    }

    public static GameLaunchSetting getGameLaunchSetting(String privatePath){
        LauncherSetting launcherSetting = GsonUtils.getLauncherSettingFromFile(AppManifest.SETTING_DIR + "/launcher_setting.json");
        PublicGameSetting publicGameSetting = GsonUtils.getPublicGameSettingFromFile(AppManifest.SETTING_DIR + "/public_game_setting.json");
        PrivateGameSetting privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(privatePath);

        GameLaunchSetting gameLaunchSetting = new GameLaunchSetting(publicGameSetting.account,
                publicGameSetting.home,
                publicGameSetting.currentVersion,
                privateGameSetting.javaPath,
                privateGameSetting.extraJavaFlags,
                privateGameSetting.extraMinecraftFlags,
                privateGameSetting.game_directory,
                privateGameSetting.boatLauncherSetting.renderer,
                privateGameSetting.width,privateGameSetting.height,
                launcherSetting.gameFileDirectory);
        return gameLaunchSetting;
    }

}
