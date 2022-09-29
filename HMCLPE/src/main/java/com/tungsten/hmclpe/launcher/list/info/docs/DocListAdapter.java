package com.tungsten.hmclpe.launcher.list.info.docs;

import static com.tungsten.hmclpe.launcher.uis.universal.setting.right.help.HelpUI.HMCL_PE_DOC_PAGE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tungsten.hmclpe.R;

import java.util.ArrayList;

public class DocListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Doc> list;

    public DocListAdapter(Context context, ArrayList<Doc> list) {
        this.context = context;
        this.list = list;
    }

    private static class ViewHolder {
        TextView title;
        TextView subTitle;
        ImageButton link;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_doc,null);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.subTitle = view.findViewById(R.id.subtitle);
            viewHolder.link = view.findViewById(R.id.link);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        Doc doc = list.get(i);
        viewHolder.title.setText(doc.getTitle());
        viewHolder.subTitle.setText(doc.getSubtitle());
        viewHolder.link.setOnClickListener(view1 -> {
            Uri uri = Uri.parse(HMCL_PE_DOC_PAGE + "?path=" + doc.getPath());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });
        return view;
    }
}
