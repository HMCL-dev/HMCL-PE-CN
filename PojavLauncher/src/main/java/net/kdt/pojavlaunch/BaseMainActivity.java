package net.kdt.pojavlaunch;

import static net.kdt.pojavlaunch.utils.MCOptionUtils.getMcScale;
import static org.lwjgl.glfw.CallbackBridge.windowHeight;
import static org.lwjgl.glfw.CallbackBridge.windowWidth;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import net.kdt.pojavlaunch.utils.JREUtils;

import org.lwjgl.glfw.CallbackBridge;

import java.util.Vector;

public class BaseMainActivity extends BaseActivity {

    private TextureView minecraftGLView;
    public float scaleFactor = 1.0F;
    public static boolean isInputStackCall = false;

    public MouseCallback mouseCallback;

    protected void init(String gameDir, String javaPath, String home, boolean highVersion, final Vector<String> args, String renderer, ImageView mouseCursor) {

        this.minecraftGLView = findViewById(R.id.main_game_render_view);

        if (highVersion){
            isInputStackCall = true;
        }

        minecraftGLView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                surface.setDefaultBufferSize(CallbackBridge.windowWidth, CallbackBridge.windowHeight);
                CallbackBridge.sendUpdateWindowSize(CallbackBridge.windowWidth, CallbackBridge.windowHeight);
                JREUtils.setupBridgeWindow(new Surface(surface));
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            runCraft(javaPath,home,highVersion,args,renderer);
                        }catch (Throwable e){

                        }
                    }
                }.start();
                Thread virtualMouseGrabThread = new Thread(() -> {
                    while (true) {
                        if (!CallbackBridge.isGrabbing() && mouseCursor.getVisibility() != View.VISIBLE) {
                            mouseModeHandler.sendEmptyMessage(1);
                        }else{
                            if (CallbackBridge.isGrabbing() && mouseCursor.getVisibility() != View.GONE) {
                                mouseModeHandler.sendEmptyMessage(0);
                            }
                        }
                    }
                }, "VirtualMouseGrabThread");
                virtualMouseGrabThread.setPriority(Thread.MIN_PRIORITY);
                virtualMouseGrabThread.start();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
                windowWidth = Tools.getDisplayFriendlyRes(width, scaleFactor);
                windowHeight = Tools.getDisplayFriendlyRes(height, scaleFactor);
                CallbackBridge.sendUpdateWindowSize(windowWidth, windowHeight);
                getMcScale(gameDir);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
                surface.setDefaultBufferSize(windowWidth, windowHeight);
            }
        });
    }

    private void runCraft(String javaPath,String home,boolean highVersion,final Vector<String> args, String renderer) throws Throwable {
        JREUtils.redirectAndPrintJRELog();
        Tools.launchMinecraft(this, javaPath,home,renderer, args);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @SuppressLint("HandlerLeak")
    public final Handler mouseModeHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mouseCallback.onMouseModeChange(false);
            }
            if (msg.what == 1) {
                mouseCallback.onMouseModeChange(true);
            }
        }
    };

    public interface MouseCallback{
        void onMouseModeChange(boolean mode);
    }

}
