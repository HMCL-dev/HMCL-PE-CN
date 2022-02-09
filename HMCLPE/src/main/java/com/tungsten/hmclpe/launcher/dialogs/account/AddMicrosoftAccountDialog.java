package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.microsoft.MicrosoftLoginActivity;
import com.tungsten.hmclpe.launcher.MainActivity;

public class AddMicrosoftAccountDialog extends Dialog implements View.OnClickListener {

    private MainActivity activity;

    private TextView accountSettingLink;
    private TextView helpLink;
    private TextView purchaseLink;

    private Button login;
    private Button cancel;

    public AddMicrosoftAccountDialog(@NonNull Context context, MainActivity activity) {
        super(context);
        this.activity = activity;
        setContentView(R.layout.dialog_add_microsoft_account);
        setCancelable(false);
        init();
    }

    private void init(){
        accountSettingLink = findViewById(R.id.setting_link);
        helpLink = findViewById(R.id.help_link);
        purchaseLink = findViewById(R.id.purchase_link);

        accountSettingLink.setMovementMethod(LinkMovementMethod.getInstance());
        helpLink.setMovementMethod(LinkMovementMethod.getInstance());
        purchaseLink.setMovementMethod(LinkMovementMethod.getInstance());

        login = findViewById(R.id.login_microsoft);
        cancel = findViewById(R.id.cancel_login_microsoft);

        login.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == login){
            Intent i = new Intent(getContext(), MicrosoftLoginActivity.class);
            activity.startActivityForResult(i,MicrosoftLoginActivity.AUTHENTICATE_MICROSOFT_REQUEST);
        }
        if (v == cancel){
            this.dismiss();
        }
    }

    public void login(Intent intent){

    }
}
