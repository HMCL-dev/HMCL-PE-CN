package com.tungsten.hmclpe.launcher.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.mod.RemoteMod;
import com.tungsten.hmclpe.launcher.uis.game.download.right.resource.DownloadResourceUI;

public class EditDownloadNameDialog extends Dialog implements View.OnClickListener {

    private DownloadResourceUI ui;
    private RemoteMod.Version version;

    private EditText editText;
    private Button positive;
    private Button negative;

    public EditDownloadNameDialog(@NonNull Context context, DownloadResourceUI ui, RemoteMod.Version version) {
        super(context);
        this.ui = ui;
        this.version = version;
        setContentView(R.layout.dialog_edit_download_name);
        setCancelable(false);
        init();
    }

    private void init() {
        editText = findViewById(R.id.download_name);
        positive = findViewById(R.id.download);
        negative = findViewById(R.id.cancel);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        editText.setText(version.getFile().getFilename());
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {

        }
        if (view == negative) {
            dismiss();
        }
    }
}
