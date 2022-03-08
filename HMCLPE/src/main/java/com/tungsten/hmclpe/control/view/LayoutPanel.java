package com.tungsten.hmclpe.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tungsten.hmclpe.R;

public class LayoutPanel extends RelativeLayout {

    private static final int POSITION_MODE_NULL = 0;
    private static final int POSITION_MODE_PERCENT = 1;
    private static final int POSITION_MODE_ABSOLUTE = 2;

    private int positionMode = POSITION_MODE_NULL;

    private float xReference;
    private float yReference;

    private boolean showBackground = false;
    private boolean showReference = false;

    private Paint paint;

    private Bitmap background;

    public LayoutPanel(Context context) {
        super(context);
    }

    public LayoutPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getContext().getColor(R.color.colorAccent));

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
            if (positionMode == POSITION_MODE_PERCENT){

            }
            if (positionMode == POSITION_MODE_ABSOLUTE){

            }

        }
        invalidate();
    }

    public void setPositionMode(int mode){
        this.positionMode = mode;
    }

    public void showReference(float x,float y){
        xReference = x;
        yReference = y;
        showReference = true;
    }

    public void hideReference(){
        showReference = false;
    }

    public void showBackground(){
        showBackground = true;
    }
}
