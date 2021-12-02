package cosine.boat;

import android.view.TextureView;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Vector;


public class BoatActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener
{

	private TextureView mainTextureView;
	public OnActivityChangeListener activityChangeListener;

	public void initLayout(int resId){
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		nOnCreate();

		setContentView(resId);

		mainTextureView = new TextureView(this);
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
		BoatActivity.setBoatNativeWindow(new Surface(surface));

		activityChangeListener.onSurfaceTextureAvailable();
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

	public void startGame(final String runtimePath,final String home,final boolean highVersion,final Vector<String> args){
		new Thread(){
			@Override
			public void run(){
				LoadMe.launchMinecraft(runtimePath,home,highVersion,args);
			}
		}.start();
	}

	public void setCursorMode(int mode){
		activityChangeListener.onCursorModeChange(mode);
	}

	public interface OnActivityChangeListener{
		void onSurfaceTextureAvailable();
		void onCursorModeChange(int mode);
	}

	public void setOnCursorChangeListener(OnActivityChangeListener listener){
		this.activityChangeListener=listener;
	}
}



