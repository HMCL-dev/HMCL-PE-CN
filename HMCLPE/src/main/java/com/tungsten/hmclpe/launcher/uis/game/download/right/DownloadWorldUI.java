package com.tungsten.hmclpe.launcher.uis.game.download.right;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.download.resources.SearchTools;
import com.tungsten.hmclpe.launcher.list.download.world.DownloadWorldListAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DownloadWorldUI extends BaseUI {

    public LinearLayout downloadWorldUI;

    private boolean isSearching = false;

    private ProgressBar progressBar;

    private ListView worldListView;
    private ArrayList<ModListBean.Mod> worldList;
    private DownloadWorldListAdapter downloadWorldListAdapter;

    public DownloadWorldUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadWorldUI = activity.findViewById(R.id.ui_download_world);

        progressBar = activity.findViewById(R.id.loading_download_world_list_progress);

        worldListView = activity.findViewById(R.id.download_world_list);
        worldList = new ArrayList<>();
        downloadWorldListAdapter = new DownloadWorldListAdapter(context,worldList);
        worldListView.setAdapter(downloadWorldListAdapter);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(downloadWorldUI,activity,context,false);
        activity.uiManager.downloadUI.startDownloadWorldUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadWorldUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.downloadUI.startDownloadWorldUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    private void init(){
        search();
    }

    private void search(){
        if (!isSearching){
            new Thread() {
                @Override
                public void run() {
                    try {
                        searchHandler.sendEmptyMessage(0);
                        Stream<ModListBean.Mod> stream = SearchTools.search("", "", 0, SearchTools.SECTION_WORLD, SearchTools.DEFAULT_PAGE_OFFSET, "", 0);
                        List<ModListBean.Mod> list = stream.collect(toList());
                        worldList.clear();
                        for (int i = 0; i < list.size(); i++) {
                            worldList.add(list.get(i));
                        }
                        searchHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        else {

        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                isSearching = true;
                progressBar.setVisibility(View.VISIBLE);
                worldListView.setVisibility(View.GONE);
            }
            if (msg.what == 1) {
                downloadWorldListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                worldListView.setVisibility(View.VISIBLE);
                isSearching = false;
            }
        }
    };
}
