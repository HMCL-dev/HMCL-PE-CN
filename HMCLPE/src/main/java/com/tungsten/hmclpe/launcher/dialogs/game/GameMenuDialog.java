package com.tungsten.hmclpe.launcher.dialogs.game;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class GameMenuDialog extends Dialog {

    public GameMenuDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_game_menu);
        setCanceledOnTouchOutside(false);
    }

}
