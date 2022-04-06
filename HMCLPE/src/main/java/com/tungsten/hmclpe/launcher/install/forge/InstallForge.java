package com.tungsten.hmclpe.launcher.install.forge;

import android.content.Context;
import android.os.Handler;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.leo618.zip.IZipCallback;
import com.leo618.zip.ZipManager;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.minecraft.forge.ForgeVersion;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListAdapter;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.file.FileUtils;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.DownloadUtil;
import com.tungsten.hmclpe.utils.io.SSLSocketClient;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InstallForge {

    private Context context;
    private MainActivity activity;
    private DownloadTaskListAdapter adapter;
    private ForgeVersion version;
    private String name;
    private InstallForgeCallback callback;

    private DownloadTaskListBean bean;

    private String universalName;

    private Handler handler = new Handler();

    public InstallForge (Context context, MainActivity activity, DownloadTaskListAdapter adapter, ForgeVersion version,String name,InstallForgeCallback callback) {
        this.context = context;
        this.activity = activity;
        this.adapter = adapter;
        this.version = version;
        this.name = name;
        this.callback = callback;
    }

    public void install() {
        bean = new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_forge),"","");
        adapter.addDownloadTask(bean);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = getInstallerUrl();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (FileUtils.deleteDirectory(AppManifest.INSTALL_DIR + "/forge")) {
                            DownloadUtil.downloadSingleFile(context, new DownloadTaskListBean("forge-installer.jar", url, AppManifest.INSTALL_DIR + "/forge/forge-installer.jar"), new DownloadTask.Feedback() {
                                @Override
                                public void addTask(DownloadTaskListBean bean) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.addDownloadTask(bean);
                                        }
                                    });
                                }

                                @Override
                                public void updateProgress(DownloadTaskListBean bean) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.onProgress(bean);
                                        }
                                    });
                                }

                                @Override
                                public void updateSpeed(String speed) {

                                }

                                @Override
                                public void removeTask(DownloadTaskListBean bean) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.onComplete(bean);
                                        }
                                    });
                                }

                                @Override
                                public void onFinished(Map<String, String> failedFile) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            unZipForgeInstaller();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled() {

                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    public String getInstallerUrl() {
        try {
            String baseUrl = "https://bmclapi2.bangbang93.com/forge/download/" + version.getBuild();
            Request request = new Request.Builder().url(baseUrl).build();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),(X509TrustManager) SSLSocketClient.getTrustManager()[0]);
            builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);
            builder.followRedirects(false);
            builder.followSslRedirects(true);
            OkHttpClient okHttpClient = builder.build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            String redirectUrl = response.headers().get("Location").replace("/maven","");
            String base;
            if (DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource) == 0) {
                base = "https://maven.minecraftforge.net";
            }
            else if (DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource) == 1) {
                base = "https://bmclapi2.bangbang93.com/maven";
            }
            else {
                base = "https://download.mcbbs.net/maven";
            }
            return base + redirectUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void unZipForgeInstaller() {
        ZipManager.unzip(AppManifest.INSTALL_DIR + "/forge/forge-installer.jar", AppManifest.INSTALL_DIR + "/forge/installer", new IZipCallback() {
            @Override
            public void onStart() {
                try {
                    FileUtils.createFile(AppManifest.INSTALL_DIR + "/forge/temp.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(int percentDone) {

            }

            @Override
            public void onFinish(boolean success) {
                if (success) {
                    downloadLibs(isNewInstaller());
                }
            }
        });
    }

    public boolean isNewInstaller() {
        return new File(AppManifest.INSTALL_DIR + "/forge/installer/version.json").exists();
    }

    public void downloadLibs(boolean isNew) {
        Gson gson = JsonUtils.defaultGsonBuilder()
                .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                .registerTypeAdapter(Bits.class, new Bits.Serializer())
                .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                .create();
        if (isNew) {
            String vString = FileStringUtils.getStringFromFile(AppManifest.INSTALL_DIR + "/forge/installer/version.json");
            String iString = FileStringUtils.getStringFromFile(AppManifest.INSTALL_DIR + "/forge/installer/install_profile.json");
            Version versionJson = gson.fromJson(vString,Version.class);
            ForgeNewInstallProfile installProfile = gson.fromJson(iString,ForgeNewInstallProfile.class);
        }
        else {
            String iString = FileStringUtils.getStringFromFile(AppManifest.INSTALL_DIR + "/forge/installer/install_profile.json");
            ForgeInstallProfile installProfile = gson.fromJson(iString,ForgeInstallProfile.class);
            Version patch = installProfile.getVersionInfo();
            ArrayList<DownloadTaskListBean> list = new ArrayList<>();
            for (Library library : patch.getLibraries()){
                if (!library.getPath().equals(installProfile.getInstall().getPath().getPath())) {
                    DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                            DownloadUrlSource.getSubUrl(DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource),DownloadUrlSource.FORGE_LIBRARIES) + "/" + library.getPath(),
                            activity.launcherSetting.gameFileDirectory + "/libraries/" +library.getPath());
                    list.add(bean);
                }
            }
            startDownloadTask(list, () -> {
                installOldForge(installProfile.getInstall(),patch);
            });
        }
    }

    public void installOldForge(ForgeInstall install,Version patch) {
        universalName = install.getFilePath();
        FileUtils.createDirectory(new File(activity.launcherSetting.gameFileDirectory + "/libraries/" + install.getPath().getPath()).getParent());
        if (FileUtils.copyFile(AppManifest.INSTALL_DIR + "/forge/installer/" + universalName,activity.launcherSetting.gameFileDirectory + "/libraries/" + install.getPath().getPath())) {
            adapter.onComplete(bean);
            callback.onFinish(true,patch.setId("forge").setVersion(version.getVersion()).setPriority(30000));
        }
        else {
            callback.onFinish(false,null);
        }
    }

    public void installNewForge() {

    }

    public void startDownloadTask(ArrayList<DownloadTaskListBean> tasks,OnDownloadFinishListener onDownloadFinishListener) {
        ArrayMap<String,String> map = new ArrayMap<>();
        for (DownloadTaskListBean bean : tasks){
            map.put(bean.url,bean.path);
        }
        DownloadTask downloadTask = new DownloadTask(context, new DownloadTask.Feedback() {
            @Override
            public void addTask(DownloadTaskListBean bean) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addDownloadTask(bean);
                    }
                });
            }

            @Override
            public void updateProgress(DownloadTaskListBean bean) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.onProgress(bean);
                    }
                });
            }

            @Override
            public void updateSpeed(String speed) {

            }

            @Override
            public void removeTask(DownloadTaskListBean bean) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.onComplete(bean);
                    }
                });
            }

            @Override
            public void onFinished(Map<String, String> failedFile) {
                onDownloadFinishListener.onFinish();
            }

            @Override
            public void onCancelled() {

            }
        });
        int maxDownloadTask = activity.launcherSetting.maxDownloadTask;
        if (activity.launcherSetting.autoDownloadTaskQuantity) {
            maxDownloadTask = 64;
        }
        downloadTask.setMaxTask(maxDownloadTask);
        downloadTask.execute(new Map[]{map});
    }

    public interface InstallForgeCallback{
        void onFinish(boolean success, Version patch);
    }

    public interface OnDownloadFinishListener{
        void onFinish();
    }

}
