package com.tungsten.hmclpe.skin;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;
import com.threed.jpct.util.SkyBox;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.account.SkinPreviewDialog;
import com.tungsten.hmclpe.skin.draw3d.Draw3DSupport;
import com.tungsten.hmclpe.skin.draw3d.Movement;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SkinRenderer implements Renderer {
    private static final String TAG = "SkinRenderer";
    private float camera_z = 20.0F;
    private int dirH = 1;
    private int dirLL = 0;
    private int dirRL = 1;
    private FrameBuffer fb = null;
    private int fps = 0;
    private int headDelta = 0;
    float headSpeed = 0.005F;
    private boolean isAnimation = true;
    private boolean isLoaded = false;
    private int isShow = 0;
    private final float legsSpeed = 0.03F;
    private final MainActivity dialog;
    private SkyBox mSkybox;
    private MainActivity master;
    private long time = System.currentTimeMillis();
    private World world = null;
    private SkinPreviewDialog skinPreviewDialog;

    public SkinRenderer(MainActivity var1, MainActivity var2, SkinPreviewDialog skinPreviewDialog) {
        this.dialog = var1;
        this.master = var2;
        this.skinPreviewDialog = skinPreviewDialog;
    }

    private void animateArms() {
        SimpleVector var1;
        SimpleVector var2;
        float var3;
        float var4;
        Object3D var6;
        if (SkinPreviewDialog.char_parts.length == 12) {
            SkinPreviewDialog.char_parts[8].setRotationPivot(new SimpleVector(0.0D, 1.5D, 0.0D));
            SkinPreviewDialog.char_parts[9].setRotationPivot(new SimpleVector(0.0D, 1.5D, 0.0D));
            SkinPreviewDialog.char_parts[10].setRotationPivot(new SimpleVector(0.0D, 1.5D, 0.0D));
            SkinPreviewDialog.char_parts[11].setRotationPivot(new SimpleVector(0.0D, 1.5D, 0.0D));
            var1 = SkinPreviewDialog.char_parts[5].getTransformedCenter();
            var2 = SkinPreviewDialog.char_parts[6].getTransformedCenter();
            var3 = var1.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            var4 = var2.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            if (var3 < 1.4414F) {
                this.dirLL = 1;
            }

            if (var3 > 1.7F) {
                this.dirLL = 0;
            }

            if (var4 < 1.4414F) {
                this.dirRL = 1;
            }

            if (var4 > 1.7F) {
                this.dirRL = 0;
            }

            Object3D var5 = SkinPreviewDialog.char_parts[8];
            var1 = SkinPreviewDialog.char_parts[8].getXAxis();
            if (this.dirLL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var5.rotateAxis(var1, var3);
            var5 = SkinPreviewDialog.char_parts[9];
            var1 = SkinPreviewDialog.char_parts[9].getXAxis();
            if (this.dirLL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var5.rotateAxis(var1, var3);
            var5 = SkinPreviewDialog.char_parts[10];
            var1 = SkinPreviewDialog.char_parts[10].getXAxis();
            if (this.dirRL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var5.rotateAxis(var1, var3);
            var6 = SkinPreviewDialog.char_parts[11];
            var2 = SkinPreviewDialog.char_parts[11].getXAxis();
            if (this.dirRL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var6.rotateAxis(var2, var3);
        } else {
            SkinPreviewDialog.char_parts[5].setRotationPivot(new SimpleVector(0.0D, 1.5D, 0.0D));
            SkinPreviewDialog.char_parts[6].setRotationPivot(new SimpleVector(0.0D, 1.5D, 0.0D));
            var2 = SkinPreviewDialog.char_parts[5].getTransformedCenter();
            var1 = SkinPreviewDialog.char_parts[6].getTransformedCenter();
            var4 = var2.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            var3 = var1.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            if (var4 < 1.3707F) {
                this.dirLL = 1;
            }

            if (var4 > 1.7707F) {
                this.dirLL = 0;
            }

            if (var3 < 1.3707F) {
                this.dirRL = 1;
            }

            if (var3 > 1.7707F) {
                this.dirRL = 0;
            }

            var6 = SkinPreviewDialog.char_parts[5];
            var2 = SkinPreviewDialog.char_parts[5].getXAxis();
            if (this.dirLL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var6.rotateAxis(var2, var3);
            var6 = SkinPreviewDialog.char_parts[6];
            var2 = SkinPreviewDialog.char_parts[6].getXAxis();
            if (this.dirRL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var6.rotateAxis(var2, var3);
        }

    }

    private void animateHead() {
        SkinPreviewDialog.char_parts[0].setRotationPivot(new SimpleVector(0.0F, 1.0F, 0.0F));
        SkinPreviewDialog.char_parts[1].setRotationPivot(new SimpleVector(0.0F, 1.0F, 0.0F));
        if (SkinPreviewDialog.char_parts[0].getXAxis().calcAngle(new SimpleVector(1.0F, 0.0F, 0.0F)) > 0.4F && this.headDelta > 10) {
            this.headDelta = 0;
            if (this.dirH == 1) {
                this.dirH = 0;
            } else {
                this.dirH = 1;
            }
        }

        ++this.headDelta;
        Object3D var1 = SkinPreviewDialog.char_parts[0];
        SimpleVector var2 = SkinPreviewDialog.char_parts[0].getYAxis();
        float var3;
        if (this.dirH == 0) {
            var3 = -this.headSpeed;
        } else {
            var3 = this.headSpeed;
        }

        var1.rotateAxis(var2, var3);
        Object3D var5 = SkinPreviewDialog.char_parts[1];
        SimpleVector var4 = SkinPreviewDialog.char_parts[1].getYAxis();
        if (this.dirH == 0) {
            var3 = -this.headSpeed;
        } else {
            var3 = this.headSpeed;
        }

        var5.rotateAxis(var4, var3);
    }

    private void animateLegs() {
        SimpleVector var1;
        SimpleVector var2;
        float var3;
        float var4;
        Object3D var5;
        if (SkinPreviewDialog.char_parts.length == 12) {
            SkinPreviewDialog.char_parts[4].setRotationPivot(new SimpleVector(0.0D, 4.5D, 0.0D));
            SkinPreviewDialog.char_parts[5].setRotationPivot(new SimpleVector(0.0D, 4.5D, 0.0D));
            SkinPreviewDialog.char_parts[6].setRotationPivot(new SimpleVector(0.0D, 4.5D, 0.0D));
            SkinPreviewDialog.char_parts[7].setRotationPivot(new SimpleVector(0.0D, 4.5D, 0.0D));
            var1 = SkinPreviewDialog.char_parts[5].getTransformedCenter();
            var2 = SkinPreviewDialog.char_parts[6].getTransformedCenter();
            var3 = var1.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            var4 = var2.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            if (var3 < 1.4414F) {
                this.dirLL = 1;
            }

            if (var3 > 1.7F) {
                this.dirLL = 0;
            }

            if (var4 < 1.4414F) {
                this.dirRL = 1;
            }

            if (var4 > 1.7F) {
                this.dirRL = 0;
            }

            Object3D var6 = SkinPreviewDialog.char_parts[5];
            var1 = SkinPreviewDialog.char_parts[5].getXAxis();
            if (this.dirLL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var6.rotateAxis(var1, var3);
            var6 = SkinPreviewDialog.char_parts[4];
            var1 = SkinPreviewDialog.char_parts[4].getXAxis();
            if (this.dirLL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var6.rotateAxis(var1, var3);
            var5 = SkinPreviewDialog.char_parts[7];
            var2 = SkinPreviewDialog.char_parts[5].getXAxis();
            if (this.dirRL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var5.rotateAxis(var2, var3);
            var6 = SkinPreviewDialog.char_parts[6];
            var1 = SkinPreviewDialog.char_parts[4].getXAxis();
            if (this.dirRL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var6.rotateAxis(var1, var3);
        } else {
            SkinPreviewDialog.char_parts[3].setRotationPivot(new SimpleVector(0.0D, 4.5D, 0.0D));
            SkinPreviewDialog.char_parts[4].setRotationPivot(new SimpleVector(0.0D, 4.5D, 0.0D));
            var2 = SkinPreviewDialog.char_parts[3].getTransformedCenter();
            var1 = SkinPreviewDialog.char_parts[4].getTransformedCenter();
            var4 = var2.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            var3 = var1.calcAngle(new SimpleVector(0.0F, 0.0F, 1.0F));
            if (var4 < 1.3707F) {
                this.dirLL = 1;
            }

            if (var4 > 1.7707F) {
                this.dirLL = 0;
            }

            if (var3 < 1.3707F) {
                this.dirRL = 1;
            }

            if (var3 > 1.7707F) {
                this.dirRL = 0;
            }

            var5 = SkinPreviewDialog.char_parts[3];
            var2 = SkinPreviewDialog.char_parts[3].getXAxis();
            if (this.dirLL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var5.rotateAxis(var2, var3);
            var5 = SkinPreviewDialog.char_parts[4];
            var2 = SkinPreviewDialog.char_parts[4].getXAxis();
            if (this.dirRL == 0) {
                var3 = -0.03F;
            } else {
                var3 = 0.03F;
            }

            var5.rotateAxis(var2, var3);
        }

    }

    private void moveCamera() {
        Movement var1 = skinPreviewDialog.movementHandler.getMovement();
        Object3D var2 = SkinPreviewDialog.char_parts[2];
        SimpleVector var3 = new SimpleVector(0.0F, 1.0F, 0.0F);
        if (var1.hasMovement()) {
            Camera var4 = this.world.getCamera();
            if (this.camera_z - var1.cameraMovementZ > 5.0F && this.camera_z - var1.cameraMovementZ < 20.0F) {
                this.camera_z -= var1.cameraMovementZ;
            }

            float var5 = var3.calcAngle(var4.getYAxis());
            if (var1.worldRotationX + var5 < 1.5588F && var5 - var1.worldRotationX < 1.5588F) {
                var4.rotateX(-var1.worldRotationX);
            }

            var4.rotateY(-var1.cameraRotationY);
            var4.setPosition(var2.getTransformedCenter());
            var4.moveCamera(2, this.camera_z);
            var4.lookAt(var2.getTransformedCenter());
            var4.moveCamera(new SimpleVector(0.0D, 0.5D, 0.0D), 1.0F);
        }

    }

    public boolean isAnimation() {
        return this.isAnimation;
    }

    public void onDrawFrame(GL10 var1) {
        if (SkinPreviewDialog.char_parts != null) {
            if (this.isAnimation && (SkinPreviewDialog.char_parts.length == 7 || SkinPreviewDialog.char_parts.length == 12)) {
                this.animateLegs();
                this.animateArms();
                this.animateHead();
            }

            int var3;
            if (!this.isLoaded) {
                Object3D[] var5 = SkinPreviewDialog.char_parts;
                int var2 = var5.length;

                for(var3 = 0; var3 < var2; ++var3) {
                    Object3D var4 = var5[var3];
                    this.world.addObject(var4);
                }

                this.isLoaded = true;
            }

            this.fb.clear(new RGBColor(0, 0, 0));
            this.mSkybox.render(this.world, this.fb);
            this.world.renderScene(this.fb);
            this.world.draw(this.fb);
            this.moveCamera();
            this.fb.display();
            var3 = this.isShow;
            if (var3 == 100) {

            } else if (var3 < 100) {
                this.isShow = var3 + 1;
            }

            if (System.currentTimeMillis() - this.time >= 1000L) {
                StringBuilder var6 = new StringBuilder();
                var6.append(this.fps);
                var6.append("fps");
                Logger.log(var6.toString());
                this.fps = 0;
                this.time = System.currentTimeMillis();
            }

            ++this.fps;
        }
    }

    public void onSurfaceChanged(GL10 var1, int var2, int var3) {
        Log.i("SkinRenderer", "onSurfaceChanged triggered");
        FrameBuffer var4 = this.fb;
        if (var4 != null) {
            var4.dispose();
        }

        this.fb = new FrameBuffer(var1, var2, var3);
        if (this.master == null) {
            this.world = new World();
            world.setAmbientLight(600, 600, 600);
            this.mSkybox = Draw3DSupport.initSkybox(dialog);
            Camera var6 = this.world.getCamera();
            var6.setFOV(0.0F);
            var6.rotateY(-0.0F);
            var6.rotateX(-0.0F);
            var6.moveCamera(2, this.camera_z);
            var6.moveCamera(new SimpleVector(0.0F, 3.0F, 0.0F), 1.0F);
            MemoryHelper.compact();
            if (this.master == null) {
                Logger.log("Saving master Activity!");
                this.master = dialog;
            }
        }

    }

    public void onSurfaceCreated(GL10 var1, EGLConfig var2) {
    }

    public void setAnimation(boolean var1) {
        this.isAnimation = var1;
    }
}
