package com.tungsten.hmclpe.launcher.list.local.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tungsten.hmclpe.launcher.dialogs.control.ControllerManagerDialog;

import java.util.ArrayList;

public class ControlPatternListAdapter extends BaseAdapter {

    private Context context;
    private ControllerManagerDialog dialog;
    private ArrayList<ControlPattern> list;

    public ControlPatternListAdapter(Context context, ControllerManagerDialog dialog, ArrayList<ControlPattern> list){
        this.context = context;
        this.dialog = dialog;
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
        return view;
    }
}
