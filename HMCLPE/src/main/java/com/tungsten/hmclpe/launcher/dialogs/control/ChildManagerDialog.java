package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.launcher.list.local.controller.ChildLayout;
import com.tungsten.hmclpe.launcher.list.local.controller.ChildLayoutListAdapter;
import com.tungsten.hmclpe.launcher.list.local.controller.ControlPattern;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;

import java.util.ArrayList;

public class ChildManagerDialog extends Dialog implements View.OnClickListener {

    private MenuHelper menuHelper;
    private ControlPattern controlPattern;

    private ListView childListView;

    private Button create;
    private Button exit;

    public ChildManagerDialog(@NonNull Context context, MenuHelper menuHelper, ControlPattern controlPattern) {
        super(context);
        this.menuHelper = menuHelper;
        this.controlPattern = controlPattern;
        setContentView(R.layout.dialog_child_manager);
        setCancelable(false);
        init();
    }

    private void init(){
        childListView = findViewById(R.id.child_list);

        create = findViewById(R.id.create_child);
        exit = findViewById(R.id.exit);

        create.setOnClickListener(this);
        exit.setOnClickListener(this);

        refreshListView();
    }

    public void refreshListView(){
        ArrayList<ChildLayout> list = SettingUtils.getChildList(controlPattern.name);
        ChildLayoutListAdapter adapter = new ChildLayoutListAdapter(getContext(),list,controlPattern,this);
        childListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == create) {
            CreateChildDialog dialog = new CreateChildDialog(getContext(), new CreateChildDialog.OnChildAddListener() {
                @Override
                public void onChildAdd(ChildLayout childLayout) {

                }
            });
            dialog.show();
        }
        if (view == exit) {
            dismiss();
        }
    }
}
