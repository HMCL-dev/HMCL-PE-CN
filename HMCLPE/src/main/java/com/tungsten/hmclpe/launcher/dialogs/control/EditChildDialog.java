package com.tungsten.hmclpe.launcher.dialogs.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.list.local.controller.ChildLayout;

import java.util.ArrayList;

public class EditChildDialog extends Dialog implements View.OnClickListener , AdapterView.OnItemSelectedListener {

    private OnChildChangeListener onChildChangeListener;
    private ChildLayout childLayout;

    private EditText editName;
    private Spinner spinner;

    private Button positive;
    private Button negative;

    private int visibility;

    public EditChildDialog(@NonNull Context context,OnChildChangeListener onChildChangeListener,ChildLayout childLayout) {
        super(context);
        this.onChildChangeListener = onChildChangeListener;
        this.childLayout = childLayout;
        setContentView(R.layout.dialog_edit_child);
        setCancelable(false);
        init();
    }

    private void init(){
        editName = findViewById(R.id.edit_child_name);
        spinner = findViewById(R.id.visibility_spinner);

        positive = findViewById(R.id.create_current_child);
        negative = findViewById(R.id.exit);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        ArrayList<String> list = new ArrayList<>();
        list.add(getContext().getString(R.string.dialog_create_child_visibility_visible));
        list.add(getContext().getString(R.string.dialog_create_child_visibility_invisible));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        editName.setText(childLayout.name);
        if (childLayout.visibility == View.VISIBLE){
            spinner.setSelection(0);
        }
        else {
            spinner.setSelection(1);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == positive){
            if (editName.getText().toString().equals("")){
                Toast.makeText(getContext(),getContext().getString(R.string.dialog_create_child_warn),Toast.LENGTH_SHORT).show();
            }
            else {
                onChildChangeListener.onChildChange(new ChildLayout(editName.getText().toString(),visibility, childLayout.viewList));
                dismiss();
            }
        }
        if (view == negative){
            dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0){
            visibility = View.VISIBLE;
        }
        if (i == 1){
            visibility = View.INVISIBLE;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnChildChangeListener{
        void onChildChange(ChildLayout childLayout);
    }

}
