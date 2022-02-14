package com.tungsten.hmclpe.launcher.list.download.minecraft.fabric;

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
import com.tungsten.hmclpe.launcher.download.minecraft.fabric.FabricLoaderVersion;

import java.util.ArrayList;

public class DownloadFabricListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private String mcVersion;
    private ArrayList<FabricLoaderVersion> versions;

    public DownloadFabricListAdapter(Context context,MainActivity activity,String mcVersion,ArrayList<FabricLoaderVersion> versions){
        this.context = context;
        this.activity = activity;
        this.mcVersion = mcVersion;
        this.versions = versions;
    }

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView fabricId;
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
            viewHolder.fabricId = view.findViewById(R.id.id);
            viewHolder.mcVersion = view.findViewById(R.id.release_time);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        FabricLoaderVersion version = versions.get(i);
        viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_fabric));
        viewHolder.fabricId.setText(version.version);
        viewHolder.mcVersion.setText(mcVersion);
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.uiManager.installGameUI.fabricVersion = version;
                activity.backToLastUI();
            }
        });
        return view;
    }
}
