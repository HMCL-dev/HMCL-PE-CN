package com.tungsten.hmclpe.launcher.download.forge;

import com.tungsten.hmclpe.launcher.game.Artifact;

public final class ForgeInstall {

    private final String profileName;
    private final String target;
    private final Artifact path;
    private final String version;
    private final String filePath;
    private final String welcome;
    private final String minecraft;
    private final String mirrorList;
    private final String logo;

    public ForgeInstall() {
        this(null, null, null, null, null, null, null, null, null);
    }

    public ForgeInstall(String profileName, String target, Artifact path, String version, String filePath, String welcome, String minecraft, String mirrorList, String logo) {
        this.profileName = profileName;
        this.target = target;
        this.path = path;
        this.version = version;
        this.filePath = filePath;
        this.welcome = welcome;
        this.minecraft = minecraft;
        this.mirrorList = mirrorList;
        this.logo = logo;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getTarget() {
        return target;
    }

    public Artifact getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getWelcome() {
        return welcome;
    }

    public String getMinecraft() {
        return minecraft;
    }

    public String getMirrorList() {
        return mirrorList;
    }

    public String getLogo() {
        return logo;
    }

}