package com.tungsten.hmclpe.launcher.dialogs.hin2n;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class Hin2nMenuDialog extends Dialog implements View.OnClickListener {

    private LinearLayout create;
    private LinearLayout join;
    private LinearLayout help;

    public Hin2nMenuDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_hin2n_menu);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        create = findViewById(R.id.create);
        join = findViewById(R.id.join);
        help = findViewById(R.id.help);

        create.setOnClickListener(this);
        join.setOnClickListener(this);
        help.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == create) {
            CreateCommunityDialog dialog = new CreateCommunityDialog(getContext());
            dialog.show();
            dismiss();
        }
        if (view == join) {
            JoinCommunityDialog dialog = new JoinCommunityDialog(getContext());
            dialog.show();
            dismiss();
        }
        if (view == help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getContext().getString(R.string.dialog_hin2n_help_title));
            builder.setMessage(getContext().getString(R.string.dialog_hin2n_help_text));
            builder.setPositiveButton(getContext().getString(R.string.dialog_hin2n_help_positive), null);
            builder.create().show();
            dismiss();
        }
    }
}
