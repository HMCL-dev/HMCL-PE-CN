package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class AddViewDialog extends Dialog implements View.OnClickListener {

    private Button showButton;
    private Button showRocker;
    private LinearLayout editButton;
    private LinearLayout editRocker;

    private Button positive;
    private Button negative;

    private int viewType;

    public AddViewDialog(@NonNull Context context) {
        super(context);
        viewType = 0;
        setContentView(R.layout.dialog_add_view);
        setCancelable(false);
        init();
    }

    private void init(){
        showButton = findViewById(R.id.add_button);
        showRocker = findViewById(R.id.add_rocker);
        editButton = findViewById(R.id.add_button_layout);
        editRocker = findViewById(R.id.add_rocker_layout);

        positive = findViewById(R.id.add_current_view);
        negative = findViewById(R.id.exit);

        showButton.setOnClickListener(this);
        showRocker.setOnClickListener(this);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == showButton){
            viewType = 0;
            showButton.setBackgroundColor(getContext().getColor(R.color.colorPureWhite));
            showRocker.setBackgroundColor(getContext().getColor(R.color.colorLightGray));
            editButton.setVisibility(View.VISIBLE);
            editRocker.setVisibility(View.GONE);
        }
        if (view == showRocker){
            viewType = 1;
            showButton.setBackgroundColor(getContext().getColor(R.color.colorLightGray));
            showRocker.setBackgroundColor(getContext().getColor(R.color.colorPureWhite));
            editButton.setVisibility(View.GONE);
            editRocker.setVisibility(View.VISIBLE);
        }
        if (view == positive){

        }
        if (view == negative){
            dismiss();
        }
    }
}
