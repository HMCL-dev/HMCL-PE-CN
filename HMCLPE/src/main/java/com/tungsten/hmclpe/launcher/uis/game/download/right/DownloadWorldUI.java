package com.tungsten.hmclpe.launcher.uis.game.download.right;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

public class DownloadWorldUI extends BaseUI implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public LinearLayout downloadWorldUI;

    private EditText editName;
    private EditText editVersion;
    private Spinner editVersionSpinner;
    private Spinner editCategory;
    private Spinner editSort;
    private Button search;

    private ArrayList<String> sortList;
    private ArrayAdapter<String> sortListAdapter;

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

        editName = activity.findViewById(R.id.download_world_arg_name);
        editVersion = activity.findViewById(R.id.edit_download_world_arg_version);
        editVersionSpinner = activity.findViewById(R.id.download_world_arg_version);
        editCategory = activity.findViewById(R.id.download_world_arg_type);
        editSort = activity.findViewById(R.id.download_world_arg_sort);

        search = activity.findViewById(R.id.search_world);
        search.setOnClickListener(this);

        sortList = new ArrayList<>();
        sortList.add(context.getString(R.string.download_mod_sort_date));
        sortList.add(context.getString(R.string.download_mod_sort_heat));
        sortList.add(context.getString(R.string.download_mod_sort_recent));
        sortList.add(context.getString(R.string.download_mod_sort_name));
        sortList.add(context.getString(R.string.download_mod_sort_author));
        sortList.add(context.getString(R.string.download_mod_sort_downloads));
        sortListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,sortList);
        sortListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        editSort.setAdapter(sortListAdapter);

        editVersionSpinner.setOnItemSelectedListener(this);
        editCategory.setOnItemSelectedListener(this);
        editSort.setOnItemSelectedListener(this);

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
        if (worldList.size() == 0 && editName.getText().toString().equals("")){
            search();
        }
    }

    private void search(){
        if (!isSearching){
            new Thread() {
                @Override
                public void run() {
                    try {
                        searchHandler.sendEmptyMessage(0);
                        Stream<ModListBean.Mod> stream = SearchTools.search("", editVersion.getText().toString(), 0, SearchTools.SECTION_WORLD, SearchTools.DEFAULT_PAGE_OFFSET, editName.getText().toString(), 0);
                        List<ModListBean.Mod> list = stream.collect(toList());
                        worldList.clear();
                        worldList.addAll(list);
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

    @Override
    public void onClick(View v) {
        if (v == search){
            search();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == editVersionSpinner){

        }
        if (parent == editCategory){

        }
        if (parent == editSort){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
