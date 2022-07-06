package com.tungsten.hmclpe.launcher.dialogs.hin2n;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class Hin2nInfoDialog extends Dialog {

    

    public Hin2nInfoDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_hin2n_community);
        setCancelable(false);
        init();
    }

    private void init() {

    }

}
