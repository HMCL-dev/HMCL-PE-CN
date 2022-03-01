package com.tungsten.hmclpe.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.InputBridge;
import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;

public class TouchPad extends View {

    private int launcher;
    private int screenWidth;
    private int screenHeight;
    private MenuHelper menuHelper;

    private Bitmap bitmap;
    private float cursorX;
    private float cursorY;
    private float startCursorX;
    private float startCursorY;

    private float initialX;
    private float initialY;
    private long downTime;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (menuHelper.gameMenuSetting.touchMode == 0){
                InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,true);
            }
            if (menuHelper.gameMenuSetting.touchMode == 1){
                InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_RIGHT,true);
            }
        }
    };

    public TouchPad(Context context,int launcher, int screenWidth, int screenHeight, MenuHelper menuHelper) {
        super(context);
        this.launcher = launcher;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.menuHelper = menuHelper;

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
        if (menuHelper.viewManager.gameCursorMode == 0) {
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect((int) cursorX, (int) cursorY, (int) cursorX + ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize), (int) cursorY + ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize));
            canvas.drawBitmap(bitmap, src, dst, new Paint(Paint.ANTI_ALIAS_FLAG));
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (menuHelper.gameMenuSetting.mouseMode == 0 && menuHelper.viewManager.gameCursorMode == 0){
            cursorX = event.getX();
            cursorY = event.getY();
            InputBridge.setPointer(launcher,(int) event.getX(),(int) event.getY());
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();
                downTime = System.currentTimeMillis();
                if (menuHelper.gameMenuSetting.mouseMode == 1 && menuHelper.viewManager.gameCursorMode == 0){
                    startCursorX = cursorX;
                    startCursorY = cursorY;
                }
                if (menuHelper.viewManager.gameCursorMode == 1 && (!menuHelper.gameMenuSetting.disableHalfScreen || initialX > (screenWidth >> 1))){
                    handler.postDelayed(runnable,400);
                }
                if (menuHelper.gameMenuSetting.mouseMode == 0 && menuHelper.viewManager.gameCursorMode == 0){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,true);
                        }
                    },50);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (menuHelper.gameMenuSetting.mouseMode == 1 && menuHelper.viewManager.gameCursorMode == 0){
                    float targetX;
                    float targetY;
                    if (startCursorX + ((event.getX() - initialX) * menuHelper.gameMenuSetting.mouseSpeed) < 0){
                        targetX = 0;
                    }
                    else if (startCursorX + ((event.getX() - initialX) * menuHelper.gameMenuSetting.mouseSpeed) > screenWidth - ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize)){
                        targetX = screenWidth - ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize);
                    }
                    else {
                        targetX = startCursorX + ((event.getX() - initialX) * menuHelper.gameMenuSetting.mouseSpeed);
                    }
                    if (startCursorY + ((event.getY() - initialY) * menuHelper.gameMenuSetting.mouseSpeed) < 0){
                        targetY = 0;
                    }
                    else if (startCursorY + ((event.getY() - initialY) * menuHelper.gameMenuSetting.mouseSpeed) > screenHeight - ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize)){
                        targetY = screenHeight - ConvertUtils.dip2px(getContext(),menuHelper.gameMenuSetting.mouseSize);
                    }
                    else {
                        targetY = startCursorY + ((event.getY() - initialY) * menuHelper.gameMenuSetting.mouseSpeed);
                    }
                    cursorX = targetX;
                    cursorY = targetY;
                    InputBridge.setPointer(launcher,(int) targetX,(int) targetY);
                }
                if (menuHelper.viewManager.gameCursorMode == 1 && (!menuHelper.gameMenuSetting.disableHalfScreen || initialX > (screenWidth >> 1))){
                    menuHelper.viewManager.setGamePointer(1,event.getX() - initialX,event.getY() - initialY);
                    if ((Math.abs(event.getX() - initialX) > 10 || Math.abs(event.getY() - initialY) > 10) && System.currentTimeMillis() - downTime < 400){
                        handler.removeCallbacks(runnable);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (menuHelper.gameMenuSetting.mouseMode == 0 && menuHelper.viewManager.gameCursorMode == 0){
                    InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,false);
                }
                if (menuHelper.viewManager.gameCursorMode == 1 && (!menuHelper.gameMenuSetting.disableHalfScreen || initialX > (screenWidth >> 1))){
                    menuHelper.viewManager.setGamePointer(0,event.getX() - initialX,event.getY() - initialY);
                    handler.removeCallbacks(runnable);
                    if (menuHelper.gameMenuSetting.touchMode == 0){
                        InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,false);
                    }
                    if (menuHelper.gameMenuSetting.touchMode == 1){
                        InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_RIGHT,false);
                    }
                }
                if (System.currentTimeMillis() - downTime <= 400 && Math.abs(event.getX() - initialX) <= 10 && Math.abs(event.getY() - initialY) <= 10){
                    if (menuHelper.gameMenuSetting.mouseMode == 1 && menuHelper.viewManager.gameCursorMode == 0){
                        InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,true);
                        InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,false);
                    }
                    if (menuHelper.viewManager.gameCursorMode == 1 && (!menuHelper.gameMenuSetting.disableHalfScreen || initialX > (screenWidth >> 1))){
                        if (menuHelper.gameMenuSetting.touchMode == 0){
                            InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_RIGHT,true);
                            InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_RIGHT,false);
                        }
                        if (menuHelper.gameMenuSetting.touchMode == 1){
                            InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,true);
                            InputBridge.sendMouseEvent(launcher,InputBridge.MOUSE_LEFT,false);
                        }
                    }
                }
                break;
        }
        return true;
    }
}
