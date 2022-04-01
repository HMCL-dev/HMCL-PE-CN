package com.tungsten.hmclpe.utils.io;

import android.content.Context;
import android.util.ArrayMap;

import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.task.DownloadTask;

public class DownloadUtil {

    public static void downloadSingleFile(Context context,DownloadTaskListBean bean, DownloadTask.Feedback feedback) {
        ArrayMap<String,String> map = new ArrayMap<>();
        map.put(bean.url,bean.path);
        DownloadTask downloadTask = new DownloadTask(context,feedback);
        downloadTask.execute(map);
    }

}
