package com.tungsten.hmclpe.launcher.uis.universal.multiplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class MultiPlayerUI extends BaseUI implements View.OnClickListener {

    public LinearLayout multiPlayerUI;

    private LinearLayout help;

    public MultiPlayerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        multiPlayerUI = activity.findViewById(R.id.ui_multi_player);

        help = activity.findViewById(R.id.multiplayer_help);

        help.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.multi_player_ui_title),activity.uiManager.uis.get(activity.uiManager.uis.size() - 2) != activity.uiManager.mainUI,false);
        CustomAnimationUtils.showViewFromLeft(multiPlayerUI,activity,context,true);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(multiPlayerUI,activity,context,true);
    }

    @Override
    public void onClick(View view) {
        if (view == help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.dialog_hin2n_help_title));
            builder.setMessage(context.getString(R.string.dialog_hin2n_help_text));
            builder.setPositiveButton(context.getString(R.string.dialog_hin2n_help_positive), null);
            builder.create().show();
        }
    }
}
