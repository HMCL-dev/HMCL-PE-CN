package com.tungsten.hmclpe.launcher.uis.universal.setting.right.help;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.info.docs.DocCategoryListAdapter;
import com.tungsten.hmclpe.launcher.list.info.docs.DocListBean;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HelpUI extends BaseUI implements View.OnClickListener {

    public LinearLayout helpUI;

    public static final String HMCL_PE_DOC_PAGE = "https://tungstend.github.io/pages/documentation.html";
    public static final String HMCL_PE_DOC_INDEX = "https://raw.githubusercontent.com/Tungstend/HMCL-PE-Docs/main/index.json";

    private ImageButton docPage;

    private ListView listView;
    private ProgressBar progressBar;
    private TextView refresh;

    public HelpUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        helpUI = activity.findViewById(R.id.ui_help);

        docPage = activity.findViewById(R.id.hmclpe_doc_link);
        docPage.setOnClickListener(this);

        listView = activity.findViewById(R.id.hmclpe_doc_list);
        progressBar = activity.findViewById(R.id.loading_hmclpe_doc_list_progress);
        refresh = activity.findViewById(R.id.refresh_doc_list);

        refresh.setOnClickListener(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(helpUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startHelpUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
        refresh();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(helpUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startHelpUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    private void refresh() {
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        new Thread(() -> {
            try {
                String response = NetworkUtils.doGet(NetworkUtils.toURL(HMCL_PE_DOC_INDEX));
                DocListBean[] docs = new Gson().fromJson(response, DocListBean[].class);
                ArrayList<DocListBean> list = new ArrayList<>(Arrays.asList(docs));
                activity.runOnUiThread(() -> {
                    listView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    refresh.setVisibility(View.GONE);
                    DocCategoryListAdapter adapter = new DocCategoryListAdapter(context, list);
                    listView.setAdapter(adapter);
                });
            } catch (IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
                    listView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    refresh.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view == docPage) {
            Uri uri = Uri.parse(HMCL_PE_DOC_PAGE);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
        if (view == refresh) {
            refresh();
        }
    }
}
