package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class AddVerifyServerDialog extends Dialog implements View.OnClickListener, TextWatcher {

    private EditText editVerifyServer;
    private Button cancel;
    private Button next;

    public AddVerifyServerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_add_verify_server);
        setCancelable(false);
        init();
    }

    private void init(){
        editVerifyServer = findViewById(R.id.edit_verify_server);
        editVerifyServer.addTextChangedListener(this);

        cancel = findViewById(R.id.cancel);
        next = findViewById(R.id.next);

        cancel.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == cancel){
            this.dismiss();
        }
        if (v == next){

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
