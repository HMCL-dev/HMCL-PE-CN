package com.tungsten.hmclpe.launcher.list.download.modpack;

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
import com.tungsten.hmclpe.launcher.mod.ModListBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadPackageListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<ModListBean.Mod> packageList;

    private class ViewHolder{
        LinearLayout packageItem;
        ImageView packageIcon;
        TextView packageName;
        TextView packageCategories;
        TextView packageIntroduction;
    }

    public DownloadPackageListAdapter (Context context, MainActivity activity, ArrayList<ModListBean.Mod> packageList){
        this.context = context;
        this.activity = activity;
        this.packageList = packageList;
    }

    @Override
    public int getCount() {
        return packageList.size();
    }

    @Override
    public Object getItem(int position) {
        return packageList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_package_list,null);
            viewHolder.packageItem = convertView.findViewById(R.id.item);
            viewHolder.packageIcon = convertView.findViewById(R.id.package_icon);
            viewHolder.packageName = convertView.findViewById(R.id.package_name);
            viewHolder.packageCategories = convertView.findViewById(R.id.package_categories);
            viewHolder.packageIntroduction = convertView.findViewById(R.id.package_introduction);
            activity.exteriorConfig.apply(viewHolder.packageCategories);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.packageIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.packageIcon.setTag(position);
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(packageList.get(position).getIconUrl());
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap icon = BitmapFactory.decodeStream(inputStream);
                    if (viewHolder.packageIcon.getTag().equals(position)){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.packageIcon.setImageBitmap(icon);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        String categories = "";
        for (int i = 0;i < packageList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,worldList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.packageCategories.setText(categories);
        viewHolder.packageName.setText(packageList.get(position).getTitle());
        viewHolder.packageIntroduction.setText(packageList.get(position).getDescription());
        viewHolder.packageItem.setOnClickListener(new View.OnClickListener() {
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
