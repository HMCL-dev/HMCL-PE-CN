package com.tungsten.hmclpe.launcher.list.download.minecraft.forge;

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
import com.tungsten.hmclpe.launcher.download.forge.ForgeVersion;
import com.tungsten.hmclpe.utils.string.StringUtils;

import java.util.ArrayList;

public class DownloadForgeListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<ForgeVersion> versions;

    public DownloadForgeListAdapter(Context context,MainActivity activity,ArrayList<ForgeVersion> versions){
        this.context = context;
        this.activity = activity;
        this.versions = versions;
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

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView forgeId;
        TextView mcVersion;
        TextView releaseTime;
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
            viewHolder.forgeId = view.findViewById(R.id.id);
            viewHolder.mcVersion = view.findViewById(R.id.type);
            viewHolder.releaseTime = view.findViewById(R.id.release_time);
            activity.exteriorConfig.apply(viewHolder.mcVersion);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        ForgeVersion version = versions.get(i);
        viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_forge));
        viewHolder.forgeId.setText(version.getVersion());
        viewHolder.mcVersion.setText(version.getGameVersion());
        viewHolder.releaseTime.setText(getReleaseTime(version.getModified()));
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.uiManager.installGameUI.forgeVersion = version;
                activity.backToLastUI();
            }
        });
        return view;
    }
}
