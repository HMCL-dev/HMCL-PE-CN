package com.tungsten.hmclpe.launcher.setting.game;

public class PojavLauncherSetting {

    public boolean enable;
    public String renderer;

    public PojavLauncherSetting(boolean enable,String renderer){
        this.enable = enable;
        this.renderer = renderer;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getRenderer() {
        return renderer;
    }
}
