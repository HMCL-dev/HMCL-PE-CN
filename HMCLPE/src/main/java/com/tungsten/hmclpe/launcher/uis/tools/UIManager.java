package com.tungsten.hmclpe.launcher.uis.tools;

import android.content.Context;
import android.content.Intent;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.account.AccountUI;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUI;
import com.tungsten.hmclpe.launcher.uis.game.download.right.game.InstallGameUI;
import com.tungsten.hmclpe.launcher.uis.game.manager.GameManagerUI;
import com.tungsten.hmclpe.launcher.uis.game.version.VersionListUI;
import com.tungsten.hmclpe.launcher.uis.game.version.universal.AddGameDirectoryUI;
import com.tungsten.hmclpe.launcher.uis.game.version.universal.InstallPackageUI;
import com.tungsten.hmclpe.launcher.uis.main.MainUI;
import com.tungsten.hmclpe.launcher.uis.universal.setting.SettingUI;

import java.util.ArrayList;

public class UIManager {

    public MainUI mainUI;
    public AccountUI accountUI;
    public GameManagerUI gameManagerUI;
    public VersionListUI versionListUI;
    public DownloadUI downloadUI;
    public SettingUI settingUI;

    public AddGameDirectoryUI addGameDirectoryUI;
    public InstallPackageUI installPackageUI;

    public InstallGameUI installGameUI;

    public BaseUI[] mainUIs;
    public ArrayList<BaseUI> uis;
    public BaseUI currentUI;

    public UIManager (Context context, MainActivity activity){
        mainUI = new MainUI(context,activity);
        accountUI = new AccountUI(context,activity);
        gameManagerUI = new GameManagerUI(context,activity);
        versionListUI = new VersionListUI(context,activity);
        downloadUI = new DownloadUI(context,activity);
        settingUI = new SettingUI(context,activity);

        addGameDirectoryUI = new AddGameDirectoryUI(context,activity);
        installPackageUI = new InstallPackageUI(context,activity);

        installGameUI = new InstallGameUI(context,activity);

        mainUI.onCreate();
        accountUI.onCreate();
        gameManagerUI.onCreate();
        versionListUI.onCreate();
        downloadUI.onCreate();
        settingUI.onCreate();

        addGameDirectoryUI.onCreate();
        installPackageUI.onCreate();

        installGameUI.onCreate();

        mainUIs = new BaseUI[]{mainUI,addGameDirectoryUI,installPackageUI,accountUI,gameManagerUI,versionListUI,downloadUI,settingUI,installGameUI};
        uis = new ArrayList<>();
        switchMainUI(mainUI);
    }

    public void switchMainUI(BaseUI ui){
        currentUI = ui;
        ui.onStart();
        if (uis.size() > 0){
            uis.get(uis.size() - 1).onStop();
        }
        uis.add(ui);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        for (BaseUI ui : mainUIs){
            ui.onActivityResult(requestCode,resultCode,data);
        }
    }
}
