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

import java.util.ArrayList;

public class AccountListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Account> accounts;

    public AccountListAdapter (Context context, ArrayList<Account> accounts){
        this.context = context;
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
        return convertView;
    }
}
