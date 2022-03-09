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
import android.widget.RelativeLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;

public class LayoutPanel extends RelativeLayout {

    private static final int POSITION_MODE_PERCENT = 0;
    private static final int POSITION_MODE_ABSOLUTE = 1;

    private int positionMode = POSITION_MODE_PERCENT;

    private float xReference;
    private float yReference;

    private boolean showBackground = false;
    private boolean showReference = false;

    private Paint linePaint;
    private Path path;
    private Paint textPaint;
    private String xText;
    private String yText;

    private Bitmap background;

    public LayoutPanel(Context context) {
        super(context);
    }

    public LayoutPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(getContext().getColor(R.color.colorAccent));
        linePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getContext().getColor(R.color.colorGreen));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);

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
            path.moveTo(xReference,0);
            path.lineTo(xReference,getHeight());
            path.moveTo(0,yReference);
            path.lineTo(getWidth(),yReference);
            canvas.drawPath(path,linePaint);
            canvas.drawText(xText,100,100,textPaint);
            canvas.drawText(yText,100,200,textPaint);
        }
        invalidate();
    }

    public void showReference(int positionMode, float x, float y,int width,int height){
        this.positionMode = positionMode;
        if (positionMode == POSITION_MODE_PERCENT){
            this.xReference = x + (width >> 1);
            this.yReference = y + (height >> 1);
            xText = ((int) ((x / (getWidth() - width)) * 1000)) / 10f + "%";
            yText = ((int) ((y / (getHeight() - height)) * 1000)) / 10f + "%";
        }
        if (positionMode == POSITION_MODE_ABSOLUTE){
            this.xReference = x;
            this.yReference = y;
            xText = ConvertUtils.px2dip(getContext(),x) + "dp";
            yText = ConvertUtils.px2dip(getContext(),y) + "dp";
        }
        showReference = true;
    }

    public void hideReference(){
        showReference = false;
    }

    public void showBackground(){
        showBackground = true;
    }
}
