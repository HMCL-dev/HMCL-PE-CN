package com.tungsten.hmclpe.launcher.list.info.docs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tungsten.hmclpe.R;

import java.util.ArrayList;

public class DocCategoryListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<DocListBean> list;

    public DocCategoryListAdapter(Context context, ArrayList<DocListBean> list) {
        this.context = context;
        this.list = list;
    }

    private static class ViewHolder {
        LinearLayout parent;
        TextView textView;
        ListView listView;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_doc_category,null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.textView = view.findViewById(R.id.doc_category);
            viewHolder.listView = view.findViewById(R.id.doc_list);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        DocListBean bean = list.get(i);
        viewHolder.parent.setVisibility(bean.getItem().size() == 0 ? View.GONE : View.VISIBLE);
        viewHolder.textView.setText(bean.getCategory());
        DocListAdapter adapter = new DocListAdapter(context, bean.getItem());
        viewHolder.listView.setAdapter(adapter);
        if (bean.getItem().size() > 0) {
            reSetListViewHeight(viewHolder.listView, getDocListHeight(viewHolder.listView) - viewHolder.listView.getLayoutParams().height);
        }
        return view;
    }

    public int getValidSize() {
        int size = 0;
        for (DocListBean bean : list) {
            if (bean.getItem().size() > 0) {
                size++;
            }
        }
        return size;
    }

    public static int getDocListHeight(ListView listView) {
        int count = listView.getAdapter().getCount();
        View view = listView.getAdapter().getView(0, null, listView);
        view.measure(0, 0);
        return (view.getMeasuredHeight() * count) + (listView.getDividerHeight() * (count - 1));
    }

    public static void reSetListViewHeight(ListView listView,int change) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height += change;
        listView.setLayoutParams(params);
    }
}
