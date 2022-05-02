package net.kdt.pojavlaunch;

import static net.kdt.pojavlaunch.utils.MCOptionUtils.getMcScale;
import static org.lwjgl.glfw.CallbackBridge.windowHeight;
import static org.lwjgl.glfw.CallbackBridge.windowWidth;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import net.kdt.pojavlaunch.utils.JREUtils;

import org.lwjgl.glfw.CallbackBridge;

import java.util.Vector;

public class BaseMainActivity extends AppCompatActivity {

    public TextureView minecraftGLView;
    public float scaleFactor = 1.0F;
    public static boolean isInputStackCall;

    public PojavCallback pojavCallback;

    protected void init(String gameDir , boolean highVersion) {

        isInputStackCall = highVersion;

        minecraftGLView = findViewById(R.id.main_game_render_view);
        minecraftGLView.setOpaque(false);
        minecraftGLView.setFocusable(true);

        minecraftGLView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                pojavCallback.onSurfaceTextureAvailable(surface,width,height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
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

    public void startGame(String javaPath,String home,boolean highVersion,final Vector<String> args, String renderer,String gameDir) {
        Thread JVMThread = new Thread(() -> {
            try {
                JREUtils.redirectAndPrintJRELog(this);
                Tools.launchMinecraft(this, javaPath,home,renderer, args,gameDir);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }, "JVM Main thread");
        JVMThread.setPriority(Thread.MAX_PRIORITY);
        JVMThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CallbackBridge.nativeSetWindowAttrib(LWJGLGLFWKeycode.GLFW_VISIBLE, 1);
    }

    @Override
    protected void onStop() {
        CallbackBridge.nativeSetWindowAttrib(LWJGLGLFWKeycode.GLFW_VISIBLE, 0);
        super.onStop();
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
                pojavCallback.onMouseModeChange(false);
            }
            if (msg.what == 1) {
                pojavCallback.onMouseModeChange(true);
            }
        }
    };

    public interface PojavCallback{
        void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height);
        void onMouseModeChange(boolean mode);
    }

}
