package com.tungsten.hmclpe.launcher.download.minecraft.game;

public class VersionManifest {

    public LatestVersion latest;
    public Versions[] versions;

    public VersionManifest (LatestVersion latest,Versions[] versions){
        this.latest = latest;
        this.versions = versions;
    }

    public class LatestVersion{
        public String release;
        public String snapshot;

        public LatestVersion(String release,String snapshot){
            this.release = release;
            this.snapshot = snapshot;
        }
    }

    public class Versions{
        public String id;
        public String type;
        public String url;
        public String time;
        public String releaseTime;

        public Versions(String id,String type,String url,String time,String releaseTime){
            this.id = id;
            this.type = type;
            this.url = url;
            this.time = time;
            this.releaseTime = releaseTime;
        }
    }

}
