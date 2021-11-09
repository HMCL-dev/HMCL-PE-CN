package com.tungsten.hmclpe.launcher.list.download.world;

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
import com.tungsten.hmclpe.launcher.download.resources.SearchTools;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.resources.DrawableUtils;
import com.tungsten.hmclpe.utils.string.ModTranslations;

import java.util.ArrayList;

public class DownloadWorldListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModListBean.Mod> worldList;

    private class ViewHolder{
        LinearLayout worldItem;
        ImageView worldIcon;
        TextView worldName;
        TextView worldCategories;
        TextView worldIntroduction;
    }

    public DownloadWorldListAdapter (Context context, ArrayList<ModListBean.Mod> worldList){
        this.context = context;
        this.worldList = worldList;
    }

    @Override
    public int getCount() {
        return worldList.size();
    }

    @Override
    public Object getItem(int position) {
        return worldList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_world_list,null);
            viewHolder.worldItem = convertView.findViewById(R.id.item);
            viewHolder.worldIcon = convertView.findViewById(R.id.world_icon);
            viewHolder.worldName = convertView.findViewById(R.id.world_name);
            viewHolder.worldCategories = convertView.findViewById(R.id.world_categories);
            viewHolder.worldIntroduction = convertView.findViewById(R.id.world_introduction);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.worldIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.worldIcon.setTag(position);
        FileDownloader.setup(context);
        FileDownloader.getImpl().create(worldList.get(position).getIconUrl())
                .setPath(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + worldList.get(position).getTitle().replace(" ","_") + ".png")
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
                        if (viewHolder.worldIcon.getTag().equals(position)){
                            viewHolder.worldIcon.setImageDrawable(DrawableUtils.getDrawableFromFile(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + worldList.get(position).getTitle().replace(" ","_") + ".png"));
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
        for (int i = 0;i < worldList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,worldList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.worldCategories.setText(categories);
        viewHolder.worldName.setText(worldList.get(position).getTitle());
        viewHolder.worldIntroduction.setText(worldList.get(position).getDescription());
        viewHolder.worldItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
