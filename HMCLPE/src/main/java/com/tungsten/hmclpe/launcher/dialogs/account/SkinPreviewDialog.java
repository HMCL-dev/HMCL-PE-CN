package com.tungsten.hmclpe.launcher.dialogs.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.skin.MinecraftSkinRenderer;
import com.tungsten.hmclpe.skin.SkinGLSurfaceView;

public class SkinPreviewDialog implements View.OnClickListener {

    private LinearLayout dialog;
    private Button fakeBackground;

    private Context context;
    private MainActivity activity;
    private Account account;

    private LinearLayout skinParentView;

    private Button positive;
    private Button negative;

    public SkinPreviewDialog(Context context, MainActivity activity, Account account){
        this.context = context;
        this.activity = activity;
        this.account = account;
        init();
    }

    public void show(){
        activity.dialogMode = true;

        dialog = activity.findViewById(R.id.dialog_offline_skin);
        fakeBackground = activity.findViewById(R.id.fake_dialog_background);

        dialog.setVisibility(View.VISIBLE);
        fakeBackground.setVisibility(View.VISIBLE);
    }

    public void dismiss(){
        activity.dialogMode = false;

        skinParentView.removeAllViews();

        dialog.setVisibility(View.GONE);
        fakeBackground.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        skinParentView = activity.findViewById(R.id.skin_parent_view);

        SkinGLSurfaceView skinGLSurfaceView = new SkinGLSurfaceView(context);
        skinGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        skinGLSurfaceView.setZOrderOnTop(true);
        MinecraftSkinRenderer renderer = new MinecraftSkinRenderer(context,R.drawable.skin_steve,false);
        skinGLSurfaceView.setRenderer(renderer,5f);
        skinGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        skinGLSurfaceView.setPreserveEGLContextOnPause(true);
        skinParentView.addView(skinGLSurfaceView);
        //renderer.updateTexture(bitmap);

        positive = activity.findViewById(R.id.edit_skin_positive);
        negative = activity.findViewById(R.id.cancel_edit_skin);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == positive){

        }
        if (v == negative){
            dismiss();
        }
    }
}
