package com.tungsten.hmclpe.control;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.view.MenuFloat;
import com.tungsten.hmclpe.control.view.MenuView;
import com.tungsten.hmclpe.launcher.setting.game.GameMenuSetting;

public class MenuHelper implements CompoundButton.OnCheckedChangeListener {

    public Context context;
    public AppCompatActivity activity;
    public DrawerLayout drawerLayout;
    public RelativeLayout baseLayout;

    public GameMenuSetting gameMenuSetting;

    public SwitchCompat switchMenuFloat;
    public SwitchCompat switchMenuView;
    public SwitchCompat switchMenuSlide;

    public MenuFloat menuFloat;
    public MenuView menuView;

    public MenuHelper(Context context, AppCompatActivity activity, DrawerLayout drawerLayout, RelativeLayout baseLayout){
        this.context = context;
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.baseLayout = baseLayout;

        gameMenuSetting = GameMenuSetting.getGameMenuSetting();
        init();
    }

    public void init(){
        switchMenuFloat = activity.findViewById(R.id.switch_float_button);
        switchMenuView = activity.findViewById(R.id.switch_bar);
        switchMenuSlide = activity.findViewById(R.id.switch_gesture);

        switchMenuFloat.setChecked(gameMenuSetting.menuFloatSetting.enable);
        switchMenuView.setChecked(gameMenuSetting.menuViewSetting.enable);
        switchMenuSlide.setChecked(gameMenuSetting.menuSlideSetting);

        switchMenuFloat.setOnCheckedChangeListener(this);
        switchMenuView.setOnCheckedChangeListener(this);
        switchMenuSlide.setOnCheckedChangeListener(this);

        baseLayout.post(new Runnable() {
            @Override
            public void run() {
                menuFloat = new MenuFloat(context,baseLayout.getWidth(),baseLayout.getHeight(),gameMenuSetting.menuFloatSetting.positionX,gameMenuSetting.menuFloatSetting.positionY);
                menuFloat.addCallback(new MenuFloat.MenuFloatCallback() {
                    @Override
                    public void onClick() {
                        drawerLayout.openDrawer(GravityCompat.END,true);
                        drawerLayout.openDrawer(GravityCompat.START,true);
                    }

                    @Override
                    public void onMove(float xPosition, float yPosition) {
                        gameMenuSetting.menuFloatSetting.positionX = xPosition;
                        gameMenuSetting.menuFloatSetting.positionY = yPosition;
                        GameMenuSetting.saveGameMenuSetting(gameMenuSetting);
                    }
                });
                menuView = new MenuView(context,baseLayout.getWidth(),baseLayout.getHeight(),gameMenuSetting.menuViewSetting.mode,gameMenuSetting.menuViewSetting.yPercent);
                menuView.addCallback(new MenuView.MenuCallback() {
                    @Override
                    public void onRelease() {
                        drawerLayout.openDrawer(GravityCompat.END,true);
                        drawerLayout.openDrawer(GravityCompat.START,true);
                    }

                    @Override
                    public void onMoveModeStart() {

                    }

                    @Override
                    public void onMove(int mode, float yPercent) {
                        gameMenuSetting.menuViewSetting.mode = mode;
                        gameMenuSetting.menuViewSetting.yPercent = yPercent;
                        GameMenuSetting.saveGameMenuSetting(gameMenuSetting);
                    }

                    @Override
                    public void onMoveModeStop() {

                    }
                });
                if (gameMenuSetting.menuFloatSetting.enable){
                    baseLayout.addView(menuFloat);
                }
                if (gameMenuSetting.menuViewSetting.enable){
                    baseLayout.addView(menuView);
                }
                checkOpenMenuSetting();
            }
        });

        if (gameMenuSetting.menuSlideSetting){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void checkOpenMenuSetting(){
        if (!gameMenuSetting.menuFloatSetting.enable && !gameMenuSetting.menuViewSetting.enable && !gameMenuSetting.menuSlideSetting){
            switchMenuFloat.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == switchMenuFloat){
            gameMenuSetting.menuFloatSetting.enable = b;
            if (b){
                baseLayout.addView(menuFloat);
            }
            else {
                baseLayout.removeView(menuFloat);
            }
            checkOpenMenuSetting();
        }
        if (compoundButton == switchMenuView){
            gameMenuSetting.menuViewSetting.enable = b;
            if (b){
                baseLayout.addView(menuView);
            }
            else {
                baseLayout.removeView(menuView);
            }
            checkOpenMenuSetting();
        }
        if (compoundButton == switchMenuSlide){
            gameMenuSetting.menuSlideSetting = b;
            if (b){
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            checkOpenMenuSetting();
        }
        GameMenuSetting.saveGameMenuSetting(gameMenuSetting);
    }
}
