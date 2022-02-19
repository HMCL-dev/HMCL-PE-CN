package com.tungsten.hmclpe.control;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.view.MenuView;

public class ControlPatternActivity extends AppCompatActivity implements View.OnTouchListener {

    private RelativeLayout baseLayout;

    private MenuView menuView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control_pattern);

        baseLayout = findViewById(R.id.base_layout);

        baseLayout.post(new Runnable() {
            @Override
            public void run() {
                menuView = new MenuView(ControlPatternActivity.this,baseLayout.getWidth(),baseLayout.getHeight(),MenuView.MENU_MODE_LEFT,0.2f);
                menuView.addCallback(new MenuView.MenuCallback() {
                    @Override
                    public void onRelease() {

                    }

                    @Override
                    public void onMoveModeStart() {

                    }

                    @Override
                    public void onMove(int mode, float yPercent) {

                    }

                    @Override
                    public void onMoveModeStop() {

                    }
                });
                baseLayout.addView(menuView);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
