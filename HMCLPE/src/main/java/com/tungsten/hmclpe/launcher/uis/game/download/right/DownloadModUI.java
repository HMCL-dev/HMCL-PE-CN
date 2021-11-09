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
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.download.resources.SearchTools;
import com.tungsten.hmclpe.launcher.list.download.mod.DownloadModListAdapter;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

import java.util.ArrayList;
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
    private ArrayList<String> categoryList;
    private ArrayAdapter<String> categoryListAdapter;
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

        versionList = new ArrayList<>();
        versionList.add("");
        versionList.add(context.getString(R.string.download_mod_version_1171));
        versionList.add(context.getString(R.string.download_mod_version_117));
        versionList.add(context.getString(R.string.download_mod_version_1165));
        versionList.add(context.getString(R.string.download_mod_version_1164));
        versionList.add(context.getString(R.string.download_mod_version_1163));
        versionList.add(context.getString(R.string.download_mod_version_1162));
        versionList.add(context.getString(R.string.download_mod_version_1161));
        versionList.add(context.getString(R.string.download_mod_version_116));
        versionList.add(context.getString(R.string.download_mod_version_1144));
        versionList.add(context.getString(R.string.download_mod_version_1143));
        versionList.add(context.getString(R.string.download_mod_version_1142));
        versionList.add(context.getString(R.string.download_mod_version_1141));
        versionList.add(context.getString(R.string.download_mod_version_114));
        versionList.add(context.getString(R.string.download_mod_version_1132));
        versionList.add(context.getString(R.string.download_mod_version_1131));
        versionList.add(context.getString(R.string.download_mod_version_113));
        versionList.add(context.getString(R.string.download_mod_version_1122));
        versionList.add(context.getString(R.string.download_mod_version_1121));
        versionList.add(context.getString(R.string.download_mod_version_112));
        versionList.add(context.getString(R.string.download_mod_version_1112));
        versionList.add(context.getString(R.string.download_mod_version_1111));
        versionList.add(context.getString(R.string.download_mod_version_111));
        versionList.add(context.getString(R.string.download_mod_version_1102));
        versionList.add(context.getString(R.string.download_mod_version_1101));
        versionList.add(context.getString(R.string.download_mod_version_110));
        versionList.add(context.getString(R.string.download_mod_version_194));
        versionList.add(context.getString(R.string.download_mod_version_193));
        versionList.add(context.getString(R.string.download_mod_version_192));
        versionList.add(context.getString(R.string.download_mod_version_191));
        versionList.add(context.getString(R.string.download_mod_version_19));
        versionList.add(context.getString(R.string.download_mod_version_189));
        versionList.add(context.getString(R.string.download_mod_version_188));
        versionList.add(context.getString(R.string.download_mod_version_187));
        versionList.add(context.getString(R.string.download_mod_version_186));
        versionList.add(context.getString(R.string.download_mod_version_185));
        versionList.add(context.getString(R.string.download_mod_version_184));
        versionList.add(context.getString(R.string.download_mod_version_183));
        versionList.add(context.getString(R.string.download_mod_version_182));
        versionList.add(context.getString(R.string.download_mod_version_181));
        versionList.add(context.getString(R.string.download_mod_version_18));
        versionList.add(context.getString(R.string.download_mod_version_1710));
        versionList.add(context.getString(R.string.download_mod_version_179));
        versionList.add(context.getString(R.string.download_mod_version_178));
        versionList.add(context.getString(R.string.download_mod_version_177));
        versionList.add(context.getString(R.string.download_mod_version_176));
        versionList.add(context.getString(R.string.download_mod_version_175));
        versionList.add(context.getString(R.string.download_mod_version_174));
        versionList.add(context.getString(R.string.download_mod_version_173));
        versionList.add(context.getString(R.string.download_mod_version_172));
        versionList.add(context.getString(R.string.download_mod_version_164));
        versionList.add(context.getString(R.string.download_mod_version_162));
        versionList.add(context.getString(R.string.download_mod_version_161));
        versionList.add(context.getString(R.string.download_mod_version_152));
        versionList.add(context.getString(R.string.download_mod_version_151));
        versionList.add(context.getString(R.string.download_mod_version_147));
        versionList.add(context.getString(R.string.download_mod_version_146));
        versionList.add(context.getString(R.string.download_mod_version_145));
        versionList.add(context.getString(R.string.download_mod_version_144));
        versionList.add(context.getString(R.string.download_mod_version_142));
        versionList.add(context.getString(R.string.download_mod_version_132));
        versionList.add(context.getString(R.string.download_mod_version_131));
        versionList.add(context.getString(R.string.download_mod_version_125));
        versionList.add(context.getString(R.string.download_mod_version_124));
        versionList.add(context.getString(R.string.download_mod_version_123));
        versionList.add(context.getString(R.string.download_mod_version_122));
        versionList.add(context.getString(R.string.download_mod_version_121));
        versionList.add(context.getString(R.string.download_mod_version_11));
        versionList.add(context.getString(R.string.download_mod_version_10));
        versionListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,versionList);
        versionListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
        versionSpinner.setAdapter(versionListAdapter);

        String[] categoryArray = context.getResources().getStringArray(R.array.download_mod_categories);
        categoryList = new ArrayList<>();
        for (int i = 0;i < categoryArray.length;i++){
            categoryList.add(categoryArray[i]);
        }
        categoryListAdapter = new ArrayAdapter<String>(context,R.layout.item_spinner,categoryList);
        categoryListAdapter.setDropDownViewResource(R.layout.item_spinner_drop_down);
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
        editVersion.addTextChangedListener(this);

        modListView = activity.findViewById(R.id.download_mod_list);
        modList = new ArrayList<>();
        modListAdapter = new DownloadModListAdapter(context,modList);
        modListView.setAdapter(modListAdapter);

        progressBar = activity.findViewById(R.id.loading_download_mod_list_progress);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v == search){
            search();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(){
        if (modList.size() == 0 && editName.getText().toString().equals("")){
            search();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                        Stream<ModListBean.Mod> stream = SearchTools.searchImpl(downloadSourceSpinner.getSelectedItem().toString(), editVersion.getText().toString(), SearchTools.getCategoryID(typeSpinner.getSelectedItemPosition()), SearchTools.SECTION_MOD, SearchTools.DEFAULT_PAGE_OFFSET, editName.getText().toString(), sortSpinner.getSelectedItemPosition());
                        List<ModListBean.Mod> list = stream.collect(toList());
                        modList.clear();
                        for (int i = 0; i < list.size(); i++) {
                            modList.add(list.get(i));
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void afterTextChanged(Editable s) {
        search();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v == editName || v == editVersion){
            search();
        }
        return false;
    }
}
