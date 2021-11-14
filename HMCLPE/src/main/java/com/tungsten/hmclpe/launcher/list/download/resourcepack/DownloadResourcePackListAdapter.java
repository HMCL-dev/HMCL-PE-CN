package com.tungsten.hmclpe.launcher.list.download.resourcepack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.list.download.world.DownloadWorldListAdapter;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.resources.DrawableUtils;

import java.util.ArrayList;

public class DownloadResourcePackListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModListBean.Mod> resourcePackList;

    private class ViewHolder{
        LinearLayout resourcePackItem;
        ImageView resourcePackIcon;
        TextView resourcePackName;
        TextView resourcePackCategories;
        TextView resourcePackIntroduction;
    }

    public DownloadResourcePackListAdapter (Context context, ArrayList<ModListBean.Mod> resourcePackList){
        this.context = context;
        this.resourcePackList = resourcePackList;
    }

    @Override
    public int getCount() {
        return resourcePackList.size();
    }

    @Override
    public Object getItem(int position) {
        return resourcePackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_resource_pack_list,null);
            viewHolder.resourcePackItem = convertView.findViewById(R.id.item);
            viewHolder.resourcePackIcon = convertView.findViewById(R.id.resource_pack_icon);
            viewHolder.resourcePackName = convertView.findViewById(R.id.resource_pack_name);
            viewHolder.resourcePackCategories = convertView.findViewById(R.id.resource_pack_categories);
            viewHolder.resourcePackIntroduction = convertView.findViewById(R.id.resource_pack_introduction);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.resourcePackIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.resourcePackIcon.setTag(position);
        FileDownloader.setup(context);
        FileDownloader.getImpl().create(resourcePackList.get(position).getIconUrl())
                .setPath(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + resourcePackList.get(position).getTitle().replace(" ","_") + ".png")
                .setTag(position)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        if (viewHolder.resourcePackIcon.getTag().equals(position)){
                            viewHolder.resourcePackIcon.setImageDrawable(DrawableUtils.getDrawableFromFile(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + resourcePackList.get(position).getTitle().replace(" ","_") + ".png"));
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
        String categories = "";
        for (int i = 0;i < resourcePackList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,worldList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.resourcePackCategories.setText(categories);
        viewHolder.resourcePackName.setText(resourcePackList.get(position).getTitle());
        viewHolder.resourcePackIntroduction.setText(resourcePackList.get(position).getDescription());
        viewHolder.resourcePackItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
