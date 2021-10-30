package com.tungsten.hmclpe.launcher.list.install;

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

    public DownloadTaskListAdapter (Context context, ArrayList<DownloadTaskListBean> list, DownloadDialog dialog){
        this.context = context;
        this.list = list;
        this.dialog = dialog;
        this.failedList = new ArrayList<>();
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
        viewHolder.name.setText(bean.name);
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
                        if (viewHolder.progressBar.getTag().equals(position)){
                            viewHolder.progressBar.setProgress((int) progress);
                        }
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        if (viewHolder.progressBar.getTag().equals(position)){
                            viewHolder.progressBar.setProgress(100);
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
