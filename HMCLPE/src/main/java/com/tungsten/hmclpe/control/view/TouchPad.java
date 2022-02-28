package com.tungsten.hmclpe.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;

public class TouchPad extends View {

    private int launcher;
    private int screenWidth;
    private int screenHeight;
    private MenuHelper menuHelper;
    private int gameCursorMode;

    private Bitmap bitmap;
    private int cursorX;
    private int cursorY;

    private int pointerX;
    private int pointerY;

    public TouchPad(Context context,int launcher, int screenWidth, int screenHeight, MenuHelper menuHelper,int gameCursor) {
        super(context);
        this.launcher = launcher;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.menuHelper = menuHelper;
        this.gameCursorMode = gameCursor;

        cursorX = 0;
        cursorY = 0;
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_cursor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(screenWidth, screenHeight);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gameCursorMode == 0) {
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect(cursorX, cursorY, cursorX + ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize), cursorY + ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize));
            canvas.drawBitmap(bitmap, src, dst, new Paint(Paint.ANTI_ALIAS_FLAG));
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }
        return true;
    }
}
