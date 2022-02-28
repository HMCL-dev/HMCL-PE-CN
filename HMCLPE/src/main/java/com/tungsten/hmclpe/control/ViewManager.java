package com.tungsten.hmclpe.control;

import android.app.Activity;
import android.content.Context;

import androidx.core.view.GravityCompat;

import com.tungsten.hmclpe.control.view.LayoutPanel;
import com.tungsten.hmclpe.control.view.MenuFloat;
import com.tungsten.hmclpe.control.view.MenuView;
import com.tungsten.hmclpe.control.view.TouchPad;
import com.tungsten.hmclpe.launcher.setting.game.GameMenuSetting;

public class ViewManager {

    public Context context;
    public Activity activity;
    public MenuHelper menuHelper;
    public LayoutPanel layoutPanel;
    public int launcher;

    public int gameCursorMode = 0;
    public TouchPad touchPad;

    public MenuFloat menuFloat;
    public MenuView menuView;

    public ViewManager (Context context, Activity activity, MenuHelper menuHelper, LayoutPanel layoutPanel,int launcher) {
        this.context = context;
        this.activity = activity;
        this.menuHelper = menuHelper;
        this.layoutPanel = layoutPanel;
        this.launcher = launcher;
        init();
    }

    private void init(){
        /*
         *初始化触控板
         */
        touchPad = new TouchPad(context,launcher,layoutPanel.getWidth(),layoutPanel.getHeight(),menuHelper,gameCursorMode);
        layoutPanel.addView(touchPad);
        /*
         *初始化菜单键
         */
        menuFloat = new MenuFloat(context,layoutPanel.getWidth(),layoutPanel.getHeight(),menuHelper.gameMenuSetting.menuFloatSetting.positionX,menuHelper.gameMenuSetting.menuFloatSetting.positionY);
        menuFloat.addCallback(new MenuFloat.MenuFloatCallback() {
            @Override
            public void onClick() {
                menuHelper.drawerLayout.openDrawer(GravityCompat.END,true);
                menuHelper.drawerLayout.openDrawer(GravityCompat.START,true);
            }

            @Override
            public void onMove(float xPosition, float yPosition) {
                menuHelper.gameMenuSetting.menuFloatSetting.positionX = xPosition;
                menuHelper.gameMenuSetting.menuFloatSetting.positionY = yPosition;
                GameMenuSetting.saveGameMenuSetting(menuHelper.gameMenuSetting);
            }
        });
        menuView = new MenuView(context,layoutPanel.getWidth(),layoutPanel.getHeight(),menuHelper.gameMenuSetting.menuViewSetting.mode,menuHelper.gameMenuSetting.menuViewSetting.yPercent);
        menuView.addCallback(new MenuView.MenuCallback() {
            @Override
            public void onRelease() {
                menuHelper.drawerLayout.openDrawer(GravityCompat.END,true);
                menuHelper.drawerLayout.openDrawer(GravityCompat.START,true);
            }

            @Override
            public void onMoveModeStart() {

            }

            @Override
            public void onMove(int mode, float yPercent) {
                menuHelper.gameMenuSetting.menuViewSetting.mode = mode;
                menuHelper.gameMenuSetting.menuViewSetting.yPercent = yPercent;
                GameMenuSetting.saveGameMenuSetting(menuHelper.gameMenuSetting);
            }

            @Override
            public void onMoveModeStop() {

            }
        });
        if (menuHelper.gameMenuSetting.menuFloatSetting.enable){
            layoutPanel.addView(menuFloat);
        }
        if (menuHelper.gameMenuSetting.menuViewSetting.enable){
            layoutPanel.addView(menuView);
        }
    }

    public void enableCursor(){
        gameCursorMode = 0;
    }

    public void disableCursor(){
        gameCursorMode = 1;
    }

}
