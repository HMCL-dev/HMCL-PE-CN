package com.tungsten.hmclpe.launcher.list.download.mod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.download.resources.mods.ModListBean;
import com.tungsten.hmclpe.launcher.download.resources.SearchTools;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.resources.DrawableUtils;
import com.tungsten.hmclpe.utils.string.ModTranslations;

import java.util.ArrayList;

public class DownloadModListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModListBean.Mod> modList;

    private class ViewHolder{
        LinearLayout modItem;
        ImageView modIcon;
        TextView modName;
        TextView modCategories;
        TextView modIntroduction;
    }

    public DownloadModListAdapter (Context context, ArrayList<ModListBean.Mod> modList){
        this.context = context;
        this.modList = modList;
    }

    @Override
    public int getCount() {
        return modList.size();
    }

    @Override
    public Object getItem(int position) {
        return modList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_mod_list,null);
            viewHolder.modItem = convertView.findViewById(R.id.item);
            viewHolder.modIcon = convertView.findViewById(R.id.mod_icon);
            viewHolder.modName = convertView.findViewById(R.id.mod_name);
            viewHolder.modCategories = convertView.findViewById(R.id.mod_categories);
            viewHolder.modIntroduction = convertView.findViewById(R.id.mod_introduction);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.modIcon.setImageDrawable(context.getDrawable(R.drawable.launcher_background_color_white));
        viewHolder.modIcon.setTag(position);
        FileDownloader.setup(context);
        FileDownloader.getImpl().create(modList.get(position).getIconUrl())
                .setPath(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + modList.get(position).getTitle().replace(" ","_") + ".png")
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
                        if (viewHolder.modIcon.getTag().equals(position)){
                            viewHolder.modIcon.setImageDrawable(DrawableUtils.getDrawableFromFile(AppManifest.DEFAULT_CACHE_DIR + "/icon_" + modList.get(position).getTitle().replace(" ","_") + ".png"));
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
        for (int i = 0;i < modList.get(position).getCategories().size();i++){
            //categories = categories + SearchTools.getCategoryFromID(context,modList.get(position).getCategories().get(i)) + "  ";
        }
        viewHolder.modCategories.setText(categories);
        viewHolder.modName.setText(ModTranslations.getDisplayName(modList.get(position).getTitle(),modList.get(position).getSlug()));
        viewHolder.modIntroduction.setText(modList.get(position).getDescription());
        viewHolder.modItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        return convertView;
    }
}
