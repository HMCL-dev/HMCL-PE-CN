package com.tungsten.hmclpe.utils.io;

import android.content.Context;

import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.task.DownloadTask;

import java.util.ArrayList;

public class DownloadUtil {

    public static void downloadSingleFile(Context context,DownloadTaskListBean bean, DownloadTask.Feedback feedback) {
        ArrayList<DownloadTaskListBean> list = new ArrayList<>();
        list.add(bean);
        DownloadTask downloadTask = new DownloadTask(context,feedback);
        downloadTask.execute(list);
    }

}
