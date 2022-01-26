package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.AuthenticationException;
import com.tungsten.hmclpe.auth.OAuth;
import com.tungsten.hmclpe.auth.microsoft.MicrosoftService;

import java.io.IOException;

public class AddMicrosoftAccountDialog extends Dialog implements View.OnClickListener {

    private TextView accountSettingLink;
    private TextView helpLink;
    private TextView purchaseLink;

    private Button login;
    private Button cancel;

    public AddMicrosoftAccountDialog(@NonNull Context context) {
        super(context);
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
            MicrosoftService microsoftService = new MicrosoftService(new OAuth.Callback() {
                @Override
                public OAuth.Session startServer() throws IOException, AuthenticationException {
                    return null;
                }

                @Override
                public void grantDeviceCode(String userCode, String verificationURI) {

                }

                @Override
                public void openBrowser(String url) throws IOException {

                }

                @Override
                public String getClientId() {
                    return null;
                }

                @Override
                public String getClientSecret() {
                    return null;
                }

                @Override
                public boolean isPublicClient() {
                    return false;
                }
            });
            new Thread(){
                @Override
                public void run() {
                    try {
                        microsoftService.authenticate();
                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
        if (v == cancel){
            this.dismiss();
        }
    }
}
