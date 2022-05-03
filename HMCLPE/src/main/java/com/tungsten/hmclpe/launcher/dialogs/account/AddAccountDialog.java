package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.MainActivity;

public class AddAccountDialog extends Dialog {

    private MainActivity activity;

    public AddAccountDialog(@NonNull Context context,MainActivity activity) {
        super(context);
    }

    public interface AddAccountCallback{
        void onAccountAdd(Account account);
        void onCancel();
    }

}
