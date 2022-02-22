package com.tungsten.hmclpe.launcher.list.local.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.dialogs.control.ChildManagerDialog;

import java.util.ArrayList;

public class ChildLayoutListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChildLayout> list;
    private ControlPattern controlPattern;
    private ChildManagerDialog childManagerDialog;

    public ChildLayoutListAdapter(Context context, ArrayList<ChildLayout> list,ControlPattern controlPattern,ChildManagerDialog childManagerDialog){
        this.context = context;
        this.list = list;
        this.controlPattern = controlPattern;
        this.childManagerDialog = childManagerDialog;
    }

    private class ViewHolder{
        TextView name;
        ImageButton edit;
        ImageButton delete;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_child_layout,null);
            viewHolder.name = view.findViewById(R.id.child_name);
            viewHolder.edit = view.findViewById(R.id.edit_child);
            viewHolder.delete = view.findViewById(R.id.delete_child);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ChildLayout childLayout = list.get(i);
        viewHolder.name.setText(childLayout.name);
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}
