package com.tungsten.hmclpe.launcher.uis.game.download.right;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.download.DownloadResourceAdapter;
import com.tungsten.hmclpe.launcher.mod.SearchTools;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.mod.curse.CurseModManager;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.launcher.view.spinner.CFCSpinnerAdapter;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DownloadResourcePackUI extends BaseUI implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener, TextWatcher {

    public LinearLayout downloadResourcePackUI;

    private EditText editName;
    private EditText editVersion;
    private Spinner editVersionSpinner;
    private Spinner editCategory;
    private Spinner editSort;
    private Button search;

    private ArrayList<String> sortList;
    private ArrayAdapter<String> sortListAdapter;
    private ArrayList<String> versionList;
    private ArrayAdapter<String> versionListAdapter;
    private ArrayList<CurseModManager.Category> categoryList;
    private CFCSpinnerAdapter categoryListAdapter;

    private boolean isSearching = false;

    private ProgressBar progressBar;
    private TextView refreshText;

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

        editName = activity.findViewById(R.id.download_resource_pack_arg_name);
        editVersion = activity.findViewById(R.id.edit_download_resource_pack_arg_version);
        editVersionSpinner = activity.findViewById(R.id.download_resource_pack_arg_version);
        editCategory = activity.findViewById(R.id.download_resource_pack_arg_type);
        editSort = activity.findViewById(R.id.download_resource_pack_arg_sort);

        search = activity.findViewById(R.id.search_resource_pack);
        search.setOnClickListener(this);

        sortList = new ArrayList<>();
        sortList.add(context.getString(R.string.download_mod_sort_date));
        sortList.add(context.getString(R.string.download_mod_sort_heat));
        sortList.add(context.getString(R.string.download_mod_sort_recent));
        sortList.add(context.getString(R.string.download_mod_sort_name));
        sortList.add(context.getString(R.string.download_mod_sort_author));
        sortList.add(context.getString(R.string.download_mod_sort_downloads));
        sortList.add(context.getString(R.string.download_mod_sort_category));
        sortList.add(context.getString(R.string.download_mod_sort_game_version));
        sortListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,sortList);
        sortListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        editSort.setAdapter(sortListAdapter);

        String[] versionArray = DownloadModUI.DEFAULT_GAME_VERSIONS;
        versionList = new ArrayList<>();
        versionList.add("");
        versionList.addAll(Arrays.asList(versionArray));
        versionListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,versionList);
        versionListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        editVersionSpinner.setAdapter(versionListAdapter);

        categoryList = new ArrayList<>();
        categoryList.add(new CurseModManager.Category(0, "All", "", "", 12, 432, true, 0, new ArrayList<>()));
        for (CurseModManager.Category category : categoryList) {
            int resId = context.getResources().getIdentifier("curse_category_" + category.getId(),"string","com.tungsten.hmclpe");
            if (resId != 0 && context.getString(resId) != null) {
                category.setName(context.getString(resId));
            }
        }
        categoryListAdapter = new CFCSpinnerAdapter(context,categoryList,12);
        editCategory.setAdapter(categoryListAdapter);

        editVersionSpinner.setOnItemSelectedListener(this);
        editCategory.setOnItemSelectedListener(this);
        editSort.setOnItemSelectedListener(this);

        editName.setOnEditorActionListener(this);
        editVersion.setOnEditorActionListener(this);
        editVersion.addTextChangedListener(this);

        progressBar = activity.findViewById(R.id.loading_download_resource_pack_list_progress);
        refreshText = activity.findViewById(R.id.refresh_resource_pack_list);
        refreshText.setOnClickListener(this);

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
        if (resourcePackList.size() == 0 && editName.getText().toString().equals("")){
            search();
        }
    }

    private void search(){
        if (!isSearching){
            new Thread(() -> {
                try {
                    searchHandler.sendEmptyMessage(0);
                    List<CurseModManager.Category> categories;
                    categories = CurseModManager.getCategories(SearchTools.SECTION_RESOURCE_PACK);
                    Stream<ModListBean.Mod> stream = SearchTools.search("", editVersion.getText().toString(), ((CurseModManager.Category) editCategory.getSelectedItem()).getId(), null, SearchTools.SECTION_RESOURCE_PACK, SearchTools.DEFAULT_PAGE_OFFSET, editName.getText().toString(), editSort.getSelectedItemPosition());
                    List<ModListBean.Mod> list = stream.collect(toList());
                    resourcePackList.clear();
                    resourcePackList.addAll(list);
                    categoryList.clear();
                    categoryList.add(new CurseModManager.Category(0, "All", "", "", 12, 432, true, 0, new ArrayList<>()));
                    for (int i = 0;i < categories.size();i++){
                        categoryList.add(categories.get(i));
                        categoryList.addAll(categories.get(i).getSubcategories());
                    }
                    for (CurseModManager.Category category : categoryList) {
                        int resId = context.getResources().getIdentifier("curse_category_" + category.getId(),"string","com.tungsten.hmclpe");
                        if (resId != 0 && context.getString(resId) != null) {
                            category.setName(context.getString(resId));
                        }
                    }
                    searchHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    searchHandler.sendEmptyMessage(2);
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
                refreshText.setVisibility(View.GONE);
                resourcePackListView.setVisibility(View.GONE);
            }
            if (msg.what == 1) {
                downloadResourcePackListAdapter.notifyDataSetChanged();
                categoryListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                refreshText.setVisibility(View.GONE);
                resourcePackListView.setVisibility(View.VISIBLE);
                isSearching = false;
            }
            if (msg.what == 2) {
                progressBar.setVisibility(View.GONE);
                refreshText.setVisibility(View.VISIBLE);
                resourcePackListView.setVisibility(View.GONE);
                isSearching = false;
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view == search){
            search();
        }
        if (view == refreshText) {
            search();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == editCategory || adapterView == editSort || adapterView == editVersionSpinner){
            search();
            if (adapterView == editVersionSpinner){
                editVersion.setText((String) adapterView.getItemAtPosition(i));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (textView == editName || textView == editVersion){
            search();
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        search();
    }
}
