package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class ExportControlDialog extends Dialog {

    public ExportControlDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_export_control);
        setCancelable(false);
    }

}
