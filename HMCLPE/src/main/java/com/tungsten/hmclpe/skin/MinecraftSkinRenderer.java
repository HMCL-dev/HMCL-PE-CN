package com.tungsten.hmclpe.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.Log;

import com.tungsten.hmclpe.skin.utils.TextureHelper;
import com.tungsten.hmclpe.skin.utils.Utils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MinecraftSkinRenderer implements GLSurfaceView.Renderer {
    public static float[] light0Position;
    public boolean changeSkinImage;
    private int mBackTexData;
    public GameCharacter mCharacter;
    private int[] mCharacterTexData;
    private Context mContext;
    public String path;
    public float[] plane_texcords;
    protected float[] plane_vertices;
    public Bitmap skin;
    public Bitmap cape;
    boolean superRun;
    boolean updateBitmapSkin;

    static {
        MinecraftSkinRenderer.light0Position = new float[]{0.0f, 0.0f, 5100.0f, 0.0f};
    }

    public MinecraftSkinRenderer(final Context mContext) {
        this.changeSkinImage = false;
        this.plane_texcords = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        this.plane_vertices = new float[]{-200.0f, -100.0f, -100.0f, -200.0f, 100.0f, -100.0f, 200.0f, 100.0f, -100.0f, 200.0f, -100.0f, -100.0f};
        this.updateBitmapSkin = false;
        this.superRun = false;
        this.mContext = mContext;
        this.mCharacter = new GameCharacter();
    }

    public MinecraftSkinRenderer(final Context mContext, final int n) {
        this.changeSkinImage = false;
        this.plane_texcords = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        this.plane_vertices = new float[]{-200.0f, -100.0f, -100.0f, -200.0f, 100.0f, -100.0f, 200.0f, 100.0f, -100.0f, 200.0f, -100.0f, -100.0f};
        this.updateBitmapSkin = false;
        this.superRun = false;
        this.mContext = mContext;
        this.mCharacter = new GameCharacter(n);
    }

    public MinecraftSkinRenderer(final Context mContext, final int n, final boolean b) {
        this.changeSkinImage = false;
        this.plane_texcords = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        this.plane_vertices = new float[]{-200.0f, -100.0f, -100.0f, -200.0f, 100.0f, -100.0f, 200.0f, 100.0f, -100.0f, 200.0f, -100.0f, -100.0f};
        this.updateBitmapSkin = false;
        this.superRun = false;
        this.mContext = mContext;
        this.mCharacter = new GameCharacter(b, n);
    }

    public MinecraftSkinRenderer(final Context mContext, final boolean b) {
        this.changeSkinImage = false;
        this.plane_texcords = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        this.plane_vertices = new float[]{-200.0f, -100.0f, -100.0f, -200.0f, 100.0f, -100.0f, 200.0f, 100.0f, -100.0f, 200.0f, -100.0f, -100.0f};
        this.updateBitmapSkin = false;
        this.superRun = false;
        this.mContext = mContext;
        this.mCharacter = new GameCharacter(b);
    }

    public void onDrawFrame(final GL10 gl10) {
        if (this.changeSkinImage) {
            this.changeSkinImage = false;
        }
        if (this.updateBitmapSkin) {
            mCharacterTexData = TextureHelper.loadGLTextureFromBitmap(this.skin,this.cape, gl10);
            this.updateBitmapSkin = false;
        }
        gl10.glClear(16640);
        gl10.glEnable(3553);
        gl10.glLoadIdentity();
        gl10.glColor4f(0.63671875f, 0.76953125f, 0.22265625f, 1.0f);
        gl10.glTranslatef(0.0f, 0.0f, -60.0f);
        gl10.glPushMatrix();
        gl10.glBindTexture(3553, mCharacterTexData[0]);
        this.mCharacter.drawBody(gl10);
        if (cape != null && cape.getWidth() == 64 && cape.getHeight() == 32) {
            gl10.glBindTexture(3553, mCharacterTexData[1]);
            this.mCharacter.drawCape(gl10);
        }
        gl10.glPopMatrix();
        gl10.glLoadIdentity();
        if (this.superRun) {
            GLU.gluLookAt(gl10, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            gl10.glRotatef(0.09f * (int) (SystemClock.uptimeMillis() % 4000L), 0.0f, 0.0f, 1.0f);
            this.mCharacter.drawBody(gl10);
        }
    }

    public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
        gl10.glViewport(0, 0, n, n2);
        gl10.glMatrixMode(5889);
        gl10.glLoadIdentity();
        final float n3 = n2 / 2.0f / (float) Math.tan(Utils.d2r(22.5f));
        GLU.gluPerspective(gl10, 45.0f, n / n2, 0.5f, Math.max(1500.0f, n3));
        gl10.glMatrixMode(5888);
        gl10.glLoadIdentity();
        GLU.gluLookAt(gl10, n / 2.0f, n2 / 2.0f, n3, n / 2.0f, n2 / 2.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        gl10.glEnable(2896);
        gl10.glEnable(16384);
        gl10.glLightfv(16384, 4608, new float[]{3.5f, 3.5f, 3.5f, 1.0f}, 0);
        gl10.glLightfv(16384, 4611, MinecraftSkinRenderer.light0Position = new float[]{0.0f, 0.0f, n3, 0.0f}, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        this.mCharacterTexData = TextureHelper.loadTexture(this.mContext, GameCharacter.selected_resource);
        gl10.glEnable(3042);
        gl10.glCullFace(1028);
        gl10.glShadeModel(7425);
        gl10.glEnable(6408);
        gl10.glEnable(2929);
        gl10.glDepthFunc(515);
        gl10.glHint(3152, 4354);
        gl10.glClearDepthf(1.0f);
    }

    public void setSuperRun(final boolean superRun) {
        this.superRun = superRun;
    }

    public void updateTexture(final Bitmap skin,Bitmap cape) {
        this.skin = skin;
        this.cape = cape;
        this.updateBitmapSkin = true;
    }
}
