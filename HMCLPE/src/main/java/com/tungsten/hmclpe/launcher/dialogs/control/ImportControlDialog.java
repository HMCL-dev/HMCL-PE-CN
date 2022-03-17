package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class ImportControlDialog extends Dialog {

    public ImportControlDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_import_control);
        setCancelable(false);
    }

}
