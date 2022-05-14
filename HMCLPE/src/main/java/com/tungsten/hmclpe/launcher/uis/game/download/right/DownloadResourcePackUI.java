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
import com.tungsten.hmclpe.launcher.list.download.DownloadResourceAdapter;
import com.tungsten.hmclpe.launcher.mod.SearchTools;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DownloadResourcePackUI extends BaseUI {

    public LinearLayout downloadResourcePackUI;

    private boolean isSearching = false;

    private ProgressBar progressBar;

    private ListView resourcePackListView;
    private ArrayList<ModListBean.Mod> resourcePackList;
    private DownloadResourceAdapter downloadResourcePackListAdapter;

    public DownloadResourcePackUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadResourcePackUI = activity.findViewById(R.id.ui_download_resource_pack);

        progressBar = activity.findViewById(R.id.loading_download_resource_pack_list_progress);

        resourcePackListView = activity.findViewById(R.id.download_resource_pack_list);
        resourcePackList = new ArrayList<>();
        downloadResourcePackListAdapter = new DownloadResourceAdapter(context,activity,resourcePackList,false);
        resourcePackListView.setAdapter(downloadResourcePackListAdapter);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(downloadResourcePackUI,activity,context,false);
        activity.uiManager.downloadUI.startDownloadResourcePackUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadResourcePackUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.downloadUI.startDownloadResourcePackUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    private void init(){
        search();
    }

    private void search(){
        if (!isSearching){
            new Thread(() -> {
                try {
                    searchHandler.sendEmptyMessage(0);
                    Stream<ModListBean.Mod> stream = SearchTools.search("", "", 0, SearchTools.SECTION_RESOURCE_PACK, SearchTools.DEFAULT_PAGE_OFFSET, "", 0);
                    List<ModListBean.Mod> list = stream.collect(toList());
                    resourcePackList.clear();
                    resourcePackList.addAll(list);
                    searchHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
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
                resourcePackListView.setVisibility(View.GONE);
            }
            if (msg.what == 1) {
                downloadResourcePackListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                resourcePackListView.setVisibility(View.VISIBLE);
                isSearching = false;
            }
        }
    };

}
