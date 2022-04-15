package com.tungsten.hmclpe.launcher.list.download.mod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import androidx.annotation.RequiresApi;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.utils.string.ModTranslations;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadModListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<ModListBean.Mod> modList;

    private class ViewHolder{
        LinearLayout modItem;
        ImageView modIcon;
        TextView modName;
        TextView modCategories;
        TextView modIntroduction;
    }

    public DownloadModListAdapter (Context context, MainActivity activity, ArrayList<ModListBean.Mod> modList){
        this.context = context;
        this.activity = activity;
        this.modList = modList;
    }

    @Override
    public int getCount() {
        return modList.size();
    }

    @Override
    public Object getItem(int position) {
        return modList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_mod_list,null);
            viewHolder.modItem = convertView.findViewById(R.id.item);
            viewHolder.modIcon = convertView.findViewById(R.id.mod_icon);
            viewHolder.modName = convertView.findViewById(R.id.mod_name);
            viewHolder.modCategories = convertView.findViewById(R.id.mod_categories);
            viewHolder.modIntroduction = convertView.findViewById(R.id.mod_introduction);
            activity.exteriorConfig.apply(viewHolder.modCategories);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.modIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.modIcon.setTag(position);
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(modList.get(position).getIconUrl());
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap icon = BitmapFactory.decodeStream(inputStream);
                    if (viewHolder.modIcon.getTag().equals(position)){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.modIcon.setImageBitmap(icon);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        String categories = "";
        for (int i = 0;i < modList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,modList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.modCategories.setText(categories);
        viewHolder.modName.setText(ModTranslations.getDisplayName(modList.get(position).getTitle(),modList.get(position).getSlug()));
        viewHolder.modIntroduction.setText(modList.get(position).getDescription());
        viewHolder.modItem.setOnClickListener(new View.OnClickListener() {
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
