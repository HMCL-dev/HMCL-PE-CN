package com.tungsten.hmclpe.launcher.setting;

import com.google.gson.Gson;
import com.tungsten.hmclpe.control.bean.button.ButtonStyle;
import com.tungsten.hmclpe.control.bean.rocker.RockerStyle;
import com.tungsten.hmclpe.launcher.list.local.controller.ChildLayout;
import com.tungsten.hmclpe.launcher.list.local.controller.ControlPattern;
import com.tungsten.hmclpe.launcher.list.local.game.GameListBean;
import com.tungsten.hmclpe.launcher.list.local.java.JavaListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.file.FileStringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SettingUtils {

    private static final String JAVA_VERSION_str = "JAVA_VERSION=\"";
    private static final String OS_ARCH_str = "OS_ARCH=\"";

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
        }
        return list;
    }

    public static ArrayList<JavaListBean> getJavaVersionInfo(){
        ArrayList<JavaListBean> list = new ArrayList<>();
        String javaPath = AppManifest.JAVA_DIR + "/";
        String[] string = new File(javaPath).list();
        if (new File(javaPath).exists()){
            for (String str : string){
                JavaListBean bean = new JavaListBean("","","");
                bean.name = str;
                File release = new File(javaPath + bean.name,"release");
                if (release.exists() && FileStringUtils.getStringFromFile(release.getAbsolutePath()) != null){
                    String releaseContent = FileStringUtils.getStringFromFile(release.getAbsolutePath());
                    int _JAVA_VERSION_index = releaseContent.indexOf(JAVA_VERSION_str);
                    int _OS_ARCH_index = releaseContent.indexOf(OS_ARCH_str);
                    String javaVersion = releaseContent.substring(_JAVA_VERSION_index,releaseContent.indexOf('"',_JAVA_VERSION_index));
                    String[] javaVersionSplit = javaVersion.split("\\.");
                    if (javaVersionSplit[0].equals("1")) {
                        bean.version = javaVersionSplit[1];
                    } else {
                        bean.version = javaVersionSplit[0];
                    }
                    bean.osArch = releaseContent.substring(_OS_ARCH_index,releaseContent.indexOf('"',_OS_ARCH_index));
                }
                else {
                    bean.version = "unknown version";
                    bean.osArch = "";
                }
                list.add(bean);
            }
        }
        return list;
    }

    public static ArrayList<ControlPattern> getControlPatternList(){
        ArrayList<ControlPattern> list = new ArrayList<>();
        String[] string = new File(AppManifest.CONTROLLER_DIR + "/").list();
        if (new File(AppManifest.CONTROLLER_DIR + "/").exists()){
            for (String str : string){
                String info = FileStringUtils.getStringFromFile(AppManifest.CONTROLLER_DIR + "/" + str + "/info.json");
                Gson gson = new Gson();
                ControlPattern controlPattern = gson.fromJson(info, ControlPattern.class);
                list.add(controlPattern);
            }
        }
        return list;
    }

    public static ArrayList<ChildLayout> getChildList(String pattern){
        ArrayList<ChildLayout> list = new ArrayList<>();
        String[] string = new File(AppManifest.CONTROLLER_DIR + "/" + pattern + "/").list();
        if (new File(AppManifest.CONTROLLER_DIR + "/" + pattern + "/").exists()){
            for (String str : string){
                if (!str.equals("info.json")){
                    String info = FileStringUtils.getStringFromFile(AppManifest.CONTROLLER_DIR + "/" + pattern + "/" + str);
                    Gson gson = new Gson();
                    ChildLayout childLayout = gson.fromJson(info, ChildLayout.class);
                    list.add(childLayout);
                }
            }
        }
        return list;
    }

    public static ArrayList<ButtonStyle> getButtonStyleList(){
        ArrayList<ButtonStyle> list = new ArrayList<>();
        if (new File(AppManifest.STYLE_DIR + "/button.json").exists()){
            String string = FileStringUtils.getStringFromFile(AppManifest.STYLE_DIR + "/button.json");
            Gson gson = new Gson();
            ButtonStyle[] buttonStyles = gson.fromJson(string, ButtonStyle[].class);
            list.addAll(Arrays.asList(buttonStyles));
            if (list.size() == 0){
                ButtonStyle style = new ButtonStyle();
                style.name = "Default";
                list.add(style);
                saveButtonStyle(list);
            }
        }
        else {
            ButtonStyle style = new ButtonStyle();
            style.name = "Default";
            list.add(style);
            saveButtonStyle(list);
        }
        return list;
    }

    public static void saveButtonStyle(ArrayList<ButtonStyle> list){
        Gson gson = new Gson();
        String string = gson.toJson(list);
        FileStringUtils.writeFile(AppManifest.STYLE_DIR + "/button.json",string);
    }

    public static ArrayList<RockerStyle> getRockerStyleList(){
        ArrayList<RockerStyle> list = new ArrayList<>();
        if (new File(AppManifest.STYLE_DIR + "/rocker.json").exists()){
            String string = FileStringUtils.getStringFromFile(AppManifest.STYLE_DIR + "/rocker.json");
            Gson gson = new Gson();
            RockerStyle[] rockerStyles = gson.fromJson(string, RockerStyle[].class);
            list.addAll(Arrays.asList(rockerStyles));
            if (list.size() == 0){
                RockerStyle style = new RockerStyle();
                style.name = "Default";
                list.add(style);
                saveRockerStyle(list);
            }
        }
        else {
            RockerStyle style = new RockerStyle();
            style.name = "Default";
            list.add(style);
            saveRockerStyle(list);
        }
        return list;
    }

    public static void saveRockerStyle(ArrayList<RockerStyle> list){
        Gson gson = new Gson();
        String string = gson.toJson(list);
        FileStringUtils.writeFile(AppManifest.STYLE_DIR + "/rocker.json",string);
    }

}
