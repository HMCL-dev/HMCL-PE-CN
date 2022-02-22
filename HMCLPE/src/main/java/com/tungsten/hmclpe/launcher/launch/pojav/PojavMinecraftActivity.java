package com.tungsten.hmclpe.launcher.launch.pojav;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.control.view.LayoutPanel;
import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;

import net.kdt.pojavlaunch.BaseMainActivity;
import net.kdt.pojavlaunch.LWJGLGLFWKeycode;
import net.kdt.pojavlaunch.utils.JREUtils;
import net.kdt.pojavlaunch.utils.MCOptionUtils;

import org.lwjgl.glfw.CallbackBridge;

import java.util.Timer;
import java.util.TimerTask;

public class PojavMinecraftActivity extends BaseMainActivity implements View.OnTouchListener {

    private DrawerLayout drawerLayout;
    private LayoutPanel baseLayout;

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

    public MenuHelper menuHelper;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(net.kdt.pojavlaunch.R.layout.activity_pojav);

        GameLaunchSetting gameLaunchSetting = GameLaunchSetting.getGameLaunchSetting(getIntent().getExtras().getString("setting_path"));

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_minecraft,null) ;
        addContentView(drawerLayout,params);

        baseLayout = findViewById(R.id.base_layout);

        mouseCursor = findViewById(R.id.mouse_cursor);
        baseTouchPad = findButton(R.id.base_touch_pad);

        scaleFactor = gameLaunchSetting.scaleFactor;

        pojavCallback = new PojavCallback() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                CallbackBridge.windowWidth = (int) (width * scaleFactor);
                CallbackBridge.windowHeight = (int) (height * scaleFactor);

                MCOptionUtils.load(gameLaunchSetting.game_directory);
                MCOptionUtils.set("overrideWidth", String.valueOf(CallbackBridge.windowWidth));
                MCOptionUtils.set("overrideHeight", String.valueOf(CallbackBridge.windowHeight));
                MCOptionUtils.save(gameLaunchSetting.game_directory);

                surface.setDefaultBufferSize(CallbackBridge.windowWidth, CallbackBridge.windowHeight);
                CallbackBridge.sendUpdateWindowSize(CallbackBridge.windowWidth, CallbackBridge.windowHeight);
                JREUtils.setupBridgeWindow(new Surface(surface));
                Thread JVMThread = new Thread(() -> {
                    try {
                        startGame(gameLaunchSetting.javaPath,
                                gameLaunchSetting.home,
                                GameLaunchSetting.isHighVersion(gameLaunchSetting),
                                PojavLauncher.getMcArgs(gameLaunchSetting, PojavMinecraftActivity.this,(int) (width * scaleFactor),(int) (height * scaleFactor),gameLaunchSetting.server),
                                gameLaunchSetting.pojavRenderer);
                    } catch (Throwable e) {

                    }
                }, "JVM Main thread");
                JVMThread.setPriority(Thread.MAX_PRIORITY);
                JVMThread.start();
                Thread virtualMouseGrabThread = new Thread(() -> {
                    while (true) {
                        if (!CallbackBridge.isGrabbing() && mouseCursor.getVisibility() != View.VISIBLE) {
                            mouseModeHandler.sendEmptyMessage(1);
                        }else{
                            if (CallbackBridge.isGrabbing() && mouseCursor.getVisibility() != View.INVISIBLE) {
                                mouseModeHandler.sendEmptyMessage(0);
                            }
                        }
                    }
                }, "VirtualMouseGrabThread");
                virtualMouseGrabThread.setPriority(Thread.MIN_PRIORITY);
                virtualMouseGrabThread.start();
            }

            @Override
            public void onMouseModeChange(boolean mode) {
                if (mode){
                    mouseCursor.setVisibility(View.VISIBLE);
                }
                else {
                    mouseCursor.setVisibility(View.INVISIBLE);
                }
            }
        };

        init(gameLaunchSetting.game_directory, GameLaunchSetting.isHighVersion(gameLaunchSetting));

        menuHelper = new MenuHelper(this,this,drawerLayout,baseLayout,false,gameLaunchSetting.controlLayout);

    }

    @SuppressLint("ClickableViewAccessibility")
    private Button findButton(int id){
        Button button = findViewById(id);
        button.setOnTouchListener(this);
        return button;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == baseTouchPad){
            if (CallbackBridge.isGrabbing()){
                switch(event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        initialX = (int)event.getX();
                        initialY = (int)event.getY();
                        downTime = System.currentTimeMillis();
                        longClickTimer = new Timer();
                        longClickTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                CallbackBridge.sendMouseButton(LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_LEFT,true);
                            }
                        },400);
                    case MotionEvent.ACTION_MOVE:
                        if (!customSettingPointer){
                            padSettingPointer = true;
                            CallbackBridge.sendCursorPos(baseX + (int)event.getX() -initialX, baseY + (int)event.getY() - initialY);
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
                            CallbackBridge.sendCursorPos(baseX,baseY);
                            padSettingPointer = false;
                        }
                        CallbackBridge.sendMouseButton(LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_LEFT,false);
                        if (Math.abs(event.getX() - initialX) <= 10 && Math.abs(event.getY() - initialY) <= 10 && System.currentTimeMillis() - downTime <= 200){
                            CallbackBridge.sendMouseKeycode(LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_RIGHT);
                        }
                        break;
                    default:
                        break;
                }
            }
            else {
                baseX = (int)event.getX();
                baseY = (int)event.getY();
                CallbackBridge.sendCursorPos(baseX * scaleFactor,baseY * scaleFactor);
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> CallbackBridge.sendMouseButton(LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_LEFT,true), 1);
                }
                if (event.getActionMasked() == MotionEvent.ACTION_UP){
                    CallbackBridge.sendMouseButton(LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_LEFT,false);
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
        CallbackBridge.sendKeyPress(LWJGLGLFWKeycode.GLFW_KEY_ESCAPE);
    }

}
