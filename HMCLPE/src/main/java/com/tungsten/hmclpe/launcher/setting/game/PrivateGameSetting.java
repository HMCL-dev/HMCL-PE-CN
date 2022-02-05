package com.tungsten.hmclpe.launcher.setting.game;

import com.tungsten.hmclpe.launcher.setting.game.child.BoatLauncherSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.GameDirSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.JavaSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.PojavLauncherSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.RamSetting;

public class PrivateGameSetting {

    public boolean enable;
    public boolean notCheckJvm;
    public boolean notCheckMinecraft;
    public boolean notCheckForge;
    public JavaSetting javaSetting;
    public String extraJavaFlags;
    public String extraMinecraftFlags;
    public GameDirSetting gameDirSetting;
    public BoatLauncherSetting boatLauncherSetting;
    public PojavLauncherSetting pojavLauncherSetting;
    public RamSetting ramSetting;
    public float scaleFactor;

    public PrivateGameSetting (boolean enable,boolean notCheckJvm,boolean notCheckMinecraft,boolean notCheckForge,JavaSetting javaSetting,String extraJavaFlags,String extraMinecraftFlags,GameDirSetting gameDirSetting,BoatLauncherSetting boatLauncherSetting,PojavLauncherSetting pojavLauncherSetting,RamSetting ramSetting,float scaleFactor){
        this.enable = enable;
        this.notCheckJvm = notCheckJvm;
        this.notCheckMinecraft = notCheckMinecraft;
        this.notCheckForge = notCheckForge;
        this.javaSetting = javaSetting;
        this.extraJavaFlags = extraJavaFlags;
        this.extraMinecraftFlags = extraMinecraftFlags;
        this.gameDirSetting = gameDirSetting;
        this.boatLauncherSetting = boatLauncherSetting;
        this.pojavLauncherSetting = pojavLauncherSetting;
        this.ramSetting = ramSetting;
        this.scaleFactor = scaleFactor;
    }

}
