package com.tungsten.hmclpe.launcher.list.install;

public class DownloadTaskListBean {

    public String name;
    public String url;
    public String path;
    public int progress = 0;

    public DownloadTaskListBean(String name,String url,String path){
        this.name = name;
        this.url = url;
        this.path = path;
    }
}
