package com.tungsten.hmclpe.launcher.list.download;

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
import com.tungsten.hmclpe.launcher.uis.game.download.right.resource.DownloadResourceUI;
import com.tungsten.hmclpe.utils.string.ModTranslations;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ModDependencyAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private List<ModListBean.Mod> list;

    public ModDependencyAdapter (Context context,MainActivity activity,List<ModListBean.Mod> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView name;
        TextView categories;
        TextView introduction;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_download_mod_dependency,null);
            viewHolder.item = view.findViewById(R.id.item);
            viewHolder.icon = view.findViewById(R.id.mod_icon);
            viewHolder.name = view.findViewById(R.id.mod_name);
            viewHolder.categories = view.findViewById(R.id.mod_categories);
            viewHolder.introduction = view.findViewById(R.id.mod_introduction);
            activity.exteriorConfig.apply(viewHolder.categories);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.icon.setTag(i);
        new Thread(() -> {
            try {
                URL url = new URL(list.get(i).getIconUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap icon = BitmapFactory.decodeStream(inputStream);
                if (viewHolder.icon.getTag().equals(i)){
                    handler.post(() -> viewHolder.icon.setImageBitmap(icon));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        String categories = "";
        for (int j = 0;j < list.get(i).getCategories().size();j++){
            //categories = categories + SearchTools.getCategoryFromID(context,modList.get(i).getCategories().get(j)) + "  ";
        }
        viewHolder.categories.setText(categories);
        viewHolder.name.setText(ModTranslations.getModBySlug(list.get(i).getSlug()) == null ? list.get(i).getTitle() : ModTranslations.getModBySlug(list.get(i).getSlug()).getDisplayName());
        viewHolder.introduction.setText(list.get(i).getDescription());
        viewHolder.item.setOnClickListener(view1 -> {
            DownloadResourceUI downloadResourceUI = new DownloadResourceUI(context,activity,list.get(i),true);
            activity.uiManager.switchMainUI(downloadResourceUI);
        });
        return view;
    }

    @SuppressLint("HandlerLeak")
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

}
