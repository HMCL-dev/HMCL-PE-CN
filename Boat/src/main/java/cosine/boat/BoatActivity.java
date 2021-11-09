package cosine.boat;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BoatActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{

    private TextureView mainTextureView;

    public static native void setBoatNativeWindow(Surface surface);

    static {
        System.loadLibrary("boat");
    }

    public void initLayout(){
        setContentView(R.layout.activity_boat);
        mainTextureView = findViewById(R.id.main_texture_view);
        mainTextureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        System.out.println("SurfaceTexture is available!");
        BoatActivity.setBoatNativeWindow(new Surface(surface));
        new Thread() {
            @Override
            public void run() {
                LauncherConfig config = LauncherConfig.fromFile(getIntent().getExtras().getString("config"));
                LoadMe.exec(config);
            }
        }.start();
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
}
