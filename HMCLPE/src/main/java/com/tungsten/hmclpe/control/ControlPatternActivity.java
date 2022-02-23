package com.tungsten.hmclpe.control;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.view.LayoutPanel;

public class ControlPatternActivity extends AppCompatActivity implements View.OnTouchListener {

    public static final int CONTROL_PATTERN_REQUEST_CODE = 3000;

    private DrawerLayout drawerLayout;
    private LayoutPanel baseLayout;

    public MenuHelper menuHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control_pattern);

        drawerLayout = findViewById(R.id.drawer_layout);
        baseLayout = findViewById(R.id.base_layout);

        menuHelper = new MenuHelper(this,this,drawerLayout,baseLayout,true,getIntent().getExtras().getString("pattern"));
        menuHelper.initialPattern = getIntent().getExtras().getString("initial");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onBackPressed() {
        String pattern;
        if (menuHelper.initialPattern == null){
            pattern = getIntent().getExtras().getString("initial");
        }
        else {
            pattern = menuHelper.initialPattern;
        }
        Intent data = new Intent();
        data.setData(Uri.parse(pattern));
        setResult(Activity.RESULT_OK,data);
        super.onBackPressed();
    }
}
