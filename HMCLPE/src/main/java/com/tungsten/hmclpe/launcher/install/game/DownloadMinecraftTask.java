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
            String versionJsonUrl = gameVersion.url;
            String gameFilePath = activity.launcherSetting.gameFileDirectory;
            //getting tasks
            String versionJson = NetworkUtils.doGet(NetworkUtils.toURL(versionJsonUrl));
            //FileStringUtils.writeFile(gameFilePath + "/versions/" + name + "/" + name + ".json",response);
            Gson gson = JsonUtils.defaultGsonBuilder()
                    .registerTypeAdapter(Artifact.class, new Artifact.Serializer())
                    .registerTypeAdapter(Bits.class, new Bits.Serializer())
                    .registerTypeAdapter(RuledArgument.class, new RuledArgument.Serializer())
                    .registerTypeAdapter(Argument.class, new Argument.Deserializer())
                    .create();
            version = gson.fromJson(versionJson, Version.class);
            String assetIndexJson = NetworkUtils.doGet(NetworkUtils.toURL(version.getAssetIndex().getUrl()));
            AssetIndex assetIndex = gson.fromJson(assetIndexJson,AssetIndex.class);
            //assetIndex.json
            allTaskList.add(new DownloadTaskListBean(version.getAssetIndex().id + ".json",
                    version.getAssetIndex().getUrl(),
                    gameFilePath + "/assets/indexes/" + version.getAssetIndex().id + ".json"));
            //version.jar
            allTaskList.add(new DownloadTaskListBean(dialog.name + ".jar",
                    version.getDownloadInfo().getUrl(),
                    gameFilePath + "/versions/" + dialog.name + "/" + dialog.name + ".jar"));
            //libraries
            for (Library library : version.getLibraries()){
                if (library.getDownload().getUrl() != null && !library.getDownload().getUrl().equals("")) {
                    DownloadTaskListBean bean = new DownloadTaskListBean(library.getArtifactFileName(),
                            library.getDownload().getUrl(),
                            activity.launcherSetting.gameFileDirectory + "/libraries/" +library.getPath());
                    allTaskList.add(bean);
                }
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
