package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.bean.button.ButtonStyle;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;

public class ButtonStyleManagerDialog extends Dialog implements View.OnClickListener {

    private AddViewDialog dialog;

    private ListView listView;

    private Button create;
    private Button positive;

    public ButtonStyleManagerDialog(@NonNull Context context,AddViewDialog dialog) {
        super(context);
        this.dialog = dialog;
        setContentView(R.layout.dialog_manage_button_style);
        setCancelable(false);
        init();
    }

    private void init(){
        listView = findViewById(R.id.button_style_list);

        create = findViewById(R.id.create_button_style);
        positive = findViewById(R.id.exit);
        create.setOnClickListener(this);
        positive.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == create){
            CreateButtonStyleDialog dialog = new CreateButtonStyleDialog(getContext(), SettingUtils.getButtonStyleList(), new CreateButtonStyleDialog.OnButtonStyleCreateListener() {
                @Override
                public void onButtonStyleCreate(ButtonStyle buttonStyle) {

                }
            });
            dialog.show();
        }
        if (view == positive){
            dismiss();
        }
    }
}
