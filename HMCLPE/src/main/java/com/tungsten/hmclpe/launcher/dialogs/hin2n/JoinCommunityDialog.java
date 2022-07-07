package com.tungsten.hmclpe.launcher.dialogs.hin2n;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.multiplayer.Hin2nService;
import com.tungsten.hmclpe.utils.string.StringUtils;

import wang.switchy.hin2n.model.EdgeStatus;

public class JoinCommunityDialog extends Dialog implements View.OnClickListener {

    private final MenuHelper menuHelper;

    private EditText editText;
    private Button positive;
    private Button negative;

    public JoinCommunityDialog(@NonNull Context context, MenuHelper menuHelper) {
        super(context);
        this.menuHelper = menuHelper;
        setContentView(R.layout.dialog_join_community);
        setCancelable(false);
        init();
    }

    private void init() {
        editText = findViewById(R.id.invite_code);
        positive = findViewById(R.id.join);
        negative = findViewById(R.id.exit);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {
            if (StringUtils.isNotBlank(editText.getText().toString())) {
                Hin2nService.COMMUNITY_CODE = editText.getText().toString();
                EdgeStatus.RunningStatus status = Hin2nService.INSTANCE == null ? EdgeStatus.RunningStatus.DISCONNECT : Hin2nService.INSTANCE.getCurrentStatus();
                if (Hin2nService.INSTANCE != null && status != EdgeStatus.RunningStatus.DISCONNECT && status != EdgeStatus.RunningStatus.FAILED) {
                    System.out.println("stop");
                    Hin2nService.INSTANCE.stop(null);
                }
                Intent vpnPrepareIntent = VpnService.prepare(menuHelper.context);
                if (vpnPrepareIntent != null) {
                    menuHelper.activity.startActivityForResult(vpnPrepareIntent, Hin2nService.VPN_REQUEST_CODE_JOIN);
                } else {
                    menuHelper.onActivityResult(Hin2nService.VPN_REQUEST_CODE_JOIN, RESULT_OK, null);
                }
                dismiss();
            }
            else {
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_join_community_code_empty), Toast.LENGTH_SHORT).show();
            }
        }
        if (view == negative) {
            dismiss();
        }
    }
}
