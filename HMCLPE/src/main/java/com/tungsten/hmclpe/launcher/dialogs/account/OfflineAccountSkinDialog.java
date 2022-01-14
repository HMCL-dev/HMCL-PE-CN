package com.tungsten.hmclpe.launcher.dialogs.account;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.threed.jpct.Object3D;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.skin.Character;
import com.tungsten.hmclpe.skin.MovementHandler;
import com.tungsten.hmclpe.skin.SkinRenderer;

import java.io.InputStream;

public class OfflineAccountSkinDialog extends Dialog implements View.OnClickListener, View.OnTouchListener {

    private Account account;

    public static Object3D[] char_parts;
    private GLSurfaceView skinGLView;
    private SkinRenderer skinRenderer;
    private Bitmap bitmap = null;
    private OfflineAccountSkinDialog master = null;
    public MovementHandler movementHandler = new MovementHandler();

    public OfflineAccountSkinDialog(@NonNull Context context, Account account) {
        super(context);
        setContentView(R.layout.dialog_offline_skin);
        this.account = account;
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        this.skinRenderer = new SkinRenderer(this, master);
        skinGLView = findViewById(R.id.skin_gl_view);
        skinGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLView.setRenderer(skinRenderer);
        new Thread(){
            @Override
            public void run(){
                AssetManager manager = getContext().getAssets();
                InputStream inputStream;
                try {
                    inputStream = manager.open("img/alex.png");
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    handler.sendEmptyMessage(0);
                }
                catch (Exception e){
                    handler.sendEmptyMessage(1);
                }
                Character var7 = new Character(OfflineAccountSkinDialog.this.getContext(), bitmap);
                char_parts = var7.getObjects();
            }
        }.start();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    @SuppressLint("HandlerLeak")
    private final android.os.Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 1){
                Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
