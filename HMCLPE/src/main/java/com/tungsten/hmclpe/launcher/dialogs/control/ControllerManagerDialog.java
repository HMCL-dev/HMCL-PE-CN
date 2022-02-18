package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.list.local.controller.ControlPattern;
import com.tungsten.hmclpe.launcher.list.local.controller.ControlPatternListAdapter;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.file.FileUtils;

import java.util.ArrayList;

public class ControllerManagerDialog extends Dialog implements View.OnClickListener {

    private boolean fullscreen;
    public String currentPattern;
    public OnPatternChangeListener onPatternChangeListener;

    private ListView patternList;
    private Button importPattern;
    private Button createNewPattern;
    private Button exit;

    public ControllerManagerDialog(@NonNull Context context,boolean fullscreen,String currentPattern,OnPatternChangeListener onPatternChangeListener) {
        super(context);
        this.fullscreen = fullscreen;
        this.currentPattern = currentPattern;
        this.onPatternChangeListener = onPatternChangeListener;
        setContentView(R.layout.dialog_controller_manager);
        setCancelable(false);
        init();
    }

    private void init(){
        patternList = findViewById(R.id.control_pattern_list);
        importPattern = findViewById(R.id.import_pattern);
        createNewPattern = findViewById(R.id.new_pattern);
        exit = findViewById(R.id.exit);

        importPattern.setOnClickListener(this);
        createNewPattern.setOnClickListener(this);
        exit.setOnClickListener(this);

        loadList();
    }

    private void loadList(){
        ArrayList<ControlPattern> list = SettingUtils.getControlPatternList();
        ControlPatternListAdapter adapter = new ControlPatternListAdapter(getContext(),this,list,currentPattern,fullscreen);
        patternList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == importPattern){

        }
        if (view == createNewPattern){
            CreateControlPatternDialog dialog = new CreateControlPatternDialog(getContext(), new CreateControlPatternDialog.OnPatternCreateListener() {
                @Override
                public void OnPatternCreate(ControlPattern controlPattern) {
                    FileUtils.createDirectory(AppManifest.CONTROLLER_DIR + "/" + controlPattern.name);
                    Gson gson = new Gson();
                    String string = gson.toJson(controlPattern);
                    FileStringUtils.writeFile(AppManifest.CONTROLLER_DIR + "/" + controlPattern.name + "/info.json",string);
                    loadList();
                }
            });
            dialog.show();
        }
        if (view == exit){
            dismiss();
        }
    }

    public interface OnPatternChangeListener{
        void onPatternChange(String pattern);
    }

}
