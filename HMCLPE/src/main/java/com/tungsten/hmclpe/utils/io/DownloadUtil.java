package com.tungsten.hmclpe.utils.io;

import android.content.Context;

import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.file.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class DownloadUtil {

    public static void downloadSingleFile(Context context,DownloadTaskListBean bean, DownloadTask.Feedback feedback) {
        ArrayList<DownloadTaskListBean> list = new ArrayList<>();
        list.add(bean);
        DownloadTask downloadTask = new DownloadTask(context,feedback);
        downloadTask.execute(list);
    }

    public static boolean downloadFile(String url,String nameOutput,String sha1, DownloadTask.DownloadFeedback monitor) throws IOException {
        File nameOutputFile = new File(nameOutput);
        if (!nameOutputFile.exists()) {
            nameOutputFile.getParentFile().mkdirs();
        }
        else {
            if (!isRightFile(nameOutput,sha1)) {
                nameOutputFile.delete();
            }
        }
        URL downloadUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection)downloadUrl.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setConnectTimeout(8000);
        httpURLConnection.connect();
        InputStream inputStream = httpURLConnection.getInputStream();
        FileOutputStream fos = new FileOutputStream(nameOutputFile);
        int cur = 0;
        int oval = 0;
        long len = httpURLConnection.getContentLength();
        byte[] buf = new byte[65535];
        long lastTime = System.currentTimeMillis();
        long lastLen = 0;
        while ((cur = inputStream.read(buf)) != -1) {
            oval += cur;
            if (System.currentTimeMillis() - lastTime>=1000){
                lastLen = oval;
                lastTime = System.currentTimeMillis();
                if (monitor != null)
                    monitor.updateSpeed(formetFileSize(oval - lastLen) + "/s");
            }
            fos.write(buf, 0, cur);
            if (monitor != null)
                monitor.updateProgress(oval, len);
        }
        fos.close();
        inputStream.close();
        if (!isRightFile(nameOutput,sha1)) {
            nameOutputFile.delete();
            return false;
        }
        return true;
    }

    public static boolean isRightFile(String path,String sha1) {
        if (new File(path).exists()) {
            if (sha1 != null && !sha1.equals("")) {
                if (Objects.equals(FileUtils.getFileSha1(path), sha1)) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
