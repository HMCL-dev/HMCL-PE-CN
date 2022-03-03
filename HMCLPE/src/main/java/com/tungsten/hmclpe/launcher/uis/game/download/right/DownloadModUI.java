package com.tungsten.hmclpe.launcher.uis.game.download.right;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.download.resources.curse.CurseModManager;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.download.resources.SearchTools;
import com.tungsten.hmclpe.launcher.list.download.mod.DownloadModListAdapter;
import com.tungsten.hmclpe.launcher.list.view.spinner.SpinnerAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DownloadModUI extends BaseUI implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher, TextView.OnEditorActionListener {

    public LinearLayout downloadModUI;

    private Spinner gameSpinner;
    private Spinner downloadSourceSpinner;
    private EditText editName;
    private EditText editVersion;
    private Spinner versionSpinner;
    private Spinner typeSpinner;
    private Spinner sortSpinner;
    private Button search;

    private ArrayList<String> sourceList;
    private ArrayAdapter<String> sourceListAdapter;
    private ArrayList<String> versionList;
    private ArrayAdapter<String> versionListAdapter;
    private ArrayList<CurseModManager.Category> categoryList;
    private SpinnerAdapter categoryListAdapter;
    private ArrayList<String> sortList;
    private ArrayAdapter<String> sortListAdapter;

    private ListView modListView;
    private ArrayList<ModListBean.Mod> modList;
    private DownloadModListAdapter modListAdapter;
    private ProgressBar progressBar;
    private boolean isSearching = false;

    public DownloadModUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadModUI = activity.findViewById(R.id.ui_download_mod);

        gameSpinner = activity.findViewById(R.id.download_mod_arg_game);
        downloadSourceSpinner = activity.findViewById(R.id.download_mod_arg_source);
        editName = activity.findViewById(R.id.download_mod_arg_name);
        editVersion = activity.findViewById(R.id.edit_download_mod_arg_version);
        versionSpinner = activity.findViewById(R.id.download_mod_arg_version);
        typeSpinner = activity.findViewById(R.id.download_mod_arg_type);
        sortSpinner = activity.findViewById(R.id.download_mod_arg_sort);

        sourceList = new ArrayList<>();
        sourceList.add(context.getString(R.string.download_mod_source_curse_forge));
        sourceList.add(context.getString(R.string.download_mod_source_modrinth));
        sourceListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,sourceList);
        sourceListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        downloadSourceSpinner.setAdapter(sourceListAdapter);

        String[] versionArray = context.getResources().getStringArray(R.array.download_resource_version);
        versionList = new ArrayList<>();
        versionList.add("");
        versionList.addAll(Arrays.asList(versionArray));
        versionListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,versionList);
        versionListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        versionSpinner.setAdapter(versionListAdapter);

        categoryList = new ArrayList<>();
        categoryList.add(new CurseModManager.Category(0, "All", "", "", 6, 6, 432, new ArrayList<>()));
        categoryListAdapter = new SpinnerAdapter(context,categoryList,6);
        typeSpinner.setAdapter(categoryListAdapter);

        sortList = new ArrayList<>();
        sortList.add(context.getString(R.string.download_mod_sort_date));
        sortList.add(context.getString(R.string.download_mod_sort_heat));
        sortList.add(context.getString(R.string.download_mod_sort_recent));
        sortList.add(context.getString(R.string.download_mod_sort_name));
        sortList.add(context.getString(R.string.download_mod_sort_author));
        sortList.add(context.getString(R.string.download_mod_sort_downloads));
        sortListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,sortList);
        sortListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        sortSpinner.setAdapter(sortListAdapter);

        gameSpinner.setOnItemSelectedListener(this);
        downloadSourceSpinner.setOnItemSelectedListener(this);
        versionSpinner.setOnItemSelectedListener(this);
        typeSpinner.setOnItemSelectedListener(this);
        sortSpinner.setOnItemSelectedListener(this);

        search = activity.findViewById(R.id.search_mod);

        search.setOnClickListener(this);

        editName.setOnEditorActionListener(this);
        editVersion.setOnEditorActionListener(this);
        editVersion.addTextChangedListener(this);

        modListView = activity.findViewById(R.id.download_mod_list);
        modList = new ArrayList<>();
        modListAdapter = new DownloadModListAdapter(context,activity,modList);
        modListView.setAdapter(modListAdapter);

        progressBar = activity.findViewById(R.id.loading_download_mod_list_progress);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(downloadModUI,activity,context,false);
        activity.uiManager.downloadUI.startDownloadModUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(downloadModUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.downloadUI.startDownloadModUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == search){
            search();
        }
    }

    private void init(){
        if (modList.size() == 0 && editName.getText().toString().equals("")){
            search();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == typeSpinner || parent == sortSpinner || parent == versionSpinner){
            search();
            if (parent == versionSpinner){
                editVersion.setText((String) parent.getItemAtPosition(position));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void search(){
        if (!isSearching){
            new Thread() {
                @Override
                public void run() {
                    try {
                        searchHandler.sendEmptyMessage(0);
                        List<CurseModManager.Category> categories = new ArrayList<>();
                        try {
                            categories = CurseModManager.getCategories(SearchTools.SECTION_MOD);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stream<ModListBean.Mod> stream = SearchTools.searchImpl(downloadSourceSpinner.getSelectedItem().toString(), editVersion.getText().toString(), ((CurseModManager.Category) typeSpinner.getSelectedItem()).getId(), SearchTools.SECTION_MOD, SearchTools.DEFAULT_PAGE_OFFSET, editName.getText().toString(), sortSpinner.getSelectedItemPosition());
                        List<ModListBean.Mod> list = stream.collect(toList());
                        modList.clear();
                        modList.addAll(list);
                        categoryList.clear();
                        categoryList.add(new CurseModManager.Category(0, "All", "", "", 6, 6, 432, new ArrayList<>()));
                        for (int i = 0;i < categories.size();i++){
                            categoryList.add(categories.get(i));
                            categoryList.addAll(categories.get(i).getSubcategories());
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
                modListView.setVisibility(View.GONE);
            }
            if (msg.what == 1) {
                modListAdapter.notifyDataSetChanged();
                categoryListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                modListView.setVisibility(View.VISIBLE);
                isSearching = false;
            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        search();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v == editName || v == editVersion){
            search();
        }
        return false;
    }
}
