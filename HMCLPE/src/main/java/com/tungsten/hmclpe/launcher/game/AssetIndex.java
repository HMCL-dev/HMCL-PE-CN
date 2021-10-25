package com.tungsten.hmclpe.launcher.game;

public class AssetIndex {
    public int totalSize;
    public String id;
    public String url;
    public String sha1;
    public int size;

    public AssetIndex(int totalSize,String id,String url,String sha1,int size){
        this.totalSize = totalSize;
        this.id = id;
        this.url = url;
        this.sha1 = sha1;
        this.size = size;
    }
}
