package net.kdt.pojavlaunch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.setFullscreen(this);
        Tools.updateWindowSize(this);
    }

    @Override
    public void startActivity(Intent i) {
        super.startActivity(i);
        new Throwable("StartActivity").printStackTrace();
    }
}
