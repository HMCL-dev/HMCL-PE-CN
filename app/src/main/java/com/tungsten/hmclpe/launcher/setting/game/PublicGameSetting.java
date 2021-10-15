package com.tungsten.hmclpe.launcher.setting.game;

import com.tungsten.hmclpe.auth.Account;

public class PublicGameSetting {

    public Account account;
    public String home;
    public String currentVersion;

    public PublicGameSetting (Account account,String home,String currentVersion){
        this.account = account;
        this.home = home;
        this.currentVersion = currentVersion;
    }
}
