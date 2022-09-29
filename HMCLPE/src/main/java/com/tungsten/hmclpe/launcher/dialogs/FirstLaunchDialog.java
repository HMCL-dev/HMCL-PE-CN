package com.tungsten.hmclpe.launcher.dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class FirstLaunchDialog extends Dialog implements View.OnClickListener {

    private Button eula;
    private Button positive;

    public FirstLaunchDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_first_start);
        setCancelable(false);
        init();
    }

    private void init() {
        eula = findViewById(R.id.eula);
        positive = findViewById(R.id.positive);

        eula.setOnClickListener(this);
        positive.setOnClickListener(this);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onClick(View view) {
        if (view == eula) {
            Uri uri = Uri.parse("https://tungstend.github.io/pages/eula.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
        if (view == positive) {
            SharedPreferences sharedPreferences;
            SharedPreferences.Editor editor;
            sharedPreferences = getContext().getSharedPreferences("global", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putInt("first_launch", 1);
            editor.apply();
            dismiss();
        }
    }
}
