package com.tungsten.hmclpe.launcher.setting.launcher;

public class LauncherSetting {

    public String gameFileDirectory;
    public int downloadUrlSource;
    public int language;
    public int maxDownloadTask;
    public boolean autoCheckUpdate;
    public boolean getBetaVersion;
    public String launcherTheme;
    public String launcherBackground;

    public LauncherSetting(String gameFileDirectory,int downloadUrlSource,int language,int maxDownloadTask,boolean autoCheckUpdate,boolean getBetaVersion,String launcherTheme,String launcherBackground){
        this.gameFileDirectory = gameFileDirectory;
        this.downloadUrlSource = downloadUrlSource;
        this.language = language;
        this.maxDownloadTask = maxDownloadTask;
        this.autoCheckUpdate = autoCheckUpdate;
        this.getBetaVersion = getBetaVersion;
        this.launcherTheme = launcherTheme;
        this.launcherBackground = launcherBackground;
    }
}
