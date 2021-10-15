package com.tungsten.hmclpe.launcher.uis.game.version;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.info.contents.ContentListAdapter;
import com.tungsten.hmclpe.launcher.list.info.contents.ContentListBean;
import com.tungsten.hmclpe.launcher.list.local.game.GameListAdapter;
import com.tungsten.hmclpe.launcher.list.local.game.GameListBean;
import com.tungsten.hmclpe.launcher.setting.InitializeSetting;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.util.ArrayList;

public class VersionListUI extends BaseUI implements View.OnClickListener {

    public LinearLayout versionListUI;

    private ListView gameDirList;
    private ListView versionList;

    public ArrayList<ContentListBean> contentList;
    public ArrayList<GameListBean> gameList;

    private ContentListAdapter contentListAdapter;
    private GameListAdapter gameListAdapter;

    private LinearLayout startAddGameDirUI;
    private LinearLayout startDownloadMcUI;
    private LinearLayout startInstallPackageUI;
    private LinearLayout refresh;
    private LinearLayout startGlobalSettingUI;

    private TextView startDownloadMcUIText;

    public VersionListUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        versionListUI = activity.findViewById(R.id.ui_version_list);

        gameDirList = activity.findViewById(R.id.game_file_directory_list);
        versionList = activity.findViewById(R.id.local_version_list);

        startDownloadMcUIText = activity.findViewById(R.id.start_download_mc_ui);
        startDownloadMcUIText.setOnClickListener(this);

        startAddGameDirUI = activity.findViewById(R.id.start_add_game_directory_ui);
        startAddGameDirUI.setOnClickListener(this);
        startDownloadMcUI = activity.findViewById(R.id.start_ui_download_minecraft);
        startDownloadMcUI.setOnClickListener(this);
        refresh = activity.findViewById(R.id.refresh_local_version_list);
        refresh.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.version_list_ui_title),false,false);
        CustomAnimationUtils.showViewFromLeft(versionListUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(versionListUI,activity,context,true);
    }

    @Override
    public void onClick(View v) {
        if (v == startDownloadMcUIText || v == startDownloadMcUI){
            activity.uiManager.switchMainUI(activity.uiManager.downloadUI);
            activity.uiManager.downloadUI.downloadUIManager.switchDownloadUI(activity.uiManager.downloadUI.downloadUIManager.downloadMinecraftUI);
        }
        if (v == startAddGameDirUI){
            activity.uiManager.switchMainUI(activity.uiManager.addGameDirectoryUI);
        }
        if (v == refresh){
            refreshVersionList();
        }
    }

    private void init(){
        contentList = InitializeSetting.initializeContents(context);
        contentListAdapter = new ContentListAdapter(context,activity,contentList);
        gameDirList.setAdapter(contentListAdapter);
        refreshVersionList();
    }

    public void refreshVersionList(){
        gameList = SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory);
        gameListAdapter = new GameListAdapter(context,activity,gameList);
        versionList.setAdapter(gameListAdapter);
        if (gameList.size() != 0){
            startDownloadMcUIText.setVisibility(View.GONE);
            versionList.setVisibility(View.VISIBLE);
        }
        else {
            startDownloadMcUIText.setVisibility(View.VISIBLE);
            versionList.setVisibility(View.GONE);
        }
    }
}
