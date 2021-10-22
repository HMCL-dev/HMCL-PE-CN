package com.tungsten.hmclpe.launcher.uis.game.download;

public class DownloadUrlSource {

    public static String DOWNLOAD_URL_SOURCE_OFFICIAL = "official";
    public static String DOWNLOAD_URL_SOURCE_BMCLAPI = "bmclapi";
    public static String DOWNLOAD_URL_SOURCE_MCBBS = "mcbbs";

    public static int VERSION_MANIFEST = 0;
    public static int VERSION_JSON = 1;
    public static int VERSION_JAR = 2;
    public static int ASSETS_INDEX_JSON = 3;
    public static int ASSETS_OBJ = 4;
    public static int LIBRARIES = 5;

    public static String[] OFFICIAL_URLS = {
            "https://launchermeta.mojang.com/mc/game/version_manifest.json",
            "https://launchermeta.mojang.com",
            "https://launcher.mojang.com",
            "https://launchermeta.mojang.com",
            "http://resources.download.minecraft.net",
            "https://libraries.minecraft.net"
    };

    public static String[] BMCLAPI_URLS = {
            "https://bmclapi2.bangbang93.com/mc/game/version_manifest.json",
            "https://bmclapi2.bangbang93.com",
            "https://bmclapi2.bangbang93.com",
            "https://bmclapi2.bangbang93.com",
            "https://bmclapi2.bangbang93.com/assets",
            "https://bmclapi2.bangbang93.com/maven"
    };

    public static String[] MCBBS_URLS = {
            "https://download.mcbbs.net/mc/game/version_manifest.json",
            "https://download.mcbbs.net",
            "https://download.mcbbs.net",
            "https://download.mcbbs.net",
            "https://download.mcbbs.net/assets",
            "https://download.mcbbs.net/maven"
    };

    public static String replaceUrlTitle(String url,String source , int type){
        StringBuilder stringBuilder = new StringBuilder(url);
        return stringBuilder.replace(0,getUrlTitle(DOWNLOAD_URL_SOURCE_OFFICIAL,type).length(),getUrlTitle(source,type)).toString();
    }

    public static String getUrlTitle(String source , int type){
        if (source.equals(DOWNLOAD_URL_SOURCE_OFFICIAL)){
            return OFFICIAL_URLS[type];
        }
        else if (source.equals(DOWNLOAD_URL_SOURCE_BMCLAPI)){
            return BMCLAPI_URLS[type];
        }
        else {
            return MCBBS_URLS[type];
        }
    }

}
