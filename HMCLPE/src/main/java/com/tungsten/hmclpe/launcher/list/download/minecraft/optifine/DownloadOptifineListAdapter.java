package com.tungsten.hmclpe.launcher.list.download.minecraft.optifine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.optifine.OptifineVersion;

import java.util.ArrayList;

public class DownloadOptifineListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<OptifineVersion> versions;

    public DownloadOptifineListAdapter(Context context,MainActivity activity,ArrayList<OptifineVersion> versions){
        this.context = context;
        this.activity = activity;
        this.versions = versions;
    }

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView optifineId;
        TextView mcVersion;
    }

    @Override
    public int getCount() {
        return versions.size();
    }

    @Override
    public Object getItem(int i) {
        return versions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_download_game_list,null);
            viewHolder.item = view.findViewById(R.id.item);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.optifineId = view.findViewById(R.id.id);
            viewHolder.mcVersion = view.findViewById(R.id.release_time);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        OptifineVersion version = versions.get(i);
        viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_command));
        viewHolder.optifineId.setText(version.type + "_" + version.patch);
        viewHolder.mcVersion.setText(version.mcVersion);
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.uiManager.installGameUI.optifineVersion = version;
                activity.backToLastUI();
            }
        });
        return view;
    }
}
