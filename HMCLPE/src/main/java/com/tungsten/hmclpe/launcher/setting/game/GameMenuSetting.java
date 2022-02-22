package com.tungsten.hmclpe.launcher.setting.game;

import com.google.gson.Gson;
import com.tungsten.hmclpe.control.view.MenuView;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.game.child.MenuFloatSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.MenuViewSetting;
import com.tungsten.hmclpe.utils.file.FileStringUtils;

import java.io.File;

public class GameMenuSetting {

    public MenuFloatSetting menuFloatSetting;
    public MenuViewSetting menuViewSetting;
    public boolean menuSlideSetting;

    public GameMenuSetting(MenuFloatSetting menuFloatSetting,MenuViewSetting menuViewSetting,boolean menuSlideSetting){
        this.menuFloatSetting = menuFloatSetting;
        this.menuViewSetting = menuViewSetting;
        this.menuSlideSetting = menuSlideSetting;
    }

    public static GameMenuSetting getGameMenuSetting(){
        GameMenuSetting gameMenuSetting;
        String path = AppManifest.SETTING_DIR + "/game_menu_setting.json";
        if (!new File(path).exists()){
            gameMenuSetting = new GameMenuSetting(new MenuFloatSetting(true,0.5f,0.5f),
                    new MenuViewSetting(true, MenuView.MENU_MODE_LEFT,0.2f),
                    true);
            saveGameMenuSetting(gameMenuSetting);
        }
        else {
            String string = FileStringUtils.getStringFromFile(path);
            Gson gson = new Gson();
            gameMenuSetting = gson.fromJson(string,GameMenuSetting.class);
        }
        return gameMenuSetting;
    }

    public static void saveGameMenuSetting(GameMenuSetting gameMenuSetting){
        String path = AppManifest.SETTING_DIR + "/game_menu_setting.json";
        Gson gson = new Gson();
        String string = gson.toJson(gameMenuSetting);
        FileStringUtils.writeFile(path,string);
    }

}
