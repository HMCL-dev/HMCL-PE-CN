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
import com.tungsten.hmclpe.launcher.download.resources.SearchTools;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.list.download.modpack.DownloadPackageListAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DownloadPackageUI extends BaseUI {

    public LinearLayout downloadPackageUI;

    private boolean isSearching = false;

    private ProgressBar progressBar;

    private ListView packageListView;
    private ArrayList<ModListBean.Mod> packageList;
    private DownloadPackageListAdapter downloadPackageListAdapter;

    public DownloadPackageUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadPackageUI = activity.findViewById(R.id.ui_download_package);

        progressBar = activity.findViewById(R.id.loading_download_package_list_progress);

        packageListView = activity.findViewById(R.id.download_package_list);
        packageList = new ArrayList<>();
        downloadPackageListAdapter = new DownloadPackageListAdapter(context,activity,packageList);
        packageListView.setAdapter(downloadPackageListAdapter);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(downloadPackageUI,activity,context,false);
        activity.uiManager.downloadUI.startDownloadPackageUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadPackageUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.downloadUI.startDownloadPackageUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
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
                        Stream<ModListBean.Mod> stream = SearchTools.search("", "", 0, SearchTools.SECTION_PACKAGE, SearchTools.DEFAULT_PAGE_OFFSET, "", 0);
                        List<ModListBean.Mod> list = stream.collect(toList());
                        packageList.clear();
                        packageList.addAll(list);
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
                packageListView.setVisibility(View.GONE);
            }
            if (msg.what == 1) {
                downloadPackageListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                packageListView.setVisibility(View.VISIBLE);
                isSearching = false;
            }
        }
    };

}
