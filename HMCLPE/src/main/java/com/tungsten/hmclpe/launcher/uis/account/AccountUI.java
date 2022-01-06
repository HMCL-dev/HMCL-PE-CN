package com.tungsten.hmclpe.launcher.uis.account;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.account.AddMicrosoftAccountDialog;
import com.tungsten.hmclpe.launcher.dialogs.account.AddMojangAccountDialog;
import com.tungsten.hmclpe.launcher.dialogs.account.AddOfflineAccountDialog;
import com.tungsten.hmclpe.launcher.dialogs.account.AddVerifyServerDialog;
import com.tungsten.hmclpe.launcher.list.account.AccountListAdapter;
import com.tungsten.hmclpe.launcher.setting.InitializeSetting;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.util.ArrayList;

public class AccountUI extends BaseUI implements View.OnClickListener {

    public LinearLayout accountUI;

    private LinearLayout addOfflineAccount;
    private LinearLayout addMojangAccount;
    private LinearLayout addMicrosoftAccount;
    private LinearLayout addLoginServer;

    private ListView externalServerList;
    private ListView accountList;

    private ArrayList<Account> accounts;
    private AccountListAdapter accountListAdapter;

    public AccountUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        accountUI = activity.findViewById(R.id.ui_account);

        addOfflineAccount = activity.findViewById(R.id.add_offline_account);
        addMojangAccount = activity.findViewById(R.id.add_mojang_account);
        addMicrosoftAccount = activity.findViewById(R.id.add_microsoft_account);
        addLoginServer = activity.findViewById(R.id.add_login_server);

        addOfflineAccount.setOnClickListener(this);
        addMojangAccount.setOnClickListener(this);
        addMicrosoftAccount.setOnClickListener(this);
        addLoginServer.setOnClickListener(this);

        externalServerList = activity.findViewById(R.id.external_server_list);
        accountList = activity.findViewById(R.id.account_list);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.account_ui_title),false,false);
        CustomAnimationUtils.showViewFromLeft(accountUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(accountUI,activity,context,true);
    }

    private void init(){
        accounts = InitializeSetting.initializeAccounts(context);
        accountListAdapter = new AccountListAdapter(context,accounts);
        accountList.setAdapter(accountListAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == addOfflineAccount){
            AddOfflineAccountDialog addOfflineAccountDialog = new AddOfflineAccountDialog(context);
            addOfflineAccountDialog.show();
        }
        if (v == addMojangAccount){
            AddMojangAccountDialog addMojangAccountDialog = new AddMojangAccountDialog(context);
            addMojangAccountDialog.show();
        }
        if (v == addMicrosoftAccount){
            AddMicrosoftAccountDialog addMicrosoftAccountDialog = new AddMicrosoftAccountDialog(context);
            addMicrosoftAccountDialog.show();
        }
        if (v == addLoginServer){
            AddVerifyServerDialog addVerifyServerDialog = new AddVerifyServerDialog(context);
            addVerifyServerDialog.show();
        }
    }
}
