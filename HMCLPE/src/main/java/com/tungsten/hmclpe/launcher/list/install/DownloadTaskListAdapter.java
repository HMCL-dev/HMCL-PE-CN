package com.tungsten.hmclpe.launcher.list.install;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.dialogs.install.DownloadDialog;

import java.util.ArrayList;

public class DownloadTaskListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DownloadTaskListBean> list;
    private DownloadDialog dialog;
    private ArrayList<DownloadTaskListBean> failedList;

    private TextView name;
    private ProgressBar progressBar;

    public DownloadTaskListAdapter (Context context, ArrayList<DownloadTaskListBean> list, DownloadDialog dialog){
        this.context = context;
        this.list = list;
        this.dialog = dialog;
        this.failedList = new ArrayList<>();
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

    //@SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_download_task,null);
        name = convertView.findViewById(R.id.download_task_name);
        name.setTag(position);
        progressBar = convertView.findViewById(R.id.download_task_progress);
        progressBar.setTag(position);
        final DownloadTaskListBean bean = list.get(position);
        if (name.getTag().equals(position)){
            name.setText(bean.name);
        }
        FileDownloader.setup(context);
        FileDownloader.getImpl().create(bean.url)
                .setPath(bean.path)
                .setTag(position)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        long soFar = soFarBytes;
                        long total = totalBytes;
                        long progress = 100 * soFar / total;
                        progressBar.setProgress((int) progress);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        if (progressBar.getTag().equals(position)){
                            progressBar.setProgress(100);
                        }
                        if (getCount() != 0){
                            dialog.onTaskFinished(position);
                        }
                        if (getCount() == 0){
                            dialog.onDownloadFinished(failedList);
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        failedList.add(bean);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
        return convertView;
    }
}
