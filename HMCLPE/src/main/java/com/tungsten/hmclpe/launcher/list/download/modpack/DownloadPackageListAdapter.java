package com.tungsten.hmclpe.launcher.list.download.modpack;

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

public class DownloadPackageListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModListBean.Mod> packageList;

    private class ViewHolder{
        LinearLayout packageItem;
        ImageView packageIcon;
        TextView packageName;
        TextView packageCategories;
        TextView packageIntroduction;
    }

    public DownloadPackageListAdapter (Context context, ArrayList<ModListBean.Mod> packageList){
        this.context = context;
        this.packageList = packageList;
    }

    @Override
    public int getCount() {
        return packageList.size();
    }

    @Override
    public Object getItem(int position) {
        return packageList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_package_list,null);
            viewHolder.packageItem = convertView.findViewById(R.id.item);
            viewHolder.packageIcon = convertView.findViewById(R.id.package_icon);
            viewHolder.packageName = convertView.findViewById(R.id.package_name);
            viewHolder.packageCategories = convertView.findViewById(R.id.package_categories);
            viewHolder.packageIntroduction = convertView.findViewById(R.id.package_introduction);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.packageIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.packageIcon.setTag(position);
        FileDownloader.setup(context);
        FileDownloader.getImpl().create(packageList.get(position).getIconUrl())
                .setPath(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + packageList.get(position).getTitle().replace(" ","_") + ".png")
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
                        if (viewHolder.packageIcon.getTag().equals(position)){
                            viewHolder.packageIcon.setImageDrawable(DrawableUtils.getDrawableFromFile(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + packageList.get(position).getTitle().replace(" ","_") + ".png"));
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
        for (int i = 0;i < packageList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,worldList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.packageCategories.setText(categories);
        viewHolder.packageName.setText(packageList.get(position).getTitle());
        viewHolder.packageIntroduction.setText(packageList.get(position).getDescription());
        viewHolder.packageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
