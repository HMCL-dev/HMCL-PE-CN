package com.tungsten.hmclpe.launcher.uis.account;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.account.AddMicrosoftAccountDialog;
import com.tungsten.hmclpe.launcher.dialogs.account.AddMojangAccountDialog;
import com.tungsten.hmclpe.launcher.dialogs.account.AddOfflineAccountDialog;
import com.tungsten.hmclpe.launcher.dialogs.account.AddVerifyServerDialog;
import com.tungsten.hmclpe.launcher.list.account.AccountListAdapter;
import com.tungsten.hmclpe.launcher.list.account.server.AuthlibInjectorServerListAdapter;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.InitializeSetting;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.util.ArrayList;

public class AccountUI extends BaseUI implements View.OnClickListener {

    public LinearLayout accountUI;

    private LinearLayout addOfflineAccount;
    private LinearLayout addMojangAccount;
    private LinearLayout addMicrosoftAccount;
    private LinearLayout addLoginServer;

    private ListView externalServerList;
    private ListView accountList;

    public ArrayList<Account> accounts;
    public AccountListAdapter accountListAdapter;

    public ArrayList<AuthlibInjectorServer> serverList;
    public AuthlibInjectorServerListAdapter serverListAdapter;

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
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.account_ui_title),activity.uiManager.uis.get(activity.uiManager.uis.size() - 2) != activity.uiManager.mainUI,false);
        CustomAnimationUtils.showViewFromLeft(accountUI,activity,context,true);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(accountUI,activity,context,true);
    }

    private void init(){
        accounts = InitializeSetting.initializeAccounts(context);
        accountListAdapter = new AccountListAdapter(context,activity,accounts);
        accountList.setAdapter(accountListAdapter);

        serverList = InitializeSetting.initializeAuthlibInjectorServer(context);
        serverListAdapter = new AuthlibInjectorServerListAdapter(context,activity,serverList);
        externalServerList.setAdapter(serverListAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == addOfflineAccount){
            AddOfflineAccountDialog addOfflineAccountDialog = new AddOfflineAccountDialog(context, accounts, new AddOfflineAccountDialog.OnOfflineAccountAddListener() {
                @Override
                public void onPositive(Account account) {
                    activity.publicGameSetting.account = account;
                    GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
                    accounts.add(account);
                    accountListAdapter.notifyDataSetChanged();
                    GsonUtils.saveAccounts(accounts,AppManifest.ACCOUNT_DIR + "/accounts.json");
                }
            });
            addOfflineAccountDialog.show();
        }
        if (v == addMojangAccount){
            AddMojangAccountDialog addMojangAccountDialog = new AddMojangAccountDialog(context, accounts, new AddMojangAccountDialog.OnMojangAccountAddListener() {
                @Override
                public void onPositive(Account account) {
                    activity.publicGameSetting.account = account;
                    GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
                    accounts.add(account);
                    accountListAdapter.notifyDataSetChanged();
                    GsonUtils.saveAccounts(accounts,AppManifest.ACCOUNT_DIR + "/accounts.json");
                }
            });
            addMojangAccountDialog.show();
        }
        if (v == addMicrosoftAccount){
            AddMicrosoftAccountDialog addMicrosoftAccountDialog = new AddMicrosoftAccountDialog(context);
            addMicrosoftAccountDialog.show();
        }
        if (v == addLoginServer){
            AddVerifyServerDialog addVerifyServerDialog = new AddVerifyServerDialog(context, new AddVerifyServerDialog.OnAuthlibInjectorServerAddListener() {
                @Override
                public void onServerAdd(AuthlibInjectorServer authlibInjectorServer) {
                    if (!serverList.contains(authlibInjectorServer)){
                        serverList.add(authlibInjectorServer);
                        serverListAdapter.notifyDataSetChanged();
                        GsonUtils.saveServer(serverList,AppManifest.ACCOUNT_DIR + "/authlib_injector_server.json");
                    }
                }
            });
            addVerifyServerDialog.show();
        }
    }
}
