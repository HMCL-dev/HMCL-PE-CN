package com.tungsten.hmclpe.launcher.list.local.mod;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tungsten.filepicker.Constants;
import com.tungsten.filepicker.FileBrowser;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.dialogs.ModInfoDialog;
import com.tungsten.hmclpe.launcher.mod.LocalModFile;
import com.tungsten.hmclpe.utils.string.ModTranslations;
import com.tungsten.hmclpe.utils.string.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class LocalModListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LocalModFile> list;

    private static class ViewHolder {
        LinearLayout item;
        CheckBox checkBox;
        TextView name;
        TextView category;
        TextView info;
        ImageButton openFolder;
        ImageButton showInfo;
    }

    public LocalModListAdapter (Context context,ArrayList<LocalModFile> list) {
        this.context = context;
        this.list = list;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_local_mod,null);
            viewHolder.item = view.findViewById(R.id.item);
            viewHolder.checkBox = view.findViewById(R.id.check_enable);
            viewHolder.name = view.findViewById(R.id.mod_name);
            viewHolder.category = view.findViewById(R.id.mod_category);
            viewHolder.info = view.findViewById(R.id.mod_info);
            viewHolder.openFolder = view.findViewById(R.id.open_folder);
            viewHolder.showInfo = view.findViewById(R.id.show_mod_info);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        LocalModFile localModFile = list.get(i);
        viewHolder.checkBox.setChecked(localModFile.isActive());
        viewHolder.name.setText(localModFile.getFileName());
        String displayName = ModTranslations.getModById(localModFile.getId()) == null ? "" : Objects.requireNonNull(ModTranslations.getModById(localModFile.getId())).getDisplayName();
        viewHolder.category.setText(displayName);
        String unknown = context.getString(R.string.mod_manager_ui_unknown_info);
        String name = StringUtils.isBlank(localModFile.getName()) ? unknown : localModFile.getName();
        String version = StringUtils.isBlank(localModFile.getVersion()) ? unknown : localModFile.getVersion();
        String author = StringUtils.isBlank(localModFile.getAuthors()) ? unknown : localModFile.getAuthors();
        viewHolder.info.setText(context.getString(R.string.mod_manager_ui_info).replace("%n",name).replace("%v",version).replace("%a",author));
        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            try {
                localModFile.setActive(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        viewHolder.item.setOnClickListener(view1 -> {

        });
        viewHolder.openFolder.setOnClickListener(view12 -> {
            Intent intent = new Intent(context, FileBrowser.class);
            intent.putExtra(Constants.INITIAL_DIRECTORY, localModFile.getFile().getParent().toString());
            context.startActivity(intent);
        });
        viewHolder.showInfo.setOnClickListener(view13 -> {
            ModInfoDialog dialog = new ModInfoDialog(context,localModFile);
            dialog.show();
        });
        return view;
    }
}
