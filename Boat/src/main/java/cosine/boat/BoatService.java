package cosine.boat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BoatService extends Service {

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> LoadMe.startVirGLService(this,getExternalFilesDir("debug").getAbsolutePath(),getCacheDir().getAbsolutePath())).start();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service doesn't support to be bound.
        return null;
    }
}