package com.tungsten.hmclpe.launcher.uis.universal.setting.right.help;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class AboutUsUI extends BaseUI implements View.OnClickListener {

    public LinearLayout aboutUsUI;

    private ImageButton hmclpe;
    private ImageButton tungs;
    private ImageButton mio;

    public AboutUsUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        aboutUsUI = activity.findViewById(R.id.ui_about);

        hmclpe = activity.findViewById(R.id.hmclpe_link);
        tungs = activity.findViewById(R.id.tungs_link);
        mio = activity.findViewById(R.id.mio_link);

        hmclpe.setOnClickListener(this);
        tungs.setOnClickListener(this);
        mio.setOnClickListener(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(aboutUsUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startAboutUsUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(aboutUsUI,activity,context,false);
        if (activity.isLoaded){
            activity.uiManager.settingUI.startAboutUsUI.setBackground(context.getResources().getDrawable(R.drawable.launcher_button_parent));
        }
    }

    @Override
    public void onClick(View view) {
        Uri uri = null;
        if (view == hmclpe) {
            uri = Uri.parse("http://tungstend.hmcl-pe.cn/");
        }
        if (view == tungs) {
            uri = Uri.parse("https://space.bilibili.com/18115101");
        }
        if (view == mio) {
            uri = Uri.parse("https://space.bilibili.com/35801833");
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
