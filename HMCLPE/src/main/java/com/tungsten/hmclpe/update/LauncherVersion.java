package com.tungsten.hmclpe.update;

public class LauncherVersion {

    public int versionCode;
    public String versionName;
    public String date;
    public String url;
    public String updateLog;

    public LauncherVersion (int versionCode,String versionName,String date,String url,String updateLog) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.date = date;
        this.url = url;
        this.updateLog = updateLog;
    }

}
