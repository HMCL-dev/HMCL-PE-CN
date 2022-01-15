package com.tungsten.hmclpe.launcher.setting;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.local.game.GameListBean;
import com.tungsten.hmclpe.launcher.list.local.java.JavaListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.file.FileStringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

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
            return list;
        }
        else {
            return list;
        }
    }

    public static ArrayList<JavaListBean> getJavaVersionInfo(MainActivity activity){
        ArrayList<JavaListBean> list = new ArrayList<>();
        String javaPath;
        if (activity.privateGameSetting.boatLauncherSetting.enable){
            javaPath = AppManifest.BOAT_JAVA_DIR + "/";
        }
        else {
            javaPath = AppManifest.POJAV_JAVA_DIR + "/";
        }
        String[] string = new File(javaPath).list();
        if (new File(javaPath).exists()){
            for (String str : string){
                JavaListBean bean = new JavaListBean("","","");
                bean.name = str;
                File release = new File(javaPath,"/" + bean.name + "/release");
                if (release.exists()){
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
            return list;
        }
        else {
            return list;
        }
    }

}
