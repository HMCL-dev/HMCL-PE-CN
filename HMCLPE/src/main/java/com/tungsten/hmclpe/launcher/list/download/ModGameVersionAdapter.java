package com.tungsten.hmclpe.launcher.list.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.mod.ModInfo;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;

import java.util.List;

public class ModGameVersionAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private ModInfo modInfo;
    private int[] layoutHeights;

    private class ViewHolder{
        LinearLayout item;
        ImageView show;
        TextView name;
        LinearLayout modListLayout;
        ListView modListView;
    }

    public ModGameVersionAdapter (Context context, ModInfo modInfo) {
        this.context = context;
        this.list = modInfo.getAllSupportedGameVersion();
        this.modInfo = modInfo;
        this.layoutHeights = new int[list.size()];
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_download_mod_game_version,null);
            viewHolder.item = view.findViewById(R.id.item);
            viewHolder.show = view.findViewById(R.id.show_game_version);
            viewHolder.name = view.findViewById(R.id.game_version);
            viewHolder.modListLayout = view.findViewById(R.id.mod_list_layout);
            viewHolder.modListView = view.findViewById(R.id.mod_list);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(list.get(i));
        ModVersionAdapter modVersionAdapter = new ModVersionAdapter(context,modInfo.getVersionByGameVersion(list.get(i)));
        viewHolder.modListView.setAdapter(modVersionAdapter);
        viewHolder.modListView.post(() -> {
            layoutHeights[i] = viewHolder.modListLayout.getHeight();
            viewHolder.modListLayout.setVisibility(View.GONE);
        });
        viewHolder.item.setOnClickListener(view1 -> {
            HiddenAnimationUtils.newInstance(context,viewHolder.modListLayout,viewHolder.show,layoutHeights[i]).toggle();
        });
        return view;
    }

}
