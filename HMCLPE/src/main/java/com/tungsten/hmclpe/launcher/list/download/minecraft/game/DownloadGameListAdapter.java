package com.tungsten.hmclpe.launcher.list.download.minecraft.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.forge.ForgeVersion;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.utils.string.StringUtils;

import java.util.ArrayList;

public class DownloadGameListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<VersionManifest.Version> versions;

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView mcId;
        TextView type;
        TextView releaseTime;
    }

    private String getType(String type){
        if (type.equals("release")){
            return context.getString(R.string.download_minecraft_ui_release);
        }
        else if (type.equals("snapshot")){
            return context.getString(R.string.download_minecraft_ui_snapshot);
        }
        else {
            return context.getString(R.string.download_minecraft_ui_old);
        }
    }

    private String getReleaseTime(String time){
        int p1 = time.indexOf("-",0);
        String str1 = StringUtils.substringBefore(time,"-") + " " + context.getString(R.string.download_minecraft_item_year) + " " + time.substring(p1 + 1);
        int p2 = str1.indexOf("-",0);
        String str2 = StringUtils.substringBefore(str1,"-") + " " + context.getString(R.string.download_minecraft_item_month) + " " + str1.substring(p2 + 1);
        int p3 = str2.indexOf("T",0);
        String str3 = StringUtils.substringBefore(str2,"T") + " " + context.getString(R.string.download_minecraft_item_day) + " " + str2.substring(p3 + 1);
        return StringUtils.substringBefore(str3,"+");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getIcon(String type){
        if (type.equals("release")){
            return context.getDrawable(R.drawable.ic_grass);
        }
        else if (type.equals("snapshot")){
            return context.getDrawable(R.drawable.ic_command);
        }
        else {
            return context.getDrawable(R.drawable.ic_craft_table);
        }
    }

    public DownloadGameListAdapter(Context context, MainActivity activity, ArrayList<VersionManifest.Version> versions){
        this.context = context;
        this.activity = activity;
        this.versions = versions;
    }

    @Override
    public int getCount() {
        return versions.size();
    }

    @Override
    public Object getItem(int position) {
        return versions.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_game_list,null);
            viewHolder.item = convertView.findViewById(R.id.item);
            viewHolder.icon = convertView.findViewById(R.id.icon);
            viewHolder.mcId = convertView.findViewById(R.id.id);
            viewHolder.type = convertView.findViewById(R.id.type);
            viewHolder.releaseTime = convertView.findViewById(R.id.release_time);
            activity.exteriorConfig.apply(viewHolder.type);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        VersionManifest.Version version = versions.get(position);
        viewHolder.icon.setImageDrawable(getIcon(version.type));
        viewHolder.mcId.setText(version.id);
        viewHolder.type.setText(getType(version.type));
        viewHolder.releaseTime.setText(getReleaseTime(version.releaseTime));
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.uiManager.installGameUI.name = version.id;
                activity.uiManager.installGameUI.fabricVersion = null;
                activity.uiManager.installGameUI.fabricAPIVersion = null;
                activity.uiManager.installGameUI.forgeVersion = new ForgeVersion();
                activity.uiManager.installGameUI.optifineVersion = null;
                activity.uiManager.installGameUI.liteLoaderVersion = null;
                activity.uiManager.installGameUI.version = version;
                activity.uiManager.switchMainUI(activity.uiManager.installGameUI);
            }
        });
        return convertView;
    }
}
