package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class OfflineAccountSkinDialog extends Dialog {

    public OfflineAccountSkinDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_offline_skin);
    }
}
