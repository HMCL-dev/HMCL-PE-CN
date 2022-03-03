package com.tungsten.hmclpe.launcher.list.download.world;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadWorldListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<ModListBean.Mod> worldList;

    private class ViewHolder{
        LinearLayout worldItem;
        ImageView worldIcon;
        TextView worldName;
        TextView worldCategories;
        TextView worldIntroduction;
    }

    public DownloadWorldListAdapter (Context context,MainActivity activity, ArrayList<ModListBean.Mod> worldList){
        this.context = context;
        this.activity = activity;
        this.worldList = worldList;
    }

    @Override
    public int getCount() {
        return worldList.size();
    }

    @Override
    public Object getItem(int position) {
        return worldList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_world_list,null);
            viewHolder.worldItem = convertView.findViewById(R.id.item);
            viewHolder.worldIcon = convertView.findViewById(R.id.world_icon);
            viewHolder.worldName = convertView.findViewById(R.id.world_name);
            viewHolder.worldCategories = convertView.findViewById(R.id.world_categories);
            viewHolder.worldIntroduction = convertView.findViewById(R.id.world_introduction);
            activity.exteriorConfig.apply(viewHolder.worldCategories);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.worldIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.worldIcon.setTag(position);
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(worldList.get(position).getIconUrl());
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap icon = BitmapFactory.decodeStream(inputStream);
                    if (viewHolder.worldIcon.getTag().equals(position)){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.worldIcon.setImageBitmap(icon);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        String categories = "";
        for (int i = 0;i < worldList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,worldList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.worldCategories.setText(categories);
        viewHolder.worldName.setText(worldList.get(position).getTitle());
        viewHolder.worldIntroduction.setText(worldList.get(position).getDescription());
        viewHolder.worldItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    @SuppressLint("HandlerLeak")
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
}
