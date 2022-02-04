package cosine.boat;

import android.os.Bundle;
import android.view.TextureView;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Vector;


public class BoatActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener
{

	private TextureView mainTextureView;
	public BoatCallback boatCallback;
	public float scaleFactor = 1.0F;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		nOnCreate();

		setContentView(R.layout.activity_boat);

		mainTextureView = findViewById(R.id.main_surface);
		mainTextureView.setSurfaceTextureListener(this);
	}
	
	public static native void setBoatNativeWindow(Surface surface);

	public native void nOnCreate();
	
	static {
		System.loadLibrary("boat");
	}
	
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

		// TODO: Implement this method
		System.out.println("SurfaceTexture is available!");

		boatCallback.onSurfaceTextureAvailable(surface,width,height);
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture p1, int p2, int p3)
	{
		// TODO: Implement this method
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture p1)
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture p1)
	{
		// TODO: Implement this method
	}

	public void startGame(final String javaPath,final String home,final boolean highVersion,final Vector<String> args,String renderer){
		new Thread(){
			@Override
			public void run(){
				LoadMe.launchMinecraft(javaPath,home,highVersion,args,renderer);
			}
		}.start();
	}

	public void setCursorMode(int mode){
		boatCallback.onCursorModeChange(mode);
	}

	public interface BoatCallback{
		void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height);
		void onCursorModeChange(int mode);
	}

	public void setBoatCallback(BoatCallback callback){
		this.boatCallback = callback;
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



