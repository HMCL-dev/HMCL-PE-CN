package com.tungsten.hmclpe.launcher.list.download.minecraft.liteloader;

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
import com.tungsten.hmclpe.launcher.download.liteloader.LiteLoaderVersion;

import java.util.ArrayList;

public class DownloadLiteLoaderListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private String mcVersion;
    private ArrayList<LiteLoaderVersion> versions;
    private boolean install;

    public DownloadLiteLoaderListAdapter(Context context,MainActivity activity,String mcVersion,ArrayList<LiteLoaderVersion> versions,boolean install){
        this.context = context;
        this.activity = activity;
        this.mcVersion = mcVersion;
        this.versions = versions;
        this.install = install;
    }

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView liteLoaderId;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_download_game_list,null);
            viewHolder.item = view.findViewById(R.id.item);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.liteLoaderId = view.findViewById(R.id.id);
            viewHolder.mcVersion = view.findViewById(R.id.release_time);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        LiteLoaderVersion version = versions.get(i);
        viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_chicken));
        viewHolder.liteLoaderId.setText(version.getVersion());
        viewHolder.mcVersion.setText(mcVersion);
        viewHolder.item.setOnClickListener(v -> {
            if (install) {

            }
            else {
                activity.uiManager.installGameUI.liteLoaderVersion = version;
                activity.backToLastUI();
            }
        });
        return view;
    }
}
