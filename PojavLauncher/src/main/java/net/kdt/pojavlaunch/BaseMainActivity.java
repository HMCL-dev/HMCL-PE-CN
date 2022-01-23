package net.kdt.pojavlaunch;

import static net.kdt.pojavlaunch.Tools.currentDisplayMetrics;
import static org.lwjgl.glfw.CallbackBridge.windowHeight;
import static org.lwjgl.glfw.CallbackBridge.windowWidth;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import net.kdt.pojavlaunch.utils.JREUtils;

import java.util.Vector;

public class BaseMainActivity extends BaseActivity {

    private TextureView minecraftGLView;

    protected void initLayout(String javaPath,String home,boolean highVersion,final Vector<String> args, String renderer) {
        setContentView(R.layout.activity_pojav);

        this.minecraftGLView = findViewById(R.id.main_game_render_view);

        minecraftGLView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                JREUtils.setupBridgeWindow(new Surface(surface));
                try {
                    runCraft(javaPath,home,highVersion,args,renderer);
                }catch (Throwable e){

                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }
        });
    }

    private void runCraft(String javaPath,String home,boolean highVersion,final Vector<String> args, String renderer) throws Throwable {
        
        JREUtils.jreReleaseList = JREUtils.readJREReleaseProperties(javaPath);

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

}
