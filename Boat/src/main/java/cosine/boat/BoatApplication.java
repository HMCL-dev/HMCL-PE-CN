package cosine.boat;

import android.app.Application;
import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class BoatApplication extends Application implements Application.ActivityLifecycleCallbacks
{
	public static Activity mCurrentActivity;
	public static Activity getCurrentActivity(){
		return BoatApplication.mCurrentActivity;
	}
	public static BoatApplication mInstance = null;
	public SharedPreferences mPref;

	public static BoatApplication getInstance() {
		return mInstance;
	}

	private boolean is_OTG = false;

	public boolean is_OTG() {
		return is_OTG;
	}

	public void setIs_OTG(boolean is_OTG) {
		this.is_OTG = is_OTG;
	}

	@Override
	public void onActivityCreated(Activity p1, Bundle p2)
	{
		// TODO: Implement this method
		
	}

	@Override
	public void onActivityStarted(Activity p1)
	{
		// TODO: Implement this method
		BoatApplication.mCurrentActivity = p1;
		System.out.println(BoatApplication.mCurrentActivity);
	}

	@Override
	public void onActivityResumed(Activity p1)
	{
		// TODO: Implement this method

	}

	@Override
	public void onActivityPaused(Activity p1)
	{
		// TODO: Implement this method

	}

	@Override
	public void onActivityStopped(Activity p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void onActivitySaveInstanceState(Activity p1, Bundle p2)
	{
		// TODO: Implement this method
	}

	@Override
	public void onActivityDestroyed(Activity p1)
	{
		// TODO: Implement this method
	}


	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();

	}

	/**
	 * 监听程序崩溃/重启
	 */
	private static class CustomEventListener {

	}

	public void setAppColorTheme(){
		//setTheme(R.style.Theme_Boat_H2O2_Custom_GREEN);
	}

}
