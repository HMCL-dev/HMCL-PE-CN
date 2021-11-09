package com.tungsten.hmclpe.launcher.launch.boat;

import android.os.Bundle;

import androidx.annotation.Nullable;

import cosine.boat.BoatActivity;

public class BoatMinecraftActivity extends BoatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
    }
}
