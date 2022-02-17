package com.tungsten.hmclpe.launcher.launch.boat;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;

import java.util.Timer;
import java.util.TimerTask;

import cosine.boat.BoatActivity;
import cosine.boat.BoatInput;
import cosine.boat.keyboard.BoatKeycodes;

public class BoatMinecraftActivity extends BoatActivity implements View.OnTouchListener {

    private RelativeLayout baseLayout;

    private ImageView mouseCursor;
    private Button baseTouchPad;

    private int initialX;
    private int initialY;
    private int baseX;
    private int baseY;

    private long downTime;
    private Timer longClickTimer;

    private boolean customSettingPointer = false;
    private boolean padSettingPointer = false;

    private int cursorMode = BoatInput.CursorEnabled;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameLaunchSetting gameLaunchSetting = GameLaunchSetting.getGameLaunchSetting(getIntent().getExtras().getString("setting_path"));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        baseLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_minecraft,null) ;
        addContentView(baseLayout,params);

        mouseCursor = findViewById(R.id.mouse_cursor);
        baseTouchPad = findButton(R.id.base_touch_pad);

        scaleFactor = gameLaunchSetting.scaleFactor;

        this.setBoatCallback(new BoatCallback() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                surface.setDefaultBufferSize((int) (width * scaleFactor), (int) (height * scaleFactor));
                BoatActivity.setBoatNativeWindow(new Surface(surface));
                startGame(gameLaunchSetting.javaPath,
                        gameLaunchSetting.home,
                        GameLaunchSetting.isHighVersion(gameLaunchSetting),
                        BoatLauncher.getMcArgs(gameLaunchSetting,BoatMinecraftActivity.this,(int) (width * scaleFactor), (int) (height * scaleFactor),gameLaunchSetting.server),
                        gameLaunchSetting.boatRenderer);
            }

            @Override
            public void onCursorModeChange(int mode) {
                cursorMode = mode;
                cursorModeHandler.sendEmptyMessage(mode);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler cursorModeHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == BoatInput.CursorDisabled){
                mouseCursor.setVisibility(View.INVISIBLE);
            }
            if (msg.what == BoatInput.CursorEnabled){
                mouseCursor.setVisibility(View.VISIBLE);
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private Button findButton(int id){
        Button button = findViewById(id);
        button.setOnTouchListener(this);
        return button;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == baseTouchPad){
            if (cursorMode == BoatInput.CursorDisabled){
                switch(event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        initialX = (int)event.getX();
                        initialY = (int)event.getY();
                        downTime = System.currentTimeMillis();
                        longClickTimer = new Timer();
                        longClickTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                BoatInput.setMouseButton(BoatInput.Button1,true);
                            }
                        },400);
                    case MotionEvent.ACTION_MOVE:
                        if (!customSettingPointer){
                            padSettingPointer = true;
                            BoatInput.setPointer(baseX + (int)event.getX() -initialX, baseY + (int)event.getY() - initialY);
                        }
                        if (Math.abs(event.getX() - initialX) > 10 && Math.abs(event.getY() - initialY) > 10){
                            longClickTimer.cancel();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        longClickTimer.cancel();
                        if (padSettingPointer){
                            baseX += ((int)event.getX() - initialX);
                            baseY += ((int)event.getY() - initialY);
                            BoatInput.setPointer(baseX,baseY);
                            padSettingPointer = false;
                        }
                        BoatInput.setMouseButton(BoatInput.Button1,false);
                        if (Math.abs(event.getX() - initialX) <= 10 && Math.abs(event.getY() - initialY) <= 10 && System.currentTimeMillis() - downTime <= 200){
                            BoatInput.setMouseButton(BoatInput.Button3,true);
                            BoatInput.setMouseButton(BoatInput.Button3,false);
                        }
                        break;
                    default:
                        break;
                }
            }
            else {
                baseX = (int)event.getX();
                baseY = (int)event.getY();
                BoatInput.setPointer((int) (baseX * scaleFactor),(int) (baseY * scaleFactor));
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setMouseButton(BoatInput.Button1,true);
                }
                if (event.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setMouseButton(BoatInput.Button1,false);
                }
            }
            mouseCursor.setX(event.getX());
            mouseCursor.setY(event.getY());
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape,0,true);
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape,0,false);
    }
}
