package com.tungsten.hmclpe.launcher.list.account.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.account.AddAuthlibInjectorAccountDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class AuthlibInjectorServerListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<AuthlibInjectorServer> list;

    private class ViewHolder{
        LinearLayout add;
        TextView name;
        TextView url;
        ImageButton delete;
    }

    private String getSimplifiedUrl(String url){
        int index1 = StringUtils.ordinalIndexOf(url,"/",2);
        int index2 = StringUtils.ordinalIndexOf(url,"/", 3);
        return url.substring(index1 + 1,index2).split(":")[0];
    }

    public AuthlibInjectorServerListAdapter(Context context,MainActivity activity,ArrayList<AuthlibInjectorServer> list){
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_authlib_injector_server,null);
            viewHolder.add = convertView.findViewById(R.id.add_authlib_injector_account);
            viewHolder.name = convertView.findViewById(R.id.server_name);
            viewHolder.url = convertView.findViewById(R.id.server_url);
            viewHolder.delete = convertView.findViewById(R.id.delete_server);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        AuthlibInjectorServer authlibInjectorServer = list.get(position);
        viewHolder.name.setText(authlibInjectorServer.getName());
        viewHolder.url.setText(getSimplifiedUrl(authlibInjectorServer.getUrl()));
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAuthlibInjectorAccountDialog addAuthlibInjectorAccountDialog = new AddAuthlibInjectorAccountDialog(context, activity, activity.uiManager.accountUI.accounts, new AddAuthlibInjectorAccountDialog.OnAuthlibInjectorAccountAddListener() {
                    @Override
                    public void onAccountAdd(Account account) {
                        activity.publicGameSetting.account = account;
                        GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
                        activity.uiManager.accountUI.accounts.add(account);
                        activity.uiManager.accountUI.accountListAdapter.notifyDataSetChanged();
                        GsonUtils.saveAccounts(activity.uiManager.accountUI.accounts,AppManifest.ACCOUNT_DIR + "/accounts.json");
                    }
                },list,authlibInjectorServer);
                addAuthlibInjectorAccountDialog.show();
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                activity.uiManager.accountUI.serverList.remove(authlibInjectorServer);
                activity.uiManager.accountUI.serverListAdapter.notifyDataSetChanged();
                GsonUtils.saveServer(activity.uiManager.accountUI.serverList, AppManifest.ACCOUNT_DIR + "/authlib_injector_server.json");
                for (Account account : activity.uiManager.accountUI.accounts){
                    if (account.loginServer.equals(authlibInjectorServer.getUrl())){
                        boolean isSelected = account.email.equals(activity.publicGameSetting.account.email) && account.auth_player_name.equals(activity.publicGameSetting.account.auth_player_name) && account.auth_uuid.equals(activity.publicGameSetting.account.auth_uuid) && account.loginServer.equals(activity.publicGameSetting.account.loginServer);
                        activity.uiManager.accountUI.accounts.remove(account);
                        GsonUtils.saveAccounts(activity.uiManager.accountUI.accounts,AppManifest.ACCOUNT_DIR + "/accounts.json");
                        if (activity.uiManager.accountUI.accounts.size() == 0){
                            activity.publicGameSetting.account = new Account(0,"","","","","","","","","","");
                        }
                        else if (isSelected){
                            activity.publicGameSetting.account = activity.uiManager.accountUI.accounts.get(0);
                        }
                        GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
                        activity.uiManager.accountUI.accountListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return convertView;
    }
}
