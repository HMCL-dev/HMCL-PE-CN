package com.tungsten.hmclpe.launcher.dialogs.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.threed.jpct.Object3D;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.skin.draw3d.Character;
import com.tungsten.hmclpe.skin.draw3d.MovementHandler;
import com.tungsten.hmclpe.skin.SkinRenderer;

import java.io.InputStream;

public class SkinPreviewDialog implements View.OnClickListener {

    private LinearLayout dialog;
    private Button fakeBackground;

    private Context context;
    private MainActivity activity;
    private Account account;

    public static Object3D[] char_parts;
    private LinearLayout skinParentView;
    private SkinRenderer skinRenderer;
    private Bitmap bitmap = null;
    private MainActivity master = null;
    public MovementHandler movementHandler = new MovementHandler();

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
        this.skinRenderer = new SkinRenderer(activity, master,this);
        skinParentView = activity.findViewById(R.id.skin_parent_view);
        GLSurfaceView skinGLView = new GLSurfaceView(context);
        skinParentView.addView(skinGLView);
        skinGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLView.setRenderer(skinRenderer);
        skinGLView.setZOrderOnTop(true);
        skinGLView.setFocusable(true);
        skinGLView.setClickable(true);
        skinGLView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                movementHandler.handleMotionEvent(event);
                return false;
            }
        });
        new Thread(){
            @Override
            public void run(){
                AssetManager manager = context.getAssets();
                InputStream inputStream;
                try {
                    inputStream = manager.open("img/steve.png");
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Character var7 = new Character(context, bitmap);
                char_parts = var7.getObjects();
            }
        }.start();

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
