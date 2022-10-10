package com.tungsten.hmclpe.launcher.uis.universal.multiplayer.right;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.hin2n.CreateCommunityDialog;
import com.tungsten.hmclpe.launcher.dialogs.hin2n.JoinCommunityDialog;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.multiplayer.hin2n.Hin2nService;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import wang.switchy.hin2n.model.EdgeStatus;
import wang.switchy.hin2n.model.N2NSettingInfo;

public class Hin2nUI extends BaseUI implements View.OnClickListener {

    public LinearLayout hin2nUI;

    private Button create;
    private Button join;

    public Hin2nUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        hin2nUI = activity.findViewById(R.id.ui_hin2n);

        create = activity.findViewById(R.id.create_hin2n_community);
        join = activity.findViewById(R.id.join_hin2n_community);

        create.setOnClickListener(this);
        join.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(hin2nUI,activity,context,false);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(hin2nUI,activity,context,false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Hin2nService.VPN_REQUEST_CODE_CREATE && resultCode == RESULT_OK) {
            Intent intent = new Intent(context, Hin2nService.class);
            Bundle bundle = new Bundle();
            N2NSettingInfo n2NSettingInfo = new N2NSettingInfo(Hin2nService.getCreatorModel());
            bundle.putParcelable("n2nSettingInfo", n2NSettingInfo);
            intent.putExtra("Setting", bundle);
            activity.startService(intent);
        }
        if (requestCode == Hin2nService.VPN_REQUEST_CODE_JOIN && resultCode == RESULT_OK) {
            Intent intent = new Intent(context, Hin2nService.class);
            Bundle bundle = new Bundle();
            new Thread(() -> {
                N2NSettingInfo n2NSettingInfo = new N2NSettingInfo(Hin2nService.getPlayerModel());
                activity.runOnUiThread(() -> {
                    bundle.putParcelable("n2nSettingInfo", n2NSettingInfo);
                    intent.putExtra("Setting", bundle);
                    activity.startService(intent);
                    JoinCommunityDialog.getInstance().progressBar.setVisibility(View.GONE);
                    JoinCommunityDialog.getInstance().positive.setVisibility(View.VISIBLE);
                    JoinCommunityDialog.getInstance().negative.setEnabled(true);
                    JoinCommunityDialog.getInstance().dismiss();
                });
            }).start();
        }
    }

    @Override
    public void onClick(View view) {
        EdgeStatus.RunningStatus status = Hin2nService.INSTANCE == null ? EdgeStatus.RunningStatus.DISCONNECT : Hin2nService.INSTANCE.getCurrentStatus();
        boolean isCreated = Hin2nService.INSTANCE != null && status != EdgeStatus.RunningStatus.DISCONNECT && status != EdgeStatus.RunningStatus.FAILED;
        if (view == create) {
            if (!isCreated) {
                CreateCommunityDialog dialog = new CreateCommunityDialog(context, null, this);
                dialog.show();
            }
            else {
                Toast.makeText(context, context.getString(R.string.dialog_hin2n_menu_in), Toast.LENGTH_SHORT).show();
            }
        }
        if (view == join) {
            if (!isCreated) {
                JoinCommunityDialog dialog = new JoinCommunityDialog(context, null, this);
                dialog.show();
            }
            else {
                Toast.makeText(context, context.getString(R.string.dialog_hin2n_menu_in), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
