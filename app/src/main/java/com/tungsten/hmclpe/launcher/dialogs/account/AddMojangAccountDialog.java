package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class AddMojangAccountDialog extends Dialog implements View.OnClickListener, TextWatcher {

    private TextView editEmail;
    private TextView editPassword;

    private TextView migrateLink;
    private TextView helpLink;
    private TextView purchaseLink;

    private Button login;
    private Button cancel;

    public AddMojangAccountDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_add_mojang_account);
        setCancelable(false);
        init();
    }

    private void init(){
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);

        editEmail.addTextChangedListener(this);
        editPassword.addTextChangedListener(this);

        migrateLink = findViewById(R.id.migrate_link);
        helpLink = findViewById(R.id.help_link);
        purchaseLink = findViewById(R.id.purchase_link);

        migrateLink.setMovementMethod(LinkMovementMethod.getInstance());
        helpLink.setMovementMethod(LinkMovementMethod.getInstance());
        purchaseLink.setMovementMethod(LinkMovementMethod.getInstance());

        login = findViewById(R.id.login_mojang);
        cancel = findViewById(R.id.cancel_login_mojang);

        login.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == login){

        }
        if (v == cancel){
            this.dismiss();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
