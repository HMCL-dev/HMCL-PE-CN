package com.tungsten.hmclpe.launcher.setting.launcher;

public class LauncherSetting {

    public String gameFileDirectory;
    public int downloadUrlSource;
    public int language;
    public int maxDownloadTask;
    public boolean autoCheckUpdate;
    public boolean getBetaVersion;
    public boolean fullscreen;
    public boolean transBar;
    public String launcherTheme;
    public String launcherBackground;
    public String cachePath;

    public LauncherSetting(String gameFileDirectory,int downloadUrlSource,int language,int maxDownloadTask,boolean autoCheckUpdate,boolean getBetaVersion,boolean fullscreen,boolean transBar,String launcherTheme,String launcherBackground,String cachePath){
        this.gameFileDirectory = gameFileDirectory;
        this.downloadUrlSource = downloadUrlSource;
        this.language = language;
        this.maxDownloadTask = maxDownloadTask;
        this.autoCheckUpdate = autoCheckUpdate;
        this.getBetaVersion = getBetaVersion;
        this.fullscreen = fullscreen;
        this.transBar = transBar;
        this.launcherTheme = launcherTheme;
        this.launcherBackground = launcherBackground;
        this.cachePath = cachePath;
    }
}
