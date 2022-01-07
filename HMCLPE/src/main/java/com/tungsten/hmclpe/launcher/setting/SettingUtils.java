package com.tungsten.hmclpe.launcher.setting;

import com.tungsten.hmclpe.launcher.list.local.game.GameListBean;
import com.tungsten.hmclpe.utils.file.FileStringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class SettingUtils {

    public static ArrayList<GameListBean> getLocalVersionInfo(String path,String currentVersion){
        ArrayList<GameListBean> list = new ArrayList<>();
        String[] string = new File(path + "/versions/").list();
        if (new File(path + "/versions/").exists()){
            for (String str : string){
                GameListBean bean = new GameListBean("","","",false);
                bean.name = str;
                if (new File(path + "/versions/" + str + "/icon.png").exists()){
                    bean.iconPath = path + "/versions/" + str + "/icon.png";
                }
                if (new File(path + "/versions/" + str +"/" + str + ".json").exists()){
                    String gameJsonText = FileStringUtils.getStringFromFile(path + "/versions/" + str +"/" + str + ".json");
                    try {
                        JSONObject object = new JSONObject(gameJsonText);
                        bean.version = "unknown";
                        bean.version = object.getString("id");
                        bean.version = object.getString("inheritsFrom");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    bean.version = "unknown";
                }
                bean.isSelected = currentVersion.equals(bean.name);
                list.add(bean);
            }
            return list;
        }
        else {
            return list;
        }
    }
}
