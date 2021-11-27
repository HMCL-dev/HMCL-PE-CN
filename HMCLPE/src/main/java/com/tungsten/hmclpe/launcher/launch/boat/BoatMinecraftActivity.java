package com.tungsten.hmclpe.launcher.launch.boat;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tungsten.hmclpe.R;

import cosine.boat.BoatActivity;

public class BoatMinecraftActivity extends BoatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout(R.layout.activity_minecraft);

        this.setOnCursorChangeListener(new OnActivityChangeListener() {
            @Override
            public void onSurfaceTextureAvailable() {

            }

            @Override
            public void onCursorModeChange(int mode) {
                
            }
        });
    }
}
