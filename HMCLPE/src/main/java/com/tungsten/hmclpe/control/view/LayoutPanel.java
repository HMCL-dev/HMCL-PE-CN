package com.tungsten.hmclpe.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.InputBridge;
import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;

import org.lwjgl.glfw.CallbackBridge;

public class LayoutPanel extends RelativeLayout {

    private static final int POSITION_MODE_PERCENT = 0;
    private static final int POSITION_MODE_ABSOLUTE = 1;

    private int positionMode = POSITION_MODE_PERCENT;

    private float[] xReference;
    private float[] yReference;

    private boolean showBackground = false;
    private boolean showReference = false;

    private Paint linePaint;
    private Path path;
    private Paint textPaint;
    private String xText;
    private String yText;

    private Bitmap background;

    private MenuHelper menuHelper;

    private int cursorMode;
    private float mouseX;
    private float mouseY;

    public LayoutPanel(Context context) {
        super(context);
    }

    public LayoutPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(getContext().getColor(R.color.colorGreen));
        linePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getContext().getColor(R.color.colorGreen));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);

        xReference = new float[2];
        yReference = new float[2];

        background = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_background);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        if (showBackground){
            Rect src = new Rect(0, 0, background.getWidth(), background.getHeight());
            Rect dst = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawBitmap(background, src, dst, new Paint(Paint.ANTI_ALIAS_FLAG));
        }
        if (showReference){
            path = new Path();
            path.moveTo(xReference[0],0);
            path.lineTo(xReference[0],getHeight());
            path.moveTo(xReference[1],0);
            path.lineTo(xReference[1],getHeight());
            path.moveTo(0,yReference[0]);
            path.lineTo(getWidth(),yReference[0]);
            path.moveTo(0,yReference[1]);
            path.lineTo(getWidth(),yReference[1]);
            canvas.drawPath(path,linePaint);
            canvas.drawText(xText,100,100,textPaint);
            canvas.drawText(yText,100,200,textPaint);
        }
        invalidate();
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (menuHelper != null && menuHelper.controlType != 0) {
            int mouseCursorIndex = -1;
            for(int i = 0; i < event.getPointerCount(); i++) {
                if(event.getToolType(i) != MotionEvent.TOOL_TYPE_MOUSE) continue;
                // Mouse found
                mouseCursorIndex = i;
                break;
            }
            if(mouseCursorIndex == -1) return false;
            mouseX = event.getX() * menuHelper.scaleFactor;
            mouseY = event.getY() * menuHelper.scaleFactor;
            if (cursorMode == 1) {
                requestFocus();
                requestPointerCapture();
            }
            return handleMouseEvent(event,0);
        }
        return false;
    }

    @Override
    public boolean dispatchCapturedPointerEvent(MotionEvent event) {
        if (menuHelper != null && menuHelper.controlType != 0) {
            mouseX += (event.getX() * menuHelper.scaleFactor);
            mouseY += (event.getY() * menuHelper.scaleFactor);
            if (cursorMode == 0) {
                releasePointerCapture();
                clearFocus();
            }
            return handleMouseEvent(event,1);
        }
        return false;
    }

    public void setupMKController(MenuHelper menuHelper) {
        this.menuHelper = menuHelper;
    }

    public void enableCursor() {
        cursorMode = 0;
    }

    public void disableCursor() {
        cursorMode = 1;
    }

    public boolean handleMouseEvent(MotionEvent motionEvent, int mode) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_HOVER_MOVE && mode == 0) {
            InputBridge.setPointer(menuHelper.launcher,(int) mouseX,(int) mouseY);
            return true;
        }
        else if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE && mode == 1) {
            InputBridge.setPointer(menuHelper.launcher,(int) mouseX,(int) mouseY);
            return true;
        }
        else if (motionEvent.getActionMasked() == MotionEvent.ACTION_BUTTON_PRESS) {
            if (motionEvent.getActionButton() == MotionEvent.BUTTON_PRIMARY) {
                InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_LEFT, true);
                return true;
            }
            else if (motionEvent.getActionButton() == MotionEvent.BUTTON_SECONDARY) {
                InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_RIGHT, true);
                return true;
            }
            else if (motionEvent.getActionButton() == MotionEvent.BUTTON_TERTIARY) {
                InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_MIDDLE, true);
                return true;
            }
            else {
                return false;
            }
        }
        else if (motionEvent.getActionMasked() == MotionEvent.ACTION_BUTTON_RELEASE) {
            if (motionEvent.getActionButton() == MotionEvent.BUTTON_PRIMARY) {
                InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_LEFT, false);
                return true;
            }
            else if (motionEvent.getActionButton() == MotionEvent.BUTTON_SECONDARY) {
                InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_RIGHT, false);
                return true;
            }
            else if (motionEvent.getActionButton() == MotionEvent.BUTTON_TERTIARY) {
                InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_MIDDLE, false);
                return true;
            }
            else {
                return false;
            }
        }
        else if (motionEvent.getActionMasked() == MotionEvent.ACTION_SCROLL) {
            if (menuHelper.launcher == 2) {
                CallbackBridge.sendScroll(motionEvent.getAxisValue(MotionEvent.AXIS_HSCROLL), motionEvent.getAxisValue(MotionEvent.AXIS_VSCROLL));
            }
            else {
                if (motionEvent.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0) {
                    for (int i = 0;i < Math.abs((int) motionEvent.getAxisValue(MotionEvent.AXIS_VSCROLL));i++) {
                        InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_SCROLL_UP, true);
                    }
                }
                if (motionEvent.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0) {
                    for (int i = 0;i < Math.abs((int) motionEvent.getAxisValue(MotionEvent.AXIS_VSCROLL));i++) {
                        InputBridge.sendMouseEvent(menuHelper.launcher, InputBridge.MOUSE_SCROLL_DOWN, true);
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void showReference(int positionMode, float x, float y, int width, int height){
        this.positionMode = positionMode;
        if (positionMode == POSITION_MODE_PERCENT){
            xText = "X:" + ((int) ((x / (getWidth() - width)) * 1000)) / 10f + "%";
            yText = "Y:" + ((int) ((y / (getHeight() - height)) * 1000)) / 10f + "%";
        }
        if (positionMode == POSITION_MODE_ABSOLUTE){
            xText = "X:" + ConvertUtils.px2dip(getContext(),x) + "dp";
            yText = "Y:" + ConvertUtils.px2dip(getContext(),y) + "dp";
        }
        this.xReference[0] = x;
        this.yReference[0] = y;
        this.xReference[1] = x + width;
        this.yReference[1] = y + height;
        showReference = true;
    }

    public void hideReference(){
        showReference = false;
    }

    public void showBackground(){
        showBackground = true;
    }

    public void hideBackground() {
        showBackground = false;
    }
}
