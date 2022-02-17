package com.tungsten.hmclpe.launcher.setting.game;

import com.tungsten.hmclpe.launcher.setting.game.child.BoatLauncherSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.GameDirSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.JavaSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.PojavLauncherSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.RamSetting;

public class PrivateGameSetting {

    public boolean enable;
    public boolean log;
    public boolean notCheckJvm;
    public boolean notCheckMinecraft;
    public boolean notCheckForge;
    public JavaSetting javaSetting;
    public String extraJavaFlags;
    public String extraMinecraftFlags;
    public String server;
    public GameDirSetting gameDirSetting;
    public BoatLauncherSetting boatLauncherSetting;
    public PojavLauncherSetting pojavLauncherSetting;
    public RamSetting ramSetting;
    public int controlType;
    public String controlLayout;
    public float scaleFactor;

    public PrivateGameSetting (boolean enable,boolean log,boolean notCheckJvm,boolean notCheckMinecraft,boolean notCheckForge,JavaSetting javaSetting,String extraJavaFlags,String extraMinecraftFlags,String server,GameDirSetting gameDirSetting,BoatLauncherSetting boatLauncherSetting,PojavLauncherSetting pojavLauncherSetting,RamSetting ramSetting,int controlType,String controlLayout,float scaleFactor){
        this.enable = enable;
        this.log = log;
        this.notCheckJvm = notCheckJvm;
        this.notCheckMinecraft = notCheckMinecraft;
        this.notCheckForge = notCheckForge;
        this.javaSetting = javaSetting;
        this.extraJavaFlags = extraJavaFlags;
        this.extraMinecraftFlags = extraMinecraftFlags;
        this.server = server;
        this.gameDirSetting = gameDirSetting;
        this.boatLauncherSetting = boatLauncherSetting;
        this.pojavLauncherSetting = pojavLauncherSetting;
        this.ramSetting = ramSetting;
        this.controlType = controlType;
        this.controlLayout = controlLayout;
        this.scaleFactor = scaleFactor;
    }

}
