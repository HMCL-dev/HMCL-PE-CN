package com.tungsten.hmclpe.control;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.hmclpe.R;

public class ControlPatternActivity extends AppCompatActivity implements View.OnTouchListener {

    private DrawerLayout drawerLayout;
    private RelativeLayout baseLayout;

    public MenuHelper menuHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control_pattern);

        drawerLayout = findViewById(R.id.drawer_layout);
        baseLayout = findViewById(R.id.base_layout);

        menuHelper = new MenuHelper(this,this,drawerLayout,baseLayout);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
