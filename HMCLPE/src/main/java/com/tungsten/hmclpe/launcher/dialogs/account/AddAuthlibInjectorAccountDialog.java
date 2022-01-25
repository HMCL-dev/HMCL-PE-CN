package com.tungsten.hmclpe.launcher.dialogs.account;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.auth.AuthInfo;
import com.tungsten.hmclpe.auth.AuthenticationException;
import com.tungsten.hmclpe.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.hmclpe.auth.yggdrasil.YggdrasilService;
import com.tungsten.hmclpe.auth.yggdrasil.YggdrasilSession;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.account.server.AuthlibInjectorServerSpinnerAdapter;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.util.ArrayList;

public class AddAuthlibInjectorAccountDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private MainActivity activity;
    private ArrayList<Account> accounts;
    private OnAuthlibInjectorAccountAddListener onAuthlibInjectorAccountAddListener;
    private ArrayList<AuthlibInjectorServer> list;
    private AuthlibInjectorServer authlibInjectorServer;

    private Spinner editServer;
    private TextView signUp;
    private ImageButton addServer;
    private EditText editEmail;
    private EditText editPassword;
    private Button login;
    private Button cancel;

    private AuthlibInjectorServerSpinnerAdapter serverListAdapter;

    private String signUpUrl;

    private Account account;

    public AddAuthlibInjectorAccountDialog(@NonNull Context context, MainActivity activity, ArrayList<Account> accounts,OnAuthlibInjectorAccountAddListener onAuthlibInjectorAccountAddListener,ArrayList<AuthlibInjectorServer> list,AuthlibInjectorServer authlibInjectorServer) {
        super(context);
        this.activity = activity;
        this.accounts = accounts;
        this.onAuthlibInjectorAccountAddListener = onAuthlibInjectorAccountAddListener;
        this.list = list;
        this.authlibInjectorServer = authlibInjectorServer;
        setContentView(R.layout.dialog_add_authlib_injector_account);
        setCancelable(false);
        init();
    }

    private void init(){
        editServer = findViewById(R.id.edit_server);
        signUp = findViewById(R.id.sign_up);
        addServer = findViewById(R.id.add_server);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        login = findViewById(R.id.login_authlib);
        cancel = findViewById(R.id.cancel_login_authlib);

        signUp.setOnClickListener(this);
        addServer.setOnClickListener(this);
        login.setOnClickListener(this);
        cancel.setOnClickListener(this);

        serverListAdapter = new AuthlibInjectorServerSpinnerAdapter(getContext(),list);
        editServer.setAdapter(serverListAdapter);
        editServer.setOnItemSelectedListener(this);
        editServer.setSelection(serverListAdapter.getItemPosition(authlibInjectorServer));
    }

    @Override
    public void onClick(View v) {
        if (v == signUp){
            Uri uri = Uri.parse(signUpUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
        if (v == addServer){
            AddVerifyServerDialog addVerifyServerDialog = new AddVerifyServerDialog(getContext(), new AddVerifyServerDialog.OnAuthlibInjectorServerAddListener() {
                @Override
                public void onServerAdd(AuthlibInjectorServer authlibInjectorServer) {
                    if (!activity.uiManager.accountUI.serverList.contains(authlibInjectorServer)){
                        activity.uiManager.accountUI.serverList.add(authlibInjectorServer);
                        activity.uiManager.accountUI.serverListAdapter.notifyDataSetChanged();
                        GsonUtils.saveServer(activity.uiManager.accountUI.serverList, AppManifest.ACCOUNT_DIR + "/authlib_injector_server.json");
                    }
                }
            });
            addVerifyServerDialog.show();
        }
        if (v == login){
            boolean exist = false;
            for (Account account : accounts){
                if (account.loginType == 4){
                    if (account.loginServer.equals(authlibInjectorServer.getUrl())){
                        exist = account.email.equals(editEmail.getText().toString());
                        if (exist){
                            break;
                        }
                    }
                }
            }
            if (exist){
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_add_authlib_injector_account_exist_warn), Toast.LENGTH_SHORT).show();
            }
            else if (editEmail.getText().toString().equals("") || editPassword.getText().toString().equals("")){
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_add_authlib_injector_account_empty_warn), Toast.LENGTH_SHORT).show();
            }
            else {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        YggdrasilService yggdrasilService = authlibInjectorServer.getYggdrasilService();
                        try {
                            YggdrasilSession yggdrasilSession = yggdrasilService.authenticate(email,password,"00000000-0000-0000-0000-000000000000");
                            AuthInfo authInfo = yggdrasilSession.toAuthInfo();
                            account = new Account(4,
                                    email,
                                    password,
                                    "mojang",
                                    "0",
                                    authInfo.getUsername(),
                                    authInfo.getUUID().toString(),
                                    authInfo.getAccessToken(),
                                    "",
                                    authlibInjectorServer.getUrl());
                            loginHandler.sendEmptyMessage(0);
                        } catch (AuthenticationException e) {
                            e.printStackTrace();
                            loginHandler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        }
        if (v == cancel){
            this.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == editServer){
            authlibInjectorServer = (AuthlibInjectorServer) serverListAdapter.getItem(position);
            signUpUrl = authlibInjectorServer.getLinks().get("register");
            if (signUpUrl == null){
                signUp.setVisibility(View.GONE);
            }
            else {
                signUp.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnAuthlibInjectorAccountAddListener{
        void onAccountAdd(Account account);
    }

    @SuppressLint("HandlerLeak")
    public final Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                onAuthlibInjectorAccountAddListener.onAccountAdd(account);
                AddAuthlibInjectorAccountDialog.this.dismiss();
            }
            if (msg.what == 1) {
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_add_authlib_injector_account_failed), Toast.LENGTH_SHORT).show();
            }
        }
    };

}
