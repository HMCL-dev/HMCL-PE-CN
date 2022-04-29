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
        if (motionEvent == null) {
            return super.onTouchEvent(motionEvent);
        }
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        if (motionEvent.getAction() == 2 && this.mRenderer != null) {
            this.mRenderer.mCharacter.SetRotateStep((x - this.mPreviousX) / this.mDensity / 1.0f, (y - this.mPreviousY) / this.mDensity / 5.1f);
        }
        this.mPreviousX = x;
        this.mPreviousY = y;
        return true;
    }
    
    public void setRenderer(final MinecraftSkinRenderer minecraftSkinRenderer, final float mDensity) {
        this.mRenderer = minecraftSkinRenderer;
        this.mDensity = mDensity;
        super.setRenderer((Renderer)minecraftSkinRenderer);
    }
}
