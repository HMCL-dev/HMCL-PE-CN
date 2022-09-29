package com.tungsten.hmclpe.launcher.list.info.docs;

public class Doc {

    private final String title;
    private final String subtitle;
    private final String author;
    private final String path;

    public Doc() {
        this("", "", "", "");
    }

    public Doc(String title, String subtitle, String author, String path) {
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getPath() {
        return path;
    }
}
