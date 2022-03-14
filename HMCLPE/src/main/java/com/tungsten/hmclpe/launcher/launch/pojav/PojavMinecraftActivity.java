package com.tungsten.hmclpe.launcher.launch.pojav;

import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.WindowManager;

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

public class PojavMinecraftActivity extends BaseMainActivity {

    private GameLaunchSetting gameLaunchSetting;

    private DrawerLayout drawerLayout;
    private LayoutPanel baseLayout;

    public MenuHelper menuHelper;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameLaunchSetting = GameLaunchSetting.getGameLaunchSetting(getIntent().getExtras().getString("setting_path"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (gameLaunchSetting.fullscreen) {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        setContentView(net.kdt.pojavlaunch.R.layout.activity_pojav);

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_control_pattern,null) ;
        addContentView(drawerLayout,params);

        baseLayout = findViewById(R.id.base_layout);

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
                        if (!CallbackBridge.isGrabbing() && menuHelper.viewManager != null && menuHelper.viewManager.gameCursorMode == 1) {
                            mouseModeHandler.sendEmptyMessage(1);
                        }else{
                            if (CallbackBridge.isGrabbing() && menuHelper.viewManager != null && menuHelper.viewManager.gameCursorMode == 0) {
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
                    menuHelper.viewManager.enableCursor();
                }
                else {
                    menuHelper.viewManager.disableCursor();
                }
            }
        };

        menuHelper = new MenuHelper(this,this,gameLaunchSetting.fullscreen,gameLaunchSetting.game_directory,drawerLayout,baseLayout,false,gameLaunchSetting.controlLayout,2,scaleFactor);

        init(gameLaunchSetting.game_directory, GameLaunchSetting.isHighVersion(gameLaunchSetting));

    }

    @Override
    public void onBackPressed() {
        CallbackBridge.sendKeyPress(LWJGLGLFWKeycode.GLFW_KEY_ESCAPE);
    }

    @Override
    protected void onPause() {
        if (menuHelper.viewManager != null && menuHelper.viewManager.gameCursorMode == 1) {
            CallbackBridge.sendKeyPress(LWJGLGLFWKeycode.GLFW_KEY_ESCAPE);
        }
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (gameLaunchSetting.fullscreen) {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
    }

}
