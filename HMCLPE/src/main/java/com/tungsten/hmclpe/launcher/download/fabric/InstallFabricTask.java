package com.tungsten.hmclpe.launcher.download.fabric;

import static com.tungsten.hmclpe.task.DownloadTask.downloadFileMonitored;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class InstallFabricTask extends AsyncTask<Version,Integer, Version> {

    private Context context;
    private MainActivity activity;
    private DownloadTaskListAdapter adapter;
    private String mcVersion;
    private FabricLoaderVersion fabricVersion;
    private InstallFabricCallback callback;

    private Handler handler;

    public InstallFabricTask (Context context,MainActivity activity,DownloadTaskListAdapter adapter,String mcVersion,FabricLoaderVersion fabricVersion,InstallFabricCallback callback) {
        this.context = context;
        this.activity = activity;
        this.adapter = adapter;
        this.mcVersion = mcVersion;
        this.fabricVersion = fabricVersion;
        this.callback = callback;

        handler = new Handler();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onStart();
    }

    @Override
    protected Version doInBackground(Version... rawPatch) {
        Version patch = rawPatch[0];
        ArrayList<DownloadTaskListBean> list = new ArrayList<>();
        String head;
        for (Library library : patch.getLibraries()){
            String url;
            if (DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource) == 0) {
                url = library.getDownload().getUrl();
            }
            else if (DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource) == 1) {
                head = DownloadUrlSource.getSubUrl(1,DownloadUrlSource.LIBRARIES) + "/";
                url = head + library.getPath();
            }
            else {
                head = DownloadUrlSource.getSubUrl(2,DownloadUrlSource.LIBRARIES) + "/";
                url = head + library.getPath();
            }
            DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                    url,
                    activity.launcherSetting.gameFileDirectory + "/libraries/" + library.getPath(),
                    library.getDownload().getSha1());
            list.add(bean);
        }
        int maxDownloadTask = activity.launcherSetting.maxDownloadTask;
        if (activity.launcherSetting.autoDownloadTaskQuantity) {
            maxDownloadTask = 64;
        }
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(maxDownloadTask);
        ExecutorService threadPool = new ThreadPoolExecutor(maxDownloadTask,
                maxDownloadTask,
                0,
                TimeUnit.SECONDS,
                workQueue,
                new ThreadPoolExecutor.DiscardPolicy());
        ArrayList<DownloadTaskListBean> failedFile = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            DownloadTaskListBean bean = list.get(j);
            String url = bean.url;
            String path = bean.path;
            String sha1 = bean.sha1;
            threadPool.execute(() -> {
                if (!new File(path).exists() || (new File(path).exists() && !Objects.equals(FileUtils.getFileSha1(path), sha1))) {
                    int tryTimes = 5;
                    for (int i = 0; i < tryTimes; i++) {
                        if (isCancelled()) {
                            threadPool.shutdownNow();
                            return;
                        }
                        activity.runOnUiThread(() -> {
                            adapter.addDownloadTask(bean);
                        });
                        DownloadTask.DownloadFeedback fb = new DownloadTask.DownloadFeedback() {
                            @Override
                            public void updateProgress(long curr, long max) {
                                long progress = 100 * curr / max;
                                bean.progress = (int) progress;
                                activity.runOnUiThread(() -> {
                                    adapter.onProgress(bean);
                                });
                            }

                            @Override
                            public void updateSpeed(String speed) {

                            }
                        };
                        if (downloadFileMonitored(url, path, sha1, fb)) {
                            activity.runOnUiThread(() -> {
                                adapter.onComplete(bean);
                            });
                            break;
                        }
                        else {
                            if (i == tryTimes - 1) {
                                failedFile.add(bean);
                            }
                            activity.runOnUiThread(() -> {
                                adapter.onComplete(bean);
                            });
                        }
                    }
                }
            });
            while (!workQueue.isEmpty()) {
                ;
            }
            int progress = (j + 1) * 100 / list.size();
            onProgressUpdate(progress);
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            callback.onFailed(e);
            cancel(true);
        }
        if (failedFile.size() == 0) {
            return patch.setId("fabric").setVersion(fabricVersion.version).setPriority(30000);
        }
        else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The following files failed to download:");
            for (DownloadTaskListBean bean : failedFile) {
                stringBuilder.append("\n  " + bean.name);
            }
            Exception e = new Exception(stringBuilder.toString());
            e.printStackTrace();
            callback.onFailed(e);
            cancel(true);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Version version) {
        super.onPostExecute(version);
        if (version == null) {
            callback.onFailed(new Exception("Unknown error"));
        }
        else {
            callback.onFinish(version);
        }
    }

    public interface InstallFabricCallback{
        void onStart();
        void onFailed(Exception e);
        void onFinish(Version version);
    }
}
