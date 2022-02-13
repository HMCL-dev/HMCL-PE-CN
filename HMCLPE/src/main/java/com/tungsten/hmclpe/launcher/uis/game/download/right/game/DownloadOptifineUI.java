package com.tungsten.hmclpe.launcher.uis.game.download.right.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class DownloadOptifineUI extends BaseUI implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public LinearLayout downloadOptifineUI;

    public static final String OPTIFINE_VERSION_MANIFEST = "https://bmclapi2.bangbang93.com/optifine/versionlist";

    public String version;

    private LinearLayout hintLayout;

    private CheckBox checkRelease;
    private CheckBox checkSnapshot;
    private CheckBox checkOld;

    private LinearLayout refresh;

    private ListView optifineListView;
    private ProgressBar progressBar;
    private TextView back;

    public DownloadOptifineUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadOptifineUI = activity.findViewById(R.id.ui_install_optifine_list);

        hintLayout = activity.findViewById(R.id.download_forge_hint_layout);
        hintLayout.setOnClickListener(this);

        checkRelease = activity.findViewById(R.id.optifine_checkbox_release);
        checkSnapshot = activity.findViewById(R.id.optifine_checkbox_snapshot);
        checkOld = activity.findViewById(R.id.optifine_checkbox_old);

        refresh = activity.findViewById(R.id.refresh_optifine_list);

        checkRelease.setChecked(true);

        checkRelease.setOnCheckedChangeListener(this);
        checkSnapshot.setOnCheckedChangeListener(this);
        checkOld.setOnCheckedChangeListener(this);

        refresh.setOnClickListener(this);

        optifineListView = activity.findViewById(R.id.optifine_version_list);
        progressBar = activity.findViewById(R.id.loading_optifine_list_progress);
        back = activity.findViewById(R.id.back_to_install_ui_optifine);

        back.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.optifine_list_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(downloadOptifineUI,activity,context,true);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadOptifineUI,activity,context,true);
    }

    private void init(){

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
        if (view == refresh){
            init();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                optifineListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 1){
                optifineListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
            }
            if (msg.what == 2){
                optifineListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        init();
    }
}
