package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class ControllerManagerDialog extends Dialog implements View.OnClickListener {

    private String currentPattern;
    private OnPatternChangeListener onPatternChangeListener;

    private ListView patternList;
    private Button importPattern;
    private Button createNewPattern;
    private Button exit;

    public ControllerManagerDialog(@NonNull Context context,String currentPattern,OnPatternChangeListener onPatternChangeListener) {
        super(context);
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
    }

    @Override
    public void onClick(View view) {
        if (view == importPattern){

        }
        if (view == createNewPattern){

        }
        if (view == exit){
            dismiss();
        }
    }

    public interface OnPatternChangeListener{
        void onPatternChange(String pattern);
    }

}
