package com.tungsten.hmclpe.launcher.dialogs.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.crone.skineditorforminecraftpe.minecraftskinrender.MinecraftSkinRenderer;
import com.crone.skineditorforminecraftpe.minecraftskinrender.SkinGLSurfaceView;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.MainActivity;

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
        MinecraftSkinRenderer renderer = new MinecraftSkinRenderer(context,R.drawable.alex,true);
        skinGLSurfaceView.setRenderer(renderer,5f);
        skinParentView.addView(skinGLSurfaceView);
        //Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/alex.png");
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
