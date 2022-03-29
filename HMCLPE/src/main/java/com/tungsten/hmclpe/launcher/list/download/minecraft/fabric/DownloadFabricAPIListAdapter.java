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
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.utils.string.StringUtils;

import java.util.ArrayList;

public class DownloadFabricAPIListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private String mcVersion;
    private ArrayList<ModListBean.Version> versions;

    public DownloadFabricAPIListAdapter(Context context,MainActivity activity,String mcVersion,ArrayList<ModListBean.Version> versions){
        this.context = context;
        this.activity = activity;
        this.mcVersion = mcVersion;
        this.versions = versions;
    }

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView fabricAPIId;
        TextView mcVersion;
        TextView time;
    }

    private String getReleaseTime(String time){
        int p1 = time.indexOf("-",0);
        String str1 = StringUtils.substringBefore(time,"-") + " " + context.getString(R.string.download_minecraft_item_year) + " " + time.substring(p1 + 1);
        int p2 = str1.indexOf("-",0);
        String str2 = StringUtils.substringBefore(str1,"-") + " " + context.getString(R.string.download_minecraft_item_month) + " " + str1.substring(p2 + 1);
        int p3 = str2.indexOf("T",0);
        String str3 = StringUtils.substringBefore(str2,"T") + " " + context.getString(R.string.download_minecraft_item_day) + " " + str2.substring(p3 + 1);
        return StringUtils.substringBefore(str3,".");
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

    @SuppressLint({"UseCompatLoadingForDrawables"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_download_game_list,null);
            viewHolder.item = view.findViewById(R.id.item);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.fabricAPIId = view.findViewById(R.id.id);
            viewHolder.mcVersion = view.findViewById(R.id.type);
            viewHolder.time = view.findViewById(R.id.release_time);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        ModListBean.Version version = versions.get(i);
        viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_fabric));
        viewHolder.fabricAPIId.setText(version.getVersion());
        viewHolder.mcVersion.setText(mcVersion);
        viewHolder.time.setText(getReleaseTime(version.getDatePublished().toString()));
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.uiManager.installGameUI.fabricAPIVersion = version;
                activity.backToLastUI();
            }
        });
        return view;
    }
}
