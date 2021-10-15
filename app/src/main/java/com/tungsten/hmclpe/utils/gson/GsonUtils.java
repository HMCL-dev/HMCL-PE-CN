package com.tungsten.hmclpe.utils.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tungsten.hmclpe.launcher.list.info.contents.ContentListBean;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.utils.file.FileStringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GsonUtils {

    public static LauncherSetting getLauncherSettingFromFile(String path){
        String string = FileStringUtils.getStringFromFile(path);
        Gson gson = new Gson();
        LauncherSetting launcherSetting = gson.fromJson(string,LauncherSetting.class);
        return launcherSetting;
    }

    public static PrivateGameSetting getPrivateGameSettingFromFile(String path){
        String string = FileStringUtils.getStringFromFile(path);
        Gson gson = new Gson();
        PrivateGameSetting privateGameSetting = gson.fromJson(string,PrivateGameSetting.class);
        return privateGameSetting;
    }

    public static PublicGameSetting getPublicGameSettingFromFile(String path){
        String string = FileStringUtils.getStringFromFile(path);
        Gson gson = new Gson();
        PublicGameSetting publicGameSetting = gson.fromJson(string,PublicGameSetting.class);
        return publicGameSetting;
    }

    public static ArrayList<ContentListBean> getContentListFromFile(String path){
        String string = FileStringUtils.getStringFromFile(path);
        Gson gson = new Gson();
        Type contentListType =new TypeToken<ArrayList<ContentListBean>>(){}.getType();
        ArrayList<ContentListBean> list = gson.fromJson(string,contentListType);
        return list;
    }

    public static void saveLauncherSetting(LauncherSetting launcherSetting,String path){
        Gson gson = new Gson();
        String string = gson.toJson(launcherSetting);
        FileStringUtils.writeFile(path,string);
    }

    public static void savePrivateGameSetting(PrivateGameSetting privateGameSetting,String path){
        Gson gson = new Gson();
        String string = gson.toJson(privateGameSetting);
        FileStringUtils.writeFile(path,string);
    }

    public static void savePublicGameSetting(PublicGameSetting publicGameSetting,String path){
        Gson gson = new Gson();
        String string = gson.toJson(publicGameSetting);
        FileStringUtils.writeFile(path,string);
    }

    public static void saveContents(ArrayList<ContentListBean> list,String path){
        Gson gson = new Gson();
        String string = gson.toJson(list);
        FileStringUtils.writeFile(path,string);
    }
}
