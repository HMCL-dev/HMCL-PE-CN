package com.tungsten.hmclpe.launcher.list.local.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.ControlPatternActivity;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.control.ControllerManagerDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.animation.HiddenAnimationUtils;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;
import com.tungsten.hmclpe.utils.file.FileUtils;

import java.util.ArrayList;

public class ControlPatternListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ControllerManagerDialog dialog;
    private ArrayList<ControlPattern> list;
    private String currentPattern;
    private boolean fullscreen;

    public ControlPatternListAdapter(Context context, MainActivity activity, ControllerManagerDialog dialog, ArrayList<ControlPattern> list, String currentPattern, boolean fullscreen){
        this.context = context;
        this.activity = activity;
        this.dialog = dialog;
        this.list = list;
        this.currentPattern = currentPattern;
        this.fullscreen = fullscreen;
    }

    private class ViewHolder{
        LinearLayout item;
        LinearLayout info;
        RadioButton check;
        TextView name;
        TextView author;
        TextView version;
        TextView describe;
        ImageButton edit;
        ImageButton share;
        ImageButton delete;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_control_pattern,null);
            viewHolder.item = view.findViewById(R.id.item);
            viewHolder.info = view.findViewById(R.id.pattern_info_layout);
            viewHolder.check = view.findViewById(R.id.check_current_pattern);
            viewHolder.name = view.findViewById(R.id.pattern_name);
            viewHolder.author = view.findViewById(R.id.author_text);
            viewHolder.version = view.findViewById(R.id.version_text);
            viewHolder.describe = view.findViewById(R.id.describe_text);
            viewHolder.edit = view.findViewById(R.id.edit_pattern);
            viewHolder.share = view.findViewById(R.id.share_pattern);
            viewHolder.delete = view.findViewById(R.id.delete_pattern);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        ControlPattern pattern = list.get(i);
        viewHolder.name.setText(pattern.name);
        viewHolder.author.setText(pattern.author);
        viewHolder.version.setText(pattern.versionName);
        viewHolder.describe.setText(pattern.describe);
        viewHolder.check.setChecked(pattern.name.equals(currentPattern));
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.onPatternChangeListener.onPatternChange(pattern.name);
                dialog.currentPattern = pattern.name;
                currentPattern = pattern.name;
                notifyDataSetChanged();
            }
        });
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HiddenAnimationUtils.newInstance(context,viewHolder.info,null, ConvertUtils.dip2px(context,70)).toggle();
            }
        });
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ControlPatternActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pattern", pattern.name);
                bundle.putString("initial", currentPattern);
                bundle.putBoolean("fullscreen",fullscreen);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent,ControlPatternActivity.CONTROL_PATTERN_REQUEST_CODE);
            }
        });
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.dialog_delete_control_pattern_title));
                builder.setMessage(context.getString(R.string.dialog_delete_control_pattern_content));
                builder.setPositiveButton(context.getString(R.string.dialog_delete_control_pattern_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int p) {
                        if (currentPattern.equals(pattern.name)){
                            if (list.size() == 1){
                                FileUtils.deleteDirectory(AppManifest.CONTROLLER_DIR + "/" + pattern.name);
                                list.remove(i);
                            }
                            else {
                                FileUtils.deleteDirectory(AppManifest.CONTROLLER_DIR + "/" + pattern.name);
                                list.remove(i);
                                dialog.onPatternChangeListener.onPatternChange(list.get(0).name);
                                dialog.currentPattern = list.get(0).name;
                                currentPattern = list.get(0).name;
                            }
                        }
                        else {
                            FileUtils.deleteDirectory(AppManifest.CONTROLLER_DIR + "/" + pattern.name);
                            list.remove(i);
                        }
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(context.getString(R.string.dialog_delete_control_pattern_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }
}
