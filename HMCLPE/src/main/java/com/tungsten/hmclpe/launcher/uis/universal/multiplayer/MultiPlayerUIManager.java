package com.tungsten.hmclpe.launcher.uis.universal.multiplayer;

import android.content.Context;
import android.content.Intent;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.launcher.uis.universal.multiplayer.right.HiPerUI;
import com.tungsten.hmclpe.launcher.uis.universal.multiplayer.right.Hin2nUI;

public class MultiPlayerUIManager {

    public HiPerUI hiPerUI;
    public Hin2nUI hin2nUI;

    public BaseUI[] multiPlayerUIs;

    public MultiPlayerUIManager(Context context, MainActivity activity) {
        hiPerUI = new HiPerUI(context, activity);
        hin2nUI = new Hin2nUI(context, activity);

        hiPerUI.onCreate();
        hin2nUI.onCreate();

        multiPlayerUIs = new BaseUI[] {
                hiPerUI,
                hin2nUI
        };
        switchMultiPlayerUIs(hin2nUI);
    }

    public void switchMultiPlayerUIs(BaseUI ui){
        for (int i = 0;i < multiPlayerUIs.length;i++){
            if (multiPlayerUIs[i] == ui){
                multiPlayerUIs[i].onStart();
            }
            else {
                multiPlayerUIs[i].onStop();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (BaseUI ui : multiPlayerUIs) {
            ui.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void onPause() {
        for (BaseUI ui : multiPlayerUIs) {
            ui.onPause();
        }
    }

    public void onResume() {
        for (BaseUI ui : multiPlayerUIs) {
            ui.onResume();
        }
    }

}
