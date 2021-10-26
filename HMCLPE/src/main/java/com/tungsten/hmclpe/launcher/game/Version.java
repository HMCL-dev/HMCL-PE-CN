package com.tungsten.hmclpe.launcher.game;

import com.tungsten.hmclpe.utils.gson.JsonMap;

import java.util.List;

public class Version {
    public String id;
    public String minecraftArguments;
    public Arguments arguments;
    public String mainClass;
    public String jar;
    public AssetIndex assetIndex;
    public String assets;
    public Integer complianceLevel;
    public JavaVersion javaVersion;
    public List<Library> libraries;
    public JsonMap<DownloadType, DownloadInfo> downloads;
    public JsonMap<DownloadType, Logging> logging;
    public String type;
    public String time;
    public String releaseTime;
    public Integer minimumLauncherVersion;

    public Version(String id,String minecraftArguments,Arguments arguments,String mainClass,String jar,AssetIndex assetIndex,String assets,Integer complianceLevel,JavaVersion javaVersion,List<Library> libraries,JsonMap<DownloadType, DownloadInfo> downloads,JsonMap<DownloadType, Logging> logging,String type,String time,String releaseTime,Integer minimumLauncherVersion){
        this.id = id;
        this.minecraftArguments = minecraftArguments;
        this.arguments = arguments;
        this.mainClass = mainClass;
        this.jar = jar;
        this.assetIndex = assetIndex;
        this.assets = assets;
        this.complianceLevel = complianceLevel;
        this.javaVersion = javaVersion;
        this.libraries = libraries;
        this.downloads = downloads;
        this.logging = logging;
        this.type = type;
        this.time = time;
        this.releaseTime = releaseTime;
        this.minimumLauncherVersion = minimumLauncherVersion;
    }
}
