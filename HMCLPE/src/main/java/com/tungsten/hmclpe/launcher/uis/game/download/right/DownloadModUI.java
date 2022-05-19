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
import com.tungsten.hmclpe.launcher.mod.curse.CurseModManager;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.mod.SearchTools;
import com.tungsten.hmclpe.launcher.list.download.DownloadResourceAdapter;
import com.tungsten.hmclpe.launcher.mod.modrinth.Modrinth;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.launcher.view.spinner.CFCSpinnerAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.launcher.view.spinner.MRCSpinnerAdapter;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

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
    private Spinner typeSpinnerMR;
    private Spinner sortSpinner;
    private Button search;

    private ArrayList<String> gameList;
    private ArrayAdapter<String> gameListAdapter;
    private ArrayList<String> sourceList;
    private ArrayAdapter<String> sourceListAdapter;
    private ArrayList<String> versionList;
    private ArrayAdapter<String> versionListAdapter;
    private ArrayList<CurseModManager.Category> categoryList;
    private CFCSpinnerAdapter categoryListAdapter;
    private ArrayList<String> modrinthCategoryList;
    private MRCSpinnerAdapter modrinthCategoryListAdapter;
    private ArrayList<String> sortList;
    private ArrayAdapter<String> sortListAdapter;

    private ListView modListView;
    private ArrayList<ModListBean.Mod> modList;
    private DownloadResourceAdapter modListAdapter;
    private ProgressBar progressBar;
    private TextView refreshText;
    private boolean isSearching = false;

    public static String[] DEFAULT_GAME_VERSIONS = new String[]{
            "1.18.2", "1.18.1", "1.18",
            "1.17.1", "1.17",
            "1.16.5", "1.16.4", "1.16.3", "1.16.2", "1.16.1", "1.16",
            "1.15.2", "1.15.1", "1.15",
            "1.14.4", "1.14.3", "1.14.2", "1.14.1", "1.14",
            "1.13.2", "1.13.1", "1.13",
            "1.12.2", "1.12.1", "1.12",
            "1.11.2", "1.11.1", "1.11",
            "1.10.2", "1.10.1", "1.10",
            "1.9.4", "1.9.3", "1.9.2", "1.9.1", "1.9",
            "1.8.9", "1.8.8", "1.8.7", "1.8.6", "1.8.5", "1.8.4", "1.8.3", "1.8.2", "1.8.1", "1.8",
            "1.7.10", "1.7.9", "1.7.8", "1.7.7", "1.7.6", "1.7.5", "1.7.4", "1.7.3", "1.7.2",
            "1.6.4", "1.6.2", "1.6.1",
            "1.5.2", "1.5.1",
            "1.4.7", "1.4.6", "1.4.5", "1.4.4", "1.4.2",
            "1.3.2", "1.3.1",
            "1.2.5", "1.2.4", "1.2.3", "1.2.2", "1.2.1",
            "1.1",
            "1.0"
    };

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
        typeSpinnerMR = activity.findViewById(R.id.download_mod_arg_type_modrinth);
        sortSpinner = activity.findViewById(R.id.download_mod_arg_sort);

        gameList = SettingUtils.getLocalVersionNames(activity.launcherSetting.gameFileDirectory);
        gameListAdapter = new ArrayAdapter<>(context,R.layout.item_spinner,gameList);
        gameSpinner.setAdapter(gameListAdapter);

        sourceList = new ArrayList<>();
        sourceList.add(context.getString(R.string.download_mod_source_curse_forge));
        sourceList.add(context.getString(R.string.download_mod_source_modrinth));
        sourceListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,sourceList);
        sourceListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        downloadSourceSpinner.setAdapter(sourceListAdapter);

        String[] versionArray = DEFAULT_GAME_VERSIONS;
        versionList = new ArrayList<>();
        versionList.add("");
        versionList.addAll(Arrays.asList(versionArray));
        versionListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,versionList);
        versionListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        versionSpinner.setAdapter(versionListAdapter);

        categoryList = new ArrayList<>();
        categoryList.add(new CurseModManager.Category(0, "All", "", "", 6, 432, true, 0, new ArrayList<>()));
        for (CurseModManager.Category category : categoryList) {
            int resId = context.getResources().getIdentifier("curse_category_" + category.getId(),"string","com.tungsten.hmclpe");
            if (resId != 0 && context.getString(resId) != null) {
                category.setName(context.getString(resId));
            }
        }
        categoryListAdapter = new CFCSpinnerAdapter(context,categoryList,6);
        typeSpinner.setAdapter(categoryListAdapter);

        modrinthCategoryList = new ArrayList<>();
        modrinthCategoryList.add("all");
        modrinthCategoryListAdapter = new MRCSpinnerAdapter(context,modrinthCategoryList);
        typeSpinnerMR.setAdapter(modrinthCategoryListAdapter);

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
        sortSpinner.setAdapter(sortListAdapter);

        gameSpinner.setOnItemSelectedListener(this);
        downloadSourceSpinner.setOnItemSelectedListener(this);
        versionSpinner.setOnItemSelectedListener(this);
        typeSpinner.setOnItemSelectedListener(this);
        typeSpinnerMR.setOnItemSelectedListener(this);
        sortSpinner.setOnItemSelectedListener(this);

        search = activity.findViewById(R.id.search_mod);

        search.setOnClickListener(this);

        editName.setOnEditorActionListener(this);
        editVersion.setOnEditorActionListener(this);
        editVersion.addTextChangedListener(this);

        modListView = activity.findViewById(R.id.download_mod_list);
        modList = new ArrayList<>();
        modListAdapter = new DownloadResourceAdapter(context,activity,modList,0);
        modListView.setAdapter(modListAdapter);

        progressBar = activity.findViewById(R.id.loading_download_mod_list_progress);
        refreshText = activity.findViewById(R.id.refresh_mod_list);
        refreshText.setOnClickListener(this);
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
        if (v == refreshText) {
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
        if (parent == typeSpinner || parent == typeSpinnerMR || parent == sortSpinner || parent == versionSpinner){
            search();
            if (parent == versionSpinner){
                editVersion.setText((String) parent.getItemAtPosition(position));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void refreshGameList() {
        gameList = SettingUtils.getLocalVersionNames(activity.launcherSetting.gameFileDirectory);
        gameListAdapter.notifyDataSetChanged();
    }

    private void search(){
        if (!isSearching){
            new Thread(() -> {
                try {
                    searchHandler.sendEmptyMessage(0);
                    final int source = downloadSourceSpinner.getSelectedItemPosition();
                    Stream<ModListBean.Mod> stream = SearchTools.searchImpl(downloadSourceSpinner.getSelectedItem().toString(), editVersion.getText().toString(), ((CurseModManager.Category) typeSpinner.getSelectedItem()).getId(), (String) typeSpinnerMR.getSelectedItem(), SearchTools.SECTION_MOD, SearchTools.DEFAULT_PAGE_OFFSET, editName.getText().toString(), sortSpinner.getSelectedItemPosition());
                    List<ModListBean.Mod> list = stream.collect(toList());
                    modList.clear();
                    modList.addAll(list);
                    if (source == 0) {
                        List<CurseModManager.Category> categories;
                        categories = CurseModManager.getCategories(SearchTools.SECTION_MOD);
                        categoryList.clear();
                        categoryList.add(new CurseModManager.Category(0, "All", "", "", 6, 432, true, 0, new ArrayList<>()));
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
                        searchHandler.post(() -> {
                            categoryListAdapter.notifyDataSetChanged();
                            typeSpinner.setVisibility(View.VISIBLE);
                            typeSpinnerMR.setVisibility(View.GONE);
                        });
                    }
                    else {
                        List<String> categoryList = Modrinth.getCategories();
                        modrinthCategoryList.clear();
                        modrinthCategoryList.add("all");
                        modrinthCategoryList.addAll(categoryList);
                        searchHandler.post(() -> {
                            modrinthCategoryListAdapter.notifyDataSetChanged();
                            typeSpinner.setVisibility(View.GONE);
                            typeSpinnerMR.setVisibility(View.VISIBLE);
                        });
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
                modListView.setVisibility(View.GONE);
            }
            if (msg.what == 1) {
                modListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                refreshText.setVisibility(View.GONE);
                modListView.setVisibility(View.VISIBLE);
                isSearching = false;
            }
            if (msg.what == 2) {
                progressBar.setVisibility(View.GONE);
                refreshText.setVisibility(View.VISIBLE);
                modListView.setVisibility(View.GONE);
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
