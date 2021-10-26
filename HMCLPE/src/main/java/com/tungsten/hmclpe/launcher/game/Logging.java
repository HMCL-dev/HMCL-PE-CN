package com.tungsten.hmclpe.launcher.game;

import com.google.gson.annotations.SerializedName;

public class Logging {

    @SerializedName("file")
    public IdDownloadInfo file;
    @SerializedName("argument")
    public String argument;
    @SerializedName("type")
    public String type;

    public Logging() {
        this(new IdDownloadInfo());
    }

    public Logging(IdDownloadInfo file) {
        this(file, "");
    }

    public Logging(IdDownloadInfo file, String argument) {
        this(file, argument, "");
    }

    public Logging(IdDownloadInfo file, String argument, String type) {
        this.file = file;
        this.argument = argument;
        this.type = type;
    }

}
