package com.tungsten.hmclpe.task;

import java.io.IOException;
import java.net.URL;

import static java.util.Objects.requireNonNull;

public class DownloadException extends IOException {

    private final URL url;

    public DownloadException(URL url, Throwable cause) {
        super("Unable to download " + url + ", " + cause.getMessage(), requireNonNull(cause));

        this.url = url;
    }

    public URL getUrl() {
        return url;
    }
}