package com.tungsten.hmclpe.launcher.uis.game.manager.right;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tungsten.filepicker.Constants;
import com.tungsten.filepicker.FileBrowser;
import com.tungsten.filepicker.FileChooser;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.local.mod.LocalModListAdapter;
import com.tungsten.hmclpe.launcher.mod.LocalModFile;
import com.tungsten.hmclpe.launcher.mod.ModManager;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.file.FileUtils;
import com.tungsten.hmclpe.utils.file.UriUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ModManagerUI extends BaseUI implements View.OnClickListener {

    private static final int ADD_MOD_REQUEST = 4100;

    public LinearLayout modManagerUI;

    private String versionName;
    private String modsDir;
    private ModManager modManager;

    private LinearLayout refresh;
    private LinearLayout addMod;
    private LinearLayout openFolder;
    private LinearLayout checkUpdate;
    private LinearLayout download;

    private ProgressBar progressBar;
    private ListView modList;

    private ArrayList<LocalModFile> allModList;
    private LocalModListAdapter localModListAdapter;

    public ModManagerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        modManagerUI = activity.findViewById(R.id.ui_mod_manager);

        refresh = activity.findViewById(R.id.refresh_local_mod);
        addMod = activity.findViewById(R.id.add_new_mod);
        openFolder = activity.findViewById(R.id.open_mod_folder);
        checkUpdate = activity.findViewById(R.id.check_mod_update);
        download = activity.findViewById(R.id.download_new_mod);

        progressBar = activity.findViewById(R.id.load_mod_progress);
        modList = activity.findViewById(R.id.local_mod_list);

        refresh.setOnClickListener(this);
        addMod.setOnClickListener(this);
        openFolder.setOnClickListener(this);
        checkUpdate.setOnClickListener(this);
        download.setOnClickListener(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(modManagerUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.gameManagerUI.startModManager.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(modManagerUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.gameManagerUI.startModManager.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    public void refresh(String versionName){
        this.versionName = versionName;
        PrivateGameSetting privateGameSetting;
        String settingPath = activity.launcherSetting.gameFileDirectory + "/versions/" + versionName + "/hmclpe.cfg";
        if (new File(settingPath).exists() && GsonUtils.getPrivateGameSettingFromFile(settingPath) != null && (GsonUtils.getPrivateGameSettingFromFile(settingPath).forceEnable || GsonUtils.getPrivateGameSettingFromFile(settingPath).enable)) {
            privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(settingPath);
        }
        else {
            privateGameSetting = activity.privateGameSetting;
        }
        if (privateGameSetting.gameDirSetting.type == 0){
            modsDir = activity.launcherSetting.gameFileDirectory + "/mods";
        }
        else if (privateGameSetting.gameDirSetting.type == 1){
            modsDir = activity.launcherSetting.gameFileDirectory + "/versions/" + versionName + "/mods";
        }
        else {
            modsDir = privateGameSetting.gameDirSetting.path + "/mods";
        }
        refreshList();
    }

    private void refreshList() {
        new Thread(() -> {
            activity.runOnUiThread(() -> {
                progressBar.setVisibility(View.VISIBLE);
                modList.setVisibility(View.GONE);
            });
            modManager = new ModManager(modsDir);
            allModList = new ArrayList<>();
            try {
                allModList.addAll(modManager.getMods());
            } catch (IOException e) {
                Log.e("getModsException",e.toString());
                e.printStackTrace();
            }
            localModListAdapter = new LocalModListAdapter(context,allModList);
            activity.runOnUiThread(() -> {
                modList.setAdapter(localModListAdapter);
                progressBar.setVisibility(View.GONE);
                modList.setVisibility(View.VISIBLE);
            });
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MOD_REQUEST && data != null) {
            if (resultCode == RESULT_OK) {
                ArrayList<Uri> selectedFiles  = data.getParcelableArrayListExtra(Constants.SELECTED_ITEMS);
                ArrayList<Path> list = new ArrayList<>();
                for (Uri uri : selectedFiles) {
                    String path = UriUtils.getRealPathFromUri_AboveApi19(context,uri);
                    if (path != null) {
                        list.add(new File(path).toPath());
                    }
                }
                new Thread(() -> {
                    for (Path path : list) {
                        try {
                            modManager.addMod(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.runOnUiThread(this::refreshList);
                }).start();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == refresh) {
            refreshList();
        }
        if (view == addMod) {
            Intent intent = new Intent(context, FileChooser.class);
            intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal());
            intent.putExtra(Constants.ALLOWED_FILE_EXTENSIONS, "jar;zip");
            intent.putExtra(Constants.INITIAL_DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath());
            activity.startActivityForResult(intent, ADD_MOD_REQUEST);
        }
        if (view == openFolder) {
            Intent intent = new Intent(context, FileBrowser.class);
            FileUtils.createDirectory(modsDir);
            intent.putExtra(Constants.INITIAL_DIRECTORY, modsDir);
            context.startActivity(intent);
        }
        if (view == checkUpdate) {

        }
        if (view == download) {
            activity.uiManager.switchMainUI(activity.uiManager.downloadUI);
            activity.uiManager.downloadUI.downloadUIManager.switchDownloadUI(activity.uiManager.downloadUI.downloadUIManager.downloadModUI);
        }
    }
}
