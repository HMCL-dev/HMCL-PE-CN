package com.tungsten.hmclpe.skin;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SkinGLSurfaceView extends GLSurfaceView
{
    private boolean alreadyCalled;
    private float mDensity;
    private float mPreviousX;
    private float mPreviousY;
    private MinecraftSkinRenderer mRenderer;
    
    public SkinGLSurfaceView(final Context context) {
        super(context);
        this.alreadyCalled = false;
    }
    
    public SkinGLSurfaceView(final Context context, final AttributeSet set) {
        super(context, set);
        this.alreadyCalled = false;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 1) {
            if (motionEvent == null) {
                return super.onTouchEvent(null);
            }
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            if (motionEvent.getAction() == 2 && this.mRenderer != null) {
                this.mRenderer.mCharacter.SetRotateStep((x - this.mPreviousX) / this.mDensity / 1.0f, (y - this.mPreviousY) / this.mDensity / 3.0f);
            }
            this.mPreviousX = x;
            this.mPreviousY = y;
        }
        if (motionEvent.getPointerCount() == 2) {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    priId = motionEvent.getPointerId(motionEvent.getActionIndex());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    secId = motionEvent.getPointerId(motionEvent.getActionIndex());
                    float deltaX = motionEvent.getX(motionEvent.findPointerIndex(priId)) - motionEvent.getX(motionEvent.findPointerIndex(secId));
                    float deltaY = motionEvent.getY(motionEvent.findPointerIndex(priId)) - motionEvent.getY(motionEvent.findPointerIndex(secId));
                    initDist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    initScale = mRenderer.mCharacter.scale;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dX = motionEvent.getX(motionEvent.findPointerIndex(priId)) - motionEvent.getX(motionEvent.findPointerIndex(secId));
                    float dY = motionEvent.getY(motionEvent.findPointerIndex(priId)) - motionEvent.getY(motionEvent.findPointerIndex(secId));
                    double dist = Math.sqrt(dX * dX + dY * dY);
                    double delta = dist - initDist;
                    System.out.println(delta);
                    if (initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))) <= 2 && initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))) >= 0.7) {
                        float scale = (float) (initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))));
                        mRenderer.mCharacter.setScale(scale);
                    }
                    break;
            }
        }

        return true;
    }

    private int priId;
    private int secId;
    private double initDist;
    private float initScale;

    public void setRenderer(final MinecraftSkinRenderer minecraftSkinRenderer, final float mDensity) {
        this.mRenderer = minecraftSkinRenderer;
        this.mDensity = mDensity;
        super.setRenderer((Renderer)minecraftSkinRenderer);
    }
}
