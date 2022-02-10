package com.tungsten.hmclpe.launcher.list.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.auth.AuthInfo;
import com.tungsten.hmclpe.auth.AuthenticationException;
import com.tungsten.hmclpe.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.hmclpe.auth.yggdrasil.GameProfile;
import com.tungsten.hmclpe.auth.yggdrasil.MojangYggdrasilProvider;
import com.tungsten.hmclpe.auth.yggdrasil.YggdrasilService;
import com.tungsten.hmclpe.auth.yggdrasil.YggdrasilSession;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.account.SkinPreviewDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.skin.draw2d.Avatar;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.util.ArrayList;
import java.util.UUID;

public class AccountListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<Account> accounts;

    public AccountListAdapter (Context context, MainActivity activity, ArrayList<Account> accounts){
        this.context = context;
        this.activity = activity;
        this.accounts = accounts;
    }

    private class ViewHolder{
        RadioButton check;
        ImageView face;
        ImageView hat;
        TextView name;
        TextView type;
        ImageButton refresh;
        ImageButton skin;
        ImageButton delete;
    }

    @SuppressLint("HandlerLeak")
    public final android.os.Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {

            }
        }
    };

    private AuthlibInjectorServer getServerFromUrl(String url){
        for (int i = 0;i < activity.uiManager.accountUI.serverList.size();i++){
            if (activity.uiManager.accountUI.serverList.get(i).getUrl().equals(url)){
                return activity.uiManager.accountUI.serverList.get(i);
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account,null);
            viewHolder.check = convertView.findViewById(R.id.select_account);
            viewHolder.face = convertView.findViewById(R.id.skin_face);
            viewHolder.hat = convertView.findViewById(R.id.skin_hat);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.type = convertView.findViewById(R.id.type);
            viewHolder.refresh = convertView.findViewById(R.id.refresh);
            viewHolder.skin = convertView.findViewById(R.id.skin);
            viewHolder.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Account account = accounts.get(position);
        boolean isSelected = account.email.equals(activity.publicGameSetting.account.email) && account.auth_player_name.equals(activity.publicGameSetting.account.auth_player_name) && account.auth_uuid.equals(activity.publicGameSetting.account.auth_uuid) && account.loginServer.equals(activity.publicGameSetting.account.loginServer);
        if (account.loginType == 1){
            viewHolder.name.setText(account.auth_player_name);
            viewHolder.type.setText(context.getString(R.string.item_account_type_offline));
            Avatar.setAvatar(account.texture, viewHolder.face, viewHolder.hat);
        }
        if (account.loginType == 2){
            viewHolder.name.setText(account.email + " - " +account.auth_player_name);
            viewHolder.type.setText(context.getString(R.string.item_account_type_mojang));
            Avatar.setAvatar(account.texture, viewHolder.face, viewHolder.hat);
        }
        if (account.loginType == 3){
            viewHolder.name.setText(account.auth_player_name);
            viewHolder.type.setText(context.getString(R.string.item_account_type_microsoft));
            Avatar.setAvatar(account.texture, viewHolder.face, viewHolder.hat);
        }
        if (account.loginType == 4){
            viewHolder.name.setText(account.email + " - " +account.auth_player_name);
            viewHolder.type.setText(context.getString(R.string.item_account_type_auth_lib) + ", " + context.getString(R.string.item_account_login_server) + " " + getServerFromUrl(account.loginServer).getName());
            Avatar.setAvatar(account.texture, viewHolder.face, viewHolder.hat);
        }
        viewHolder.check.setChecked(isSelected);
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.publicGameSetting.account = account;
                GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
                notifyDataSetChanged();
            }
        });
        viewHolder.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.loginType == 1){

                }
                if (account.loginType == 2){
                    YggdrasilService yggdrasilService = new YggdrasilService(new MojangYggdrasilProvider());
                    try {
                        YggdrasilSession yggdrasilSession = yggdrasilService.refresh(account.auth_access_token, account.auth_client_token, new GameProfile(UUID.fromString(account.auth_uuid),account.auth_player_name));
                        AuthInfo authInfo = yggdrasilSession.toAuthInfo();
                    }catch (AuthenticationException e){
                        e.printStackTrace();
                    }
                }
                if (account.loginType == 3){

                }
                if (account.loginType == 4){

                }
            }
        });
        viewHolder.skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.loginType == 1){
                    SkinPreviewDialog skinPreviewDialog = new SkinPreviewDialog(context,activity,account);
                    skinPreviewDialog.show();
                }
                else {

                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                activity.uiManager.accountUI.accounts.remove(account);
                GsonUtils.saveAccounts(activity.uiManager.accountUI.accounts,AppManifest.ACCOUNT_DIR + "/accounts.json");
                if (activity.uiManager.accountUI.accounts.size() == 0){
                    activity.publicGameSetting.account = new Account(0,"","","","","","","","","","","");
                }
                else if (isSelected){
                    activity.publicGameSetting.account = accounts.get(0);
                }
                GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
                activity.uiManager.accountUI.accountListAdapter.notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
