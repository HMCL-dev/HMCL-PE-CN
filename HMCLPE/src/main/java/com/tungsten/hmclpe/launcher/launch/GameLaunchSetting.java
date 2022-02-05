package com.tungsten.hmclpe.launcher.launch;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.local.java.JavaListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.utils.gson.GsonUtils;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cosine.boat.BoatUtils;

public class GameLaunchSetting {

    public Account account;
    public String home;
    public String currentVersion;

    public String javaPath;
    public String extraJavaFlags;
    public String extraMinecraftFlags;
    public String game_directory;
    public String boatRenderer;
    public String pojavRenderer;
    public float scaleFactor;
    public int minRam;
    public int maxRam;

    public String gameFileDirectory;

    public GameLaunchSetting(Account account,String home,String currentVersion,String javaPath,String extraJavaFlags,String extraMinecraftFlags,String game_directory,String boatRenderer,String pojavRenderer,float scaleFactor,String gameFileDirectory,int minRam,int maxRam){
        this.account = account;
        this.home = home;
        this.currentVersion = currentVersion;

        this.javaPath = javaPath;
        this.extraJavaFlags = extraJavaFlags;
        this.extraMinecraftFlags = extraMinecraftFlags;
        this.game_directory = game_directory;
        this.boatRenderer = boatRenderer;
        this.pojavRenderer = pojavRenderer;
        this.scaleFactor = scaleFactor;
        this.minRam = minRam;
        this.maxRam = maxRam;

        this.gameFileDirectory = gameFileDirectory;
    }

    public static boolean isHighVersion(GameLaunchSetting gameLaunchSetting){
        LaunchVersion version = LaunchVersion.fromDirectory(new File(gameLaunchSetting.currentVersion));
        return version.minimumLauncherVersion >= 21;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static GameLaunchSetting getGameLaunchSetting(String privatePath){
        LauncherSetting launcherSetting = GsonUtils.getLauncherSettingFromFile(AppManifest.SETTING_DIR + "/launcher_setting.json");
        PublicGameSetting publicGameSetting = GsonUtils.getPublicGameSettingFromFile(AppManifest.SETTING_DIR + "/public_game_setting.json");
        PrivateGameSetting privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(privatePath);

        String gameDir;
        if (privateGameSetting.gameDirSetting.type == 0){
            gameDir = launcherSetting.gameFileDirectory;
        }
        else if (privateGameSetting.gameDirSetting.type == 1){
            gameDir = publicGameSetting.currentVersion;
        }
        else {
            gameDir = privateGameSetting.gameDirSetting.path;
        }

        String javaPath = "";
        if (privateGameSetting.javaSetting.autoSelect){
            String versionJson = null;
            try {
                versionJson = new String(BoatUtils.readFile(new File(new File(publicGameSetting.currentVersion), (new File(publicGameSetting.currentVersion)).getName() + ".json")), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Gson gson = JsonUtils.defaultGsonBuilder()
                    .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                    .registerTypeAdapter(Bits.class, new Bits.Serializer())
                    .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                    .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                    .create();
            Version version = gson.fromJson(versionJson, Version.class);
            if (version.getJavaVersion().getMajorVersion() == 8){
                javaPath = AppManifest.JAVA_DIR + "/default";
            }
            else {
                for (JavaListBean javaListBean : SettingUtils.getJavaVersionInfo()){
                    if (javaListBean.version.equals("17")){
                        javaPath = AppManifest.JAVA_DIR + "/" + javaListBean.name;
                        break;
                    }
                }
            }
        }
        else {
            javaPath = AppManifest.JAVA_DIR + "/" + privateGameSetting.javaSetting.name;
        }

        GameLaunchSetting gameLaunchSetting = new GameLaunchSetting(publicGameSetting.account,
                publicGameSetting.home,
                publicGameSetting.currentVersion,
                javaPath,
                privateGameSetting.extraJavaFlags,
                privateGameSetting.extraMinecraftFlags,
                gameDir,
                privateGameSetting.boatLauncherSetting.renderer,
                privateGameSetting.pojavLauncherSetting.renderer,
                privateGameSetting.scaleFactor,
                launcherSetting.gameFileDirectory,
                privateGameSetting.ramSetting.minRam,
                privateGameSetting.ramSetting.maxRam);
        return gameLaunchSetting;
    }

}
