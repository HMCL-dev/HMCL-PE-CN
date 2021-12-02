package com.tungsten.hmclpe.launcher.launch;

import com.tungsten.hmclpe.auth.Account;

public class GameLaunchSetting {

    public Account account;
    public String home;
    public String currentVersion;

    public String runtimePath;
    public String extraJavaFlags;
    public String extraMinecraftFlags;
    public String game_directory;
    public String renderer;
    public int width;
    public int height;

    public GameLaunchSetting(Account account,String home,String currentVersion,String runtimePath,String extraJavaFlags,String extraMinecraftFlags,String game_directory,String renderer,int width,int height){
        this.account = account;
        this.home = home;
        this.currentVersion = currentVersion;

        this.runtimePath = runtimePath;
        this.extraJavaFlags = extraJavaFlags;
        this.extraMinecraftFlags = extraMinecraftFlags;
        this.game_directory = game_directory;
        this.renderer = renderer;
        this.width = width;
        this.height = height;
    }

}
