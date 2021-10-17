package com.tungsten.hmclpe.launcher.uis.tools;

import android.content.Context;
import android.content.Intent;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.account.AccountUI;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUI;
import com.tungsten.hmclpe.launcher.uis.game.manager.GameManagerUI;
import com.tungsten.hmclpe.launcher.uis.game.version.VersionListUI;
import com.tungsten.hmclpe.launcher.uis.game.version.universal.AddGameDirectoryUI;
import com.tungsten.hmclpe.launcher.uis.game.version.universal.InstallPackageUI;
import com.tungsten.hmclpe.launcher.uis.main.MainUI;
import com.tungsten.hmclpe.launcher.uis.universal.setting.SettingUI;

public class UIManager {

    public MainUI mainUI;
    public AccountUI accountUI;
    public GameManagerUI gameManagerUI;
    public VersionListUI versionListUI;
    public DownloadUI downloadUI;
    public SettingUI settingUI;

    public AddGameDirectoryUI addGameDirectoryUI;
    public InstallPackageUI installPackageUI;

    public BaseUI[] mainUIs;
    public BaseUI currentUI;
    public BaseUI previousUI;

    public UIManager (Context context, MainActivity activity){
        mainUI = new MainUI(context,activity);
        accountUI = new AccountUI(context,activity);
        gameManagerUI = new GameManagerUI(context,activity);
        versionListUI = new VersionListUI(context,activity);
        downloadUI = new DownloadUI(context,activity);
        settingUI = new SettingUI(context,activity);

        addGameDirectoryUI = new AddGameDirectoryUI(context,activity);
        installPackageUI = new InstallPackageUI(context,activity);

        mainUI.onCreate();
        accountUI.onCreate();
        gameManagerUI.onCreate();
        versionListUI.onCreate();
        downloadUI.onCreate();
        settingUI.onCreate();

        addGameDirectoryUI.onCreate();
        installPackageUI.onCreate();

        mainUIs = new BaseUI[]{mainUI,addGameDirectoryUI,installPackageUI,accountUI,gameManagerUI,versionListUI,downloadUI,settingUI};
        switchMainUI(mainUI);
    }

    public void switchMainUI(BaseUI ui){
        previousUI = currentUI;
        currentUI = ui;
        for (int i = 0;i < mainUIs.length;i++){
            if (mainUIs[i] == currentUI){
                mainUIs[i].onStart();
            }
            else if (mainUIs[i] == previousUI) {
                mainUIs[i].onStop();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == AddGameDirectoryUI.PICK_GAME_FILE_FOLDER_REQUEST){
            addGameDirectoryUI.onActivityResult(requestCode,resultCode,data);
        }
    }
}
