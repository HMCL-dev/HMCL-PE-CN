package com.tungsten.hmclpe.launcher.list.download.resourcepack;

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
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadResourcePackListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModListBean.Mod> resourcePackList;

    private class ViewHolder{
        LinearLayout resourcePackItem;
        ImageView resourcePackIcon;
        TextView resourcePackName;
        TextView resourcePackCategories;
        TextView resourcePackIntroduction;
    }

    public DownloadResourcePackListAdapter (Context context, ArrayList<ModListBean.Mod> resourcePackList){
        this.context = context;
        this.resourcePackList = resourcePackList;
    }

    @Override
    public int getCount() {
        return resourcePackList.size();
    }

    @Override
    public Object getItem(int position) {
        return resourcePackList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_resource_pack_list,null);
            viewHolder.resourcePackItem = convertView.findViewById(R.id.item);
            viewHolder.resourcePackIcon = convertView.findViewById(R.id.resource_pack_icon);
            viewHolder.resourcePackName = convertView.findViewById(R.id.resource_pack_name);
            viewHolder.resourcePackCategories = convertView.findViewById(R.id.resource_pack_categories);
            viewHolder.resourcePackIntroduction = convertView.findViewById(R.id.resource_pack_introduction);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.resourcePackIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.resourcePackIcon.setTag(position);
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(resourcePackList.get(position).getIconUrl());
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap icon = BitmapFactory.decodeStream(inputStream);
                    if (viewHolder.resourcePackIcon.getTag().equals(position)){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.resourcePackIcon.setImageBitmap(icon);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        String categories = "";
        for (int i = 0;i < resourcePackList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,worldList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.resourcePackCategories.setText(categories);
        viewHolder.resourcePackName.setText(resourcePackList.get(position).getTitle());
        viewHolder.resourcePackIntroduction.setText(resourcePackList.get(position).getDescription());
        viewHolder.resourcePackItem.setOnClickListener(new View.OnClickListener() {
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
