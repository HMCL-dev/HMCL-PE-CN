package com.tungsten.hmclpe.launcher.list.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.account.OfflineAccountSkinDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.util.ArrayList;

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
        TextView name;
        TextView type;
        ImageButton refresh;
        ImageButton skin;
        ImageButton delete;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account,null);
            viewHolder.check = convertView.findViewById(R.id.select_account);
            viewHolder.face = convertView.findViewById(R.id.skin_face);
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
        if (account.loginType == 1){
            viewHolder.name.setText(account.auth_player_name);
            viewHolder.type.setText(context.getString(R.string.item_account_type_offline));
        }
        if (account.loginType == 2){

        }
        if (account.loginType == 3){

        }
        if (account.loginType == 4){

        }
        viewHolder.check.setChecked(account.email.equals(activity.publicGameSetting.account.email) && account.auth_player_name.equals(activity.publicGameSetting.account.auth_player_name) && account.auth_uuid.equals(activity.publicGameSetting.account.auth_uuid) && account.loginServer.equals(activity.publicGameSetting.account.loginServer));
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
                    OfflineAccountSkinDialog offlineAccountSkinDialog = new OfflineAccountSkinDialog(context,activity,account);
                    offlineAccountSkinDialog.show();
                }
                else {

                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
