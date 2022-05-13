package com.tungsten.hmclpe.launcher.uis.game.download.right.resource;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.string.ModTranslations;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadResourceUI extends BaseDownloadUI implements View.OnClickListener {

    private ImageView icon;
    private TextView name;
    private TextView type;
    private TextView description;
    private LinearLayout mcmod;
    private LinearLayout mcbbs;
    private LinearLayout curseForge;

    public DownloadResourceUI(Context context, MainActivity activity, ModListBean.Mod bean) {
        super(context, activity, bean);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        icon = findViewById(R.id.resource_icon);
        name = findViewById(R.id.resource_name);
        type = findViewById(R.id.resource_type);
        description = findViewById(R.id.resource_description);
        mcmod = findViewById(R.id.mcmod_link);
        mcbbs = findViewById(R.id.mcbbs_link);
        curseForge = findViewById(R.id.curse_forge_link);

        mcmod.setOnClickListener(this);
        mcbbs.setOnClickListener(this);
        curseForge.setOnClickListener(this);

        new Thread(() -> {
            try {
                URL url = new URL(bean.getIconUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                activity.runOnUiThread(() -> {
                    icon.setImageBitmap(bitmap);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        name.setText(ModTranslations.getDisplayName(bean.getTitle(),bean.getSlug()));
        description.setText(bean.getDescription());

        curseForge.setVisibility(bean.getPageUrl() != null ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view == mcmod) {

        }
        if (view == mcbbs) {

        }
        if (view == curseForge) {
            Uri uri = Uri.parse(bean.getPageUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

    public static String getMcmodUrl(String mcmodId) {
        return String.format("https://www.mcmod.cn/class/%s.html", mcmodId);
    }

    public static String getMcbbsUrl(String mcbbsId) {
        return String.format("https://www.mcbbs.net/thread-%s-1-1.html", mcbbsId);
    }
}
