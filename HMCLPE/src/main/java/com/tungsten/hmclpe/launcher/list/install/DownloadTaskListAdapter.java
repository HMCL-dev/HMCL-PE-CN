package com.tungsten.hmclpe.launcher.list.install;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.dialogs.install.DownloadDialog;

import java.util.ArrayList;

public class DownloadTaskListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DownloadTaskListBean> list;
    private DownloadDialog dialog;

    public DownloadTaskListAdapter (Context context, ArrayList<DownloadTaskListBean> list, DownloadDialog dialog){
        this.context = context;
        this.list = list;
        this.dialog = dialog;
    }

    private class ViewHolder{
        TextView name;
        ProgressBar progressBar;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_task,null);
            viewHolder.name = convertView.findViewById(R.id.download_task_name);
            viewHolder.progressBar = convertView.findViewById(R.id.download_task_progress);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.name.setTag(position);
        viewHolder.progressBar.setTag(position);
        final DownloadTaskListBean bean = list.get(position);
        if (viewHolder.name.getTag().equals(position)){
            viewHolder.name.setText(bean.name);
        }
        if (viewHolder.progressBar.getTag().equals(position)){
            viewHolder.progressBar.setProgress(bean.progress);
        }
        return convertView;
    }
}
