package com.tungsten.hmclpe.launcher.uis.game.download.right.game;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.mod.modrinth.Modrinth;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.list.download.minecraft.fabric.DownloadFabricAPIListAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DownloadFabricAPIUI extends BaseUI implements View.OnClickListener {

    public LinearLayout downloadFabricAPIUI;

    public String version;

    private LinearLayout hintLayout;

    private ListView fabricAPIListView;
    private ProgressBar progressBar;
    private TextView back;

    private static final String FABRIC_API_ID = "P7dR8mSH";

    public DownloadFabricAPIUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadFabricAPIUI = activity.findViewById(R.id.ui_install_fabric_api_list);

        hintLayout = activity.findViewById(R.id.download_fabric_api_hint_layout);
        hintLayout.setOnClickListener(this);

        fabricAPIListView = activity.findViewById(R.id.fabric_api_version_list);
        progressBar = activity.findViewById(R.id.loading_fabric_api_list_progress);
        back = activity.findViewById(R.id.back_to_install_ui_fabric_api);

        back.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.fabric_api_list_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(downloadFabricAPIUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadFabricAPIUI,activity,context,true);
    }

    private void init(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.fabric_api_list_ui_warn);
        builder.setMessage(R.string.fabric_api_list_ui_warn_text);
        builder.setPositiveButton(R.string.fabric_api_list_ui_positive, (dialogInterface, i) -> { });
        AlertDialog dialog = builder.create();
        dialog.show();
        new Thread(){
            @Override
            public void run() {
                loadingHandler.sendEmptyMessage(0);
                ArrayList<ModListBean.Version> apiVersions = new ArrayList<>();
                ArrayList<ModListBean.Version> availableVersions = new ArrayList<>();
                try {
                    Stream<ModListBean.Version> stream = Modrinth.getRemoteVersionsById(FABRIC_API_ID);
                    List<ModListBean.Version> list = stream.collect(toList());
                    apiVersions.addAll(list);
                    boolean exist = false;
                    for (ModListBean.Version v : apiVersions) {
                        if (v.getGameVersions().contains(version)){
                            exist = true;
                            availableVersions.add(v);
                        }
                    }
                    if (exist) {
                        DownloadFabricAPIListAdapter adapter = new DownloadFabricAPIListAdapter(context,activity,version,availableVersions);
                        loadingHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                fabricAPIListView.setAdapter(adapter);
                            }
                        });
                        loadingHandler.sendEmptyMessage(1);
                    }
                    else {
                        loadingHandler.sendEmptyMessage(2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        if (view == hintLayout){
            Uri uri = Uri.parse("https://afdian.net/@bangbang93");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
        if (view == back){
            activity.backToLastUI();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                fabricAPIListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 1){
                fabricAPIListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 2){
                fabricAPIListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        }
    };
}
