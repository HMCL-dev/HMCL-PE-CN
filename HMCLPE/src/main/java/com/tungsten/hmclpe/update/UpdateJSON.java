package com.tungsten.hmclpe.update;

public class UpdateJSON {

    public LauncherVersion latestRelease;
    public LauncherVersion latestPrerelease;
    public LauncherVersion latestJava17;

    public UpdateJSON (LauncherVersion latestRelease,LauncherVersion latestPrerelease,LauncherVersion latestJava17) {
        this.latestRelease = latestRelease;
        this.latestPrerelease = latestPrerelease;
        this.latestJava17 = latestJava17;
    }

}
