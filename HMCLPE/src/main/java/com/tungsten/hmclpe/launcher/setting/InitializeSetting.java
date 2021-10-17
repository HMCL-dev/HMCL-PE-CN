package com.tungsten.hmclpe.launcher.setting;

import android.content.Context;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.info.contents.ContentListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.io.File;
import java.util.ArrayList;

public class InitializeSetting {

    public static ArrayList<ContentListBean> initializeContents(Context context){
        ArrayList<ContentListBean> contentList = new ArrayList<>();
        if (new File(AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json").exists() && GsonUtils.getContentListFromFile(AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json").size() != 0){
            contentList = GsonUtils.getContentListFromFile(AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json");
        }
        else {
            contentList.add(new ContentListBean(context.getString(R.string.default_game_file_directory_list_pri),AppManifest.DEFAULT_GAME_DIR,true));
            contentList.add(new ContentListBean(context.getString(R.string.default_game_file_directory_list_sec),AppManifest.INNER_GAME_DIR,false));
            GsonUtils.saveContents(contentList,AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json");
        }
        return contentList;
    }

    public static LauncherSetting initializeLauncherSetting(){
        LauncherSetting launcherSetting;
        if (new File(AppManifest.SETTING_DIR + "/launcher_setting.json").exists()){
            launcherSetting = GsonUtils.getLauncherSettingFromFile(AppManifest.SETTING_DIR + "/launcher_setting.json");
        }
        else {
            launcherSetting = new LauncherSetting(AppManifest.DEFAULT_GAME_DIR,0,0,true,false,"DEFAULT","DEFAULT");
            GsonUtils.saveLauncherSetting(launcherSetting,AppManifest.SETTING_DIR + "/launcher_setting.json");
        }
        return launcherSetting;
    }

    public static PublicGameSetting initializePublicGameSetting(Context context, MainActivity activity){
        PublicGameSetting publicGameSetting;
        if (new File(AppManifest.SETTING_DIR + "/public_game_setting.json").exists()){
            publicGameSetting = GsonUtils.getPublicGameSettingFromFile(AppManifest.SETTING_DIR + "/public_game_setting.json");
        }
        else {
            String currentVersion;
            if (SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory).size() != 0){
                currentVersion = SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory).get(0).name;
            }
            else {
                currentVersion = "";
            }
            Account account = new Account(0,"","","","","","","","");
            publicGameSetting = new PublicGameSetting(account,AppManifest.LAUNCHER_DIR,currentVersion);
            GsonUtils.savePublicGameSetting(publicGameSetting,AppManifest.SETTING_DIR + "/public_game_setting.json");
        }
        return publicGameSetting;
    }

    public static PrivateGameSetting initializePrivateGameSetting(Context context){
        PrivateGameSetting privateGameSetting;
        if (new File(AppManifest.SETTING_DIR + "/private_game_setting.json").exists()){
            privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        else {
            privateGameSetting = new PrivateGameSetting(false,false,false,false,AppManifest.DEFAULT_RUNTIME_DIR,"-client -Xms4096M -Xmx4096M","",AppManifest.DEFAULT_GAME_DIR,Integer.toString(context.getResources().getDisplayMetrics().widthPixels),Integer.toString(context.getResources().getDisplayMetrics().heightPixels),"");
            GsonUtils.savePrivateGameSetting(privateGameSetting,AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        return privateGameSetting;
    }
}
