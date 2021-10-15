package com.tungsten.hmclpe.launcher.setting.game;

public class PrivateGameSetting {

    public boolean enable;
    public boolean notCheckJvm;
    public boolean notCheckMinecraft;
    public boolean notCheckForge;
    public String runtimePath;
    public String extraJavaFlags;
    public String extraMinecraftFlags;
    public String game_directory;
    public String width;
    public String height;
    public String serverIP;

    public PrivateGameSetting (boolean enable,boolean notCheckJvm,boolean notCheckMinecraft,boolean notCheckForge,String runtimePath,String extraJavaFlags,String extraMinecraftFlags,String game_directory,String width,String height,String serverIP){
        this.enable = enable;
        this.notCheckJvm = notCheckJvm;
        this.notCheckMinecraft = notCheckMinecraft;
        this.notCheckForge = notCheckForge;
        this.runtimePath = runtimePath;
        this.extraJavaFlags = extraJavaFlags;
        this.extraMinecraftFlags = extraMinecraftFlags;
        this.game_directory = game_directory;
        this.width = width;
        this.height = height;
        this.serverIP = serverIP;
    }

}
