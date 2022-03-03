package com.tungsten.hmclpe.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.control.bean.BaseButtonInfo;

public class BaseButton extends androidx.appcompat.widget.AppCompatButton {

    public int screenWidth;
    public int screenHeight;
    public BaseButtonInfo info;
    public MenuHelper menuHelper;

    public BaseButton(Context context, int screenWidth, int screenHeight, BaseButtonInfo info, MenuHelper menuHelper) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.info = info;
        this.menuHelper = menuHelper;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
