package com.tungsten.hmclpe.launcher.list.download.minecraft.game;

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
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;

import java.util.ArrayList;

public class DownloadGameListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<VersionManifest.AllVersions> versions;

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView mcId;
        TextView type;
        TextView releaseTime;
    }

    public DownloadGameListAdapter(Context context, MainActivity activity, ArrayList<VersionManifest.AllVersions> versions){
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
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        
        return convertView;
    }
}
