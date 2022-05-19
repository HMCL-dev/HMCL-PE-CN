package com.tungsten.hmclpe.launcher.view.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.tungsten.hmclpe.R;

import java.util.ArrayList;

public class MRCSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public MRCSpinnerAdapter (Context context,ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    private class ViewHolder{
        CheckedTextView checkedTextView;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_spinner_drop_down,null);
            viewHolder.checkedTextView = view.findViewById(R.id.checkedTextViewCustom);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String c;
        int resId = context.getResources().getIdentifier("modrinth_category_" + list.get(i),"string","com.tungsten.hmclpe");
        if (resId != 0 && context.getString(resId) != null) {
            c = context.getString(resId);
        }
        else {
            c = list.get(i);
        }
        viewHolder.checkedTextView.setText(c);
        return view;
    }
}
