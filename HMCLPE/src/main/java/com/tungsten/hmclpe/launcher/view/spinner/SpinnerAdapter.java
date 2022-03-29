package com.tungsten.hmclpe.launcher.view.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.download.resources.curse.CurseModManager;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CurseModManager.Category> list;
    private int section;

    public SpinnerAdapter (Context context, ArrayList<CurseModManager.Category> list,int section){
        this.context = context;
        this.list = list;
        this.section = section;
    }

    private class ViewHolder{
        CheckedTextView checkedTextView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_drop_down,null);
            viewHolder.checkedTextView = convertView.findViewById(R.id.checkedTextViewCustom);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if (list.get(position).getParentGameCategoryId() != section){
            viewHolder.checkedTextView.setText("    " + list.get(position).getName());
        }
        else {
            viewHolder.checkedTextView.setText(list.get(position).getName());
        }
        return convertView;
    }
}
