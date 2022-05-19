package com.tungsten.hmclpe.launcher.list.local.save;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.game.World;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorldListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<World> list;

    private static class ViewHolder {
        TextView name;
        TextView info;
        ImageButton more;
    }

    public WorldListAdapter (Context context, ArrayList<World> list) {
        this.context = context;
        this.list = list;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_save,null);
            viewHolder.name = view.findViewById(R.id.save_name);
            viewHolder.info = view.findViewById(R.id.save_info);
            viewHolder.more = view.findViewById(R.id.more_vert);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        World world = list.get(i);
        viewHolder.name.setText(world.getWorldName());
        String fileName = world.getFileName();
        String lastPlayTime = DateTimeFormatter.ofPattern(context.getString(R.string.time_pattern)).withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(world.getLastPlayed()));
        String gameVersion = world.getGameVersion() == null ? context.getString(R.string.world_manager_ui_unknown_game_version) : world.getGameVersion();
        viewHolder.info.setText(context.getString(R.string.world_manager_ui_info).replace("%f",fileName).replace("%t",lastPlayTime).replace("%v",gameVersion));
        viewHolder.more.setOnClickListener(view1 -> {

        });
        return view;
    }

}
