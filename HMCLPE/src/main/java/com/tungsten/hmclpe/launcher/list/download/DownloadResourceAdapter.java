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
import com.tungsten.hmclpe.launcher.mod.curse.CurseModManager;
import com.tungsten.hmclpe.launcher.uis.game.download.right.resource.DownloadResourceUI;
import com.tungsten.hmclpe.utils.string.ModTranslations;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class DownloadResourceAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<ModListBean.Mod> modList;
    private int type;

    private class ViewHolder{
        LinearLayout item;
        ImageView icon;
        TextView name;
        TextView categories;
        TextView introduction;
    }

    public DownloadResourceAdapter(Context context, MainActivity activity, ArrayList<ModListBean.Mod> modList, int type){
        this.context = context;
        this.activity = activity;
        this.modList = modList;
        this.type = type;
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_mod,null);
            viewHolder.item = convertView.findViewById(R.id.item);
            viewHolder.icon = convertView.findViewById(R.id.mod_icon);
            viewHolder.name = convertView.findViewById(R.id.mod_name);
            viewHolder.categories = convertView.findViewById(R.id.mod_categories);
            viewHolder.introduction = convertView.findViewById(R.id.mod_introduction);
            activity.exteriorConfig.apply(viewHolder.categories);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.icon.setTag(position);
        new Thread(() -> {
            try {
                URL url = new URL(modList.get(position).getIconUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap icon = BitmapFactory.decodeStream(inputStream);
                if (viewHolder.icon.getTag().equals(position)){
                    handler.post(() -> viewHolder.icon.setImageBitmap(icon));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        StringBuilder categories = new StringBuilder();
        if (modList.get(position).getModrinthCategories().size() != 0) {
            for (int i = 0;i < modList.get(position).getModrinthCategories().size();i++){
                String c;
                int resId = context.getResources().getIdentifier("modrinth_category_" + modList.get(position).getModrinthCategories().get(i),"string","com.tungsten.hmclpe");
                if (resId != 0 && context.getString(resId) != null) {
                    c = context.getString(resId);
                }
                else {
                    c = modList.get(position).getModrinthCategories().get(i);
                }
                categories.append(c).append((i != modList.get(position).getModrinthCategories().size()) ? "   " : "");
            }
        }
        else {
            for (int i = 0;i < modList.get(position).getCategories().size();i++){
                String c = "";
                int resId = context.getResources().getIdentifier("curse_category_" + modList.get(position).getCategories().get(i),"string","com.tungsten.hmclpe");
                if (resId != 0 && context.getString(resId) != null) {
                    c = context.getString(resId);
                }
                categories.append(c).append((i != modList.get(position).getCategories().size() && !c.equals("")) ? "   " : "");
            }
        }
        viewHolder.categories.setText(categories.toString());
        viewHolder.name.setText((type == 0 && ModTranslations.getModByCurseForgeId(modList.get(position).getSlug()) != null && Objects.requireNonNull(ModTranslations.getModByCurseForgeId(modList.get(position).getSlug())).getDisplayName() != null) ? Objects.requireNonNull(ModTranslations.getModByCurseForgeId(modList.get(position).getSlug())).getDisplayName() : modList.get(position).getTitle());
        viewHolder.introduction.setText(modList.get(position).getDescription());
        viewHolder.item.setOnClickListener(view -> {
            DownloadResourceUI downloadResourceUI = new DownloadResourceUI(context,activity,modList.get(position),type);
            activity.uiManager.switchMainUI(downloadResourceUI);
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
