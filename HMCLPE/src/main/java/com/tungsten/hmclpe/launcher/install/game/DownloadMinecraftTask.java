package com.tungsten.hmclpe.launcher.install.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.install.DownloadDialog;
import com.tungsten.hmclpe.launcher.download.minecraft.game.VersionManifest;
import com.tungsten.hmclpe.launcher.game.Argument;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.AssetIndex;
import com.tungsten.hmclpe.launcher.game.AssetObject;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.RuledArgument;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;
import com.tungsten.hmclpe.utils.platform.Bits;

import java.io.IOException;
import java.util.ArrayList;

public class DownloadMinecraftTask extends AsyncTask<VersionManifest.Version, DownloadTaskListBean, ArrayList<DownloadTaskListBean>> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private MainActivity activity;
    private DownloadDialog dialog;
    private Version version;

    public DownloadMinecraftTask (Context context, MainActivity activity, DownloadDialog dialog) {
        this.context = context;
        this.activity = activity;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        dialog.downloadTaskListAdapter.addDownloadTask(new DownloadTaskListBean(context.getString(R.string.dialog_install_game_install_game),"",""));
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DownloadTaskListBean> doInBackground(VersionManifest.Version... versions) {
        ArrayList<DownloadTaskListBean> allTaskList = new ArrayList<>();
        try {
            VersionManifest.Version gameVersion = versions[0];
            String versionJsonUrl = DownloadUrlSource.getSubUrl(DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource),DownloadUrlSource.VERSION_JSON) + gameVersion.url.replace("https://launchermeta.mojang.com","");
            String gameFilePath = activity.launcherSetting.gameFileDirectory;
            //getting tasks
            String versionJson = NetworkUtils.doGet(NetworkUtils.toURL(versionJsonUrl));
            Gson gson = JsonUtils.defaultGsonBuilder()
                    .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                    .registerTypeAdapter(Bits.class, new Bits.Serializer())
                    .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                    .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                    .create();
            version = gson.fromJson(versionJson, Version.class);
            String assetIndexUrl = DownloadUrlSource.getSubUrl(DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource),DownloadUrlSource.ASSETS_INDEX_JSON) + version.getAssetIndex().getUrl().replace("https://launchermeta.mojang.com","");
            String assetIndexJson = NetworkUtils.doGet(NetworkUtils.toURL(assetIndexUrl));
            AssetIndex assetIndex = gson.fromJson(assetIndexJson,AssetIndex.class);
            //assetIndex.json
            allTaskList.add(new DownloadTaskListBean(version.getAssetIndex().id + ".json",
                    assetIndexUrl,
                    gameFilePath + "/assets/indexes/" + version.getAssetIndex().id + ".json"));
            //version.jar
            allTaskList.add(new DownloadTaskListBean(dialog.name + ".jar",
                    DownloadUrlSource.getSubUrl(DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource),DownloadUrlSource.VERSION_JAR) + version.getDownloadInfo().getUrl().replace("https://launcher.mojang.com",""),
                    gameFilePath + "/versions/" + dialog.name + "/" + dialog.name + ".jar"));
            //libraries
            for (Library library : version.getLibraries()){
                DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                        DownloadUrlSource.getSubUrl(DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource),DownloadUrlSource.LIBRARIES) + "/" + library.getPath(),
                        activity.launcherSetting.gameFileDirectory + "/libraries/" +library.getPath());
                allTaskList.add(bean);
            }
            //assets
            for (AssetObject object : assetIndex.getObjects().values()){
                DownloadTaskListBean bean = new DownloadTaskListBean(object.getHash(),
                        DownloadUrlSource.getSubUrl(DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource),DownloadUrlSource.ASSETS_OBJ) + "/" + object.getLocation(),
                        gameFilePath + "/assets/objects/" + object.getLocation());
                allTaskList.add(bean);
            }
            return allTaskList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(DownloadTaskListBean... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<DownloadTaskListBean> downloadTaskListBeans) {
        super.onPostExecute(downloadTaskListBeans);
        dialog.gameVersionJson = version.addPatch(version.setId("game").setVersion(dialog.version.id).setPriority(0));
        dialog.downloadMinecraft(downloadTaskListBeans);
    }

}
