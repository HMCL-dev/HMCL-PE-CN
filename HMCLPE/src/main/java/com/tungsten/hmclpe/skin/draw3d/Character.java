package com.tungsten.hmclpe.skin.draw3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;

import java.util.ArrayList;

public class Character {
    private boolean is64x64 = false;
    private Context mContext;
    private ArrayList<Integer> part_arm_l_tex = new ArrayList();
    private ArrayList<Integer> part_arm_l_tex_2 = new ArrayList();
    private ArrayList<Integer> part_arm_r_tex = new ArrayList();
    private ArrayList<Integer> part_arm_r_tex_2 = new ArrayList();
    private ArrayList<Integer> part_arm_tex = new ArrayList();
    private ArrayList<Integer> part_body_tex = new ArrayList();
    private ArrayList<Integer> part_hat_tex = new ArrayList();
    private ArrayList<Integer> part_head_tex = new ArrayList();
    private ArrayList<Integer> part_jacket_tex = new ArrayList();
    private ArrayList<Integer> part_leg_l_tex = new ArrayList();
    private ArrayList<Integer> part_leg_l_tex_2 = new ArrayList();
    private ArrayList<Integer> part_leg_r_tex = new ArrayList();
    private ArrayList<Integer> part_leg_r_tex_2 = new ArrayList();
    private ArrayList<Integer> part_leg_tex = new ArrayList();

    public Character(Context var1, Bitmap var2) {
        this.mContext = var1;
        this.load_textures(var2);
    }

    private void addOrReplaceTexture(TextureManager var1, String var2, Texture var3) {
        var3.setFiltering(false);
        if (var1.containsTexture(var2)) {
            var1.replaceTexture(var2, var3);
        } else {
            var1.addTexture(var2, var3);
        }

    }

    private Bitmap convert_to_4x16(Bitmap var1) {
        Bitmap var2 = Bitmap.createBitmap(4, 16, Config.ARGB_8888);
        (new Canvas(var2)).drawBitmap(var1, (Rect)null, new Rect(0, 0, 4, 12), (Paint)null);
        return var2;
    }

    private Bitmap convert_to_8x16(Bitmap var1) {
        Bitmap var2 = Bitmap.createBitmap(8, 16, Config.ARGB_8888);
        (new Canvas(var2)).drawBitmap(var1, (Rect)null, new Rect(0, 0, 8, 12), (Paint)null);
        return var2;
    }

    private static Object3D draw_body(float var0, float var1, float var2, ArrayList<Integer> var3) {
        Object3D var4 = new Object3D(12);
        float var5 = -1.0F + var0;
        float var6 = -2.0F + var1;
        float var7 = -0.5F + var2;
        SimpleVector var8 = new SimpleVector(var5, var6, var7);
        ++var0;
        SimpleVector var9 = new SimpleVector(var0, var6, var7);
        ++var1;
        SimpleVector var10 = new SimpleVector(var5, var1, var7);
        SimpleVector var11 = new SimpleVector(var0, var1, var7);
        var2 += 0.5F;
        SimpleVector var12 = new SimpleVector(var5, var6, var2);
        SimpleVector var13 = new SimpleVector(var0, var6, var2);
        SimpleVector var14 = new SimpleVector(var5, var1, var2);
        SimpleVector var15 = new SimpleVector(var0, var1, var2);

        try {
            var4.addTriangle(var12, 0.0F, 0.0F, var8, 0.0F, 1.0F, var13, 1.0F, 0.0F, (Integer)var3.get(0));
            var4.addTriangle(var13, 1.0F, 0.0F, var8, 0.0F, 1.0F, var9, 1.0F, 1.0F, (Integer)var3.get(0));
            var4.addTriangle(var14, 0.0F, 0.0F, var15, 1.0F, 0.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var15, 1.0F, 0.0F, var11, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var8, 0.0F, 0.0F, var10, 0.0F, 0.75F, var9, 1.0F, 0.0F, (Integer)var3.get(2));
            var4.addTriangle(var9, 1.0F, 0.0F, var10, 0.0F, 0.75F, var11, 1.0F, 0.75F, (Integer)var3.get(2));
            var4.addTriangle(var12, 0.0F, 0.0F, var13, 1.0F, 0.0F, var14, 0.0F, 0.75F, (Integer)var3.get(3));
            var4.addTriangle(var13, 1.0F, 0.0F, var15, 1.0F, 0.75F, var14, 0.0F, 0.75F, (Integer)var3.get(3));
            var4.addTriangle(var8, 1.0F, 0.0F, var12, 0.0F, 0.0F, var10, 1.0F, 0.75F, (Integer)var3.get(4));
            var4.addTriangle(var12, 0.0F, 0.0F, var14, 0.0F, 0.75F, var10, 1.0F, 0.75F, (Integer)var3.get(4));
            var4.addTriangle(var9, 0.0F, 0.0F, var11, 0.0F, 0.75F, var13, 1.0F, 0.0F, (Integer)var3.get(5));
            var4.addTriangle(var13, 1.0F, 0.0F, var11, 0.0F, 0.75F, var15, 1.0F, 0.75F, (Integer)var3.get(5));
        } catch (Exception var16) {
            var16.printStackTrace();
        }

        return var4;
    }

    private static Object3D draw_hat(float var0, float var1, float var2, ArrayList<Integer> var3) {
        Object3D var4 = new Object3D(12);
        float var5 = -1.25F + var0;
        float var6 = -1.25F + var1;
        float var7 = -1.25F + var2;
        SimpleVector var8 = new SimpleVector(var5, var6, var7);
        ++var0;
        SimpleVector var9 = new SimpleVector(var0, var6, var7);
        ++var1;
        SimpleVector var10 = new SimpleVector(var5, var1, var7);
        SimpleVector var11 = new SimpleVector(var0, var1, var7);
        ++var2;
        SimpleVector var12 = new SimpleVector(var5, var6, var2);
        SimpleVector var13 = new SimpleVector(var0, var6, var2);
        SimpleVector var14 = new SimpleVector(var5, var1, var2);
        SimpleVector var15 = new SimpleVector(var0, var1, var2);

        try {
            var4.addTriangle(var12, 0.0F, 0.0F, var8, 0.0F, 1.0F, var13, 1.0F, 0.0F, (Integer)var3.get(0));
            var4.addTriangle(var13, 1.0F, 0.0F, var8, 0.0F, 1.0F, var9, 1.0F, 1.0F, (Integer)var3.get(0));
            var4.addTriangle(var14, 0.0F, 0.0F, var15, 1.0F, 0.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var15, 1.0F, 0.0F, var11, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var8, 0.0F, 0.0F, var10, 0.0F, 1.0F, var9, 1.0F, 0.0F, (Integer)var3.get(2));
            var4.addTriangle(var9, 1.0F, 0.0F, var10, 0.0F, 1.0F, var11, 1.0F, 1.0F, (Integer)var3.get(2));
            var4.addTriangle(var12, 1.0F, 0.0F, var13, 0.0F, 0.0F, var14, 1.0F, 1.0F, (Integer)var3.get(3));
            var4.addTriangle(var13, 0.0F, 0.0F, var15, 0.0F, 1.0F, var14, 1.0F, 1.0F, (Integer)var3.get(3));
            var4.addTriangle(var12, 1.0F, 0.0F, var14, 1.0F, 1.0F, var8, 0.0F, 0.0F, (Integer)var3.get(4));
            var4.addTriangle(var8, 0.0F, 0.0F, var14, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(4));
            var4.addTriangle(var9, 1.0F, 0.0F, var11, 1.0F, 1.0F, var13, 0.0F, 0.0F, (Integer)var3.get(5));
            var4.addTriangle(var13, 0.0F, 0.0F, var11, 1.0F, 1.0F, var15, 0.0F, 1.0F, (Integer)var3.get(5));
            var4.setTransparency(333);
            var4.setSortOffset(-100.0F);
            var4.compile();
            var4.strip();
            var4.build();
        } catch (Exception var16) {
            var16.printStackTrace();
        }

        return var4;
    }

    private static Object3D draw_head(float var0, float var1, float var2, ArrayList<Integer> var3) {
        Object3D var4 = new Object3D(12);
        float var5 = var0 - 1.0F;
        float var6 = var1 - 1.0F;
        float var7 = var2 - 1.0F;
        SimpleVector var8 = new SimpleVector(var5, var6, var7);
        ++var0;
        SimpleVector var9 = new SimpleVector(var0, var6, var7);
        ++var1;
        SimpleVector var10 = new SimpleVector(var5, var1, var7);
        SimpleVector var11 = new SimpleVector(var0, var1, var7);
        ++var2;
        SimpleVector var12 = new SimpleVector(var5, var6, var2);
        SimpleVector var13 = new SimpleVector(var0, var6, var2);
        SimpleVector var14 = new SimpleVector(var5, var1, var2);
        SimpleVector var15 = new SimpleVector(var0, var1, var2);

        try {
            var4.addTriangle(var12, 0.0F, 0.0F, var8, 0.0F, 1.0F, var13, 1.0F, 0.0F, (Integer)var3.get(0));
            var4.addTriangle(var13, 1.0F, 0.0F, var8, 0.0F, 1.0F, var9, 1.0F, 1.0F, (Integer)var3.get(0));
            var4.addTriangle(var14, 0.0F, 0.0F, var15, 1.0F, 0.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var15, 1.0F, 0.0F, var11, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var8, 0.0F, 0.0F, var10, 0.0F, 1.0F, var9, 1.0F, 0.0F, (Integer)var3.get(2));
            var4.addTriangle(var9, 1.0F, 0.0F, var10, 0.0F, 1.0F, var11, 1.0F, 1.0F, (Integer)var3.get(2));
            var4.addTriangle(var12, 0.0F, 0.0F, var13, 1.0F, 0.0F, var14, 0.0F, 1.0F, (Integer)var3.get(3));
            var4.addTriangle(var13, 1.0F, 0.0F, var15, 1.0F, 1.0F, var14, 0.0F, 1.0F, (Integer)var3.get(3));
            var4.addTriangle(var8, 0.0F, 0.0F, var12, 1.0F, 0.0F, var10, 0.0F, 1.0F, (Integer)var3.get(4));
            var4.addTriangle(var12, 1.0F, 0.0F, var14, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(4));
            var4.addTriangle(var9, 1.0F, 0.0F, var11, 1.0F, 1.0F, var13, 0.0F, 0.0F, (Integer)var3.get(5));
            var4.addTriangle(var13, 0.0F, 0.0F, var11, 1.0F, 1.0F, var15, 0.0F, 1.0F, (Integer)var3.get(5));
        } catch (Exception var16) {
            var16.printStackTrace();
        }

        return var4;
    }

    private static Object3D draw_jacket(float var0, float var1, float var2, ArrayList<Integer> var3) {
        Object3D var4 = new Object3D(12);
        float var5 = -1.0F + var0;
        float var6 = -3.0F + var1;
        float var7 = -0.5625F + var2;
        SimpleVector var8 = new SimpleVector(var5, var6, var7);
        ++var0;
        SimpleVector var9 = new SimpleVector(var0, var6, var7);
        ++var1;
        SimpleVector var10 = new SimpleVector(var5, var1, var7);
        SimpleVector var11 = new SimpleVector(var0, var1, var7);
        var2 += 0.5625F;
        SimpleVector var12 = new SimpleVector(var5, var6, var2);
        SimpleVector var13 = new SimpleVector(var0, var6, var2);
        SimpleVector var14 = new SimpleVector(var5, var1, var2);
        SimpleVector var15 = new SimpleVector(var0, var1, var2);

        try {
            var4.addTriangle(var12, 0.0F, 0.0F, var8, 0.0F, 1.0F, var13, 1.0F, 0.0F, (Integer)var3.get(0));
            var4.addTriangle(var13, 1.0F, 0.0F, var8, 0.0F, 1.0F, var9, 1.0F, 1.0F, (Integer)var3.get(0));
            var4.addTriangle(var14, 0.0F, 0.0F, var15, 1.0F, 0.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var15, 1.0F, 0.0F, var11, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(1));
            var4.addTriangle(var8, 0.0F, 0.0F, var10, 0.0F, 1.0F, var9, 1.0F, 0.0F, (Integer)var3.get(2));
            var4.addTriangle(var9, 1.0F, 0.0F, var10, 0.0F, 1.0F, var11, 1.0F, 1.0F, (Integer)var3.get(2));
            var4.addTriangle(var12, 1.0F, 0.0F, var13, 0.0F, 0.0F, var14, 1.0F, 1.0F, (Integer)var3.get(3));
            var4.addTriangle(var13, 0.0F, 0.0F, var15, 0.0F, 1.0F, var14, 1.0F, 1.0F, (Integer)var3.get(3));
            var4.addTriangle(var12, 1.0F, 0.0F, var14, 1.0F, 1.0F, var8, 0.0F, 0.0F, (Integer)var3.get(4));
            var4.addTriangle(var8, 0.0F, 0.0F, var14, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(4));
            var4.addTriangle(var9, 1.0F, 0.0F, var11, 1.0F, 1.0F, var13, 0.0F, 0.0F, (Integer)var3.get(5));
            var4.addTriangle(var13, 0.0F, 0.0F, var11, 1.0F, 1.0F, var15, 0.0F, 1.0F, (Integer)var3.get(5));
        } catch (Exception var16) {
            var16.printStackTrace();
        }

        var4.setTransparency(333);
        var4.compile();
        var4.strip();
        var4.build();
        return var4;
    }

    private static Object3D draw_limb(float var0, float var1, float var2, ArrayList<Integer> var3, boolean var4) {
        Object3D var5 = new Object3D(12);
        float var6 = -0.5F + var0;
        float var7 = -1.5F + var1;
        float var8 = -0.5F + var2;
        SimpleVector var9 = new SimpleVector(var6, var7, var8);
        var0 += 0.5F;
        SimpleVector var10 = new SimpleVector(var0, var7, var8);
        ++var1;
        SimpleVector var11 = new SimpleVector(var6, var1, var8);
        SimpleVector var12 = new SimpleVector(var0, var1, var8);
        var2 += 0.5F;
        SimpleVector var13 = new SimpleVector(var6, var7, var2);
        SimpleVector var14 = new SimpleVector(var0, var7, var2);
        SimpleVector var15 = new SimpleVector(var6, var1, var2);
        SimpleVector var16 = new SimpleVector(var0, var1, var2);
        Exception var10000;
        boolean var10001;
        if (var4) {
            try {
                var5.addTriangle(var13, 1.0F, 0.0F, var9, 1.0F, 1.0F, var14, 0.0F, 0.0F, (Integer)var3.get(0));
                var5.addTriangle(var14, 0.0F, 0.0F, var9, 1.0F, 1.0F, var10, 0.0F, 1.0F, (Integer)var3.get(0));
                var5.addTriangle(var15, 1.0F, 0.0F, var16, 0.0F, 0.0F, var11, 1.0F, 1.0F, (Integer)var3.get(1));
                var5.addTriangle(var16, 0.0F, 0.0F, var12, 0.0F, 1.0F, var11, 1.0F, 1.0F, (Integer)var3.get(1));
                var5.addTriangle(var9, 1.0F, 0.0F, var11, 1.0F, 0.75F, var10, 0.0F, 0.0F, (Integer)var3.get(2));
                var5.addTriangle(var10, 0.0F, 0.0F, var11, 1.0F, 0.75F, var12, 0.0F, 0.75F, (Integer)var3.get(2));
                var5.addTriangle(var13, 0.0F, 0.0F, var14, 1.0F, 0.0F, var15, 0.0F, 0.75F, (Integer)var3.get(3));
                var5.addTriangle(var14, 1.0F, 0.0F, var16, 1.0F, 0.75F, var15, 0.0F, 0.75F, (Integer)var3.get(3));
                var5.addTriangle(var9, 0.0F, 0.0F, var13, 1.0F, 0.0F, var11, 0.0F, 0.75F, (Integer)var3.get(5));
                var5.addTriangle(var13, 1.0F, 0.0F, var15, 1.0F, 0.75F, var11, 0.0F, 0.75F, (Integer)var3.get(5));
                var5.addTriangle(var10, 1.0F, 0.0F, var12, 1.0F, 0.75F, var14, 0.0F, 0.0F, (Integer)var3.get(4));
                var5.addTriangle(var14, 0.0F, 0.0F, var12, 1.0F, 0.75F, var16, 0.0F, 0.75F, (Integer)var3.get(4));
                return var5;
            } catch (Exception var17) {
                var10000 = var17;
                var10001 = false;
            }
        } else {
            try {
                var5.addTriangle(var13, 0.0F, 0.0F, var9, 0.0F, 1.0F, var14, 1.0F, 0.0F, (Integer)var3.get(0));
                var5.addTriangle(var14, 1.0F, 0.0F, var9, 0.0F, 1.0F, var10, 1.0F, 1.0F, (Integer)var3.get(0));
                var5.addTriangle(var15, 0.0F, 0.0F, var16, 1.0F, 0.0F, var11, 0.0F, 1.0F, (Integer)var3.get(1));
                var5.addTriangle(var16, 1.0F, 0.0F, var12, 1.0F, 1.0F, var11, 0.0F, 1.0F, (Integer)var3.get(1));
                var5.addTriangle(var9, 0.0F, 0.0F, var11, 0.0F, 0.75F, var10, 1.0F, 0.0F, (Integer)var3.get(2));
                var5.addTriangle(var10, 1.0F, 0.0F, var11, 0.0F, 0.75F, var12, 1.0F, 0.75F, (Integer)var3.get(2));
                var5.addTriangle(var13, 1.0F, 0.0F, var14, 0.0F, 0.0F, var15, 1.0F, 0.75F, (Integer)var3.get(3));
                var5.addTriangle(var14, 0.0F, 0.0F, var16, 0.0F, 0.75F, var15, 1.0F, 0.75F, (Integer)var3.get(3));
                var5.addTriangle(var9, 1.0F, 0.0F, var13, 0.0F, 0.0F, var11, 1.0F, 0.75F, (Integer)var3.get(4));
                var5.addTriangle(var13, 0.0F, 0.0F, var15, 0.0F, 0.75F, var11, 1.0F, 0.75F, (Integer)var3.get(4));
                var5.addTriangle(var10, 0.0F, 0.0F, var12, 0.0F, 0.75F, var14, 1.0F, 0.0F, (Integer)var3.get(5));
                var5.addTriangle(var14, 1.0F, 0.0F, var12, 0.0F, 0.75F, var16, 1.0F, 0.75F, (Integer)var3.get(5));
                return var5;
            } catch (Exception var18) {
                var10000 = var18;
                var10001 = false;
            }
        }

        Exception var19 = var10000;
        var19.printStackTrace();
        return var5;
    }

    private static Object3D draw_limb_new(float var0, float var1, float var2, ArrayList<Integer> var3, boolean var4, boolean var5) {
        Object3D var6 = new Object3D(12);
        float var7;
        if (var5) {
            var7 = 0.0625F;
        } else {
            var7 = 0.0F;
        }

        float var8 = 1.5F + var7;
        float var9 = var7 + 0.5F;
        float var10 = -var9;
        var7 = var10 + var0;
        float var11 = -var8 + var1;
        var10 += var2;
        SimpleVector var12 = new SimpleVector(var7, var11, var10);
        var0 += var9;
        SimpleVector var13 = new SimpleVector(var0, var11, var10);
        var1 += var8;
        SimpleVector var14 = new SimpleVector(var7, var1, var10);
        SimpleVector var15 = new SimpleVector(var0, var1, var10);
        var2 += var9;
        SimpleVector var16 = new SimpleVector(var7, var11, var2);
        SimpleVector var17 = new SimpleVector(var0, var11, var2);
        SimpleVector var18 = new SimpleVector(var7, var1, var2);
        SimpleVector var19 = new SimpleVector(var0, var1, var2);

        Exception var10000;
        label51: {
            boolean var10001;
            try {
                var6.addTriangle(var16, 1.0F, 0.0F, var12, 1.0F, 1.0F, var17, 0.0F, 0.0F, (Integer)var3.get(0));
                var6.addTriangle(var17, 0.0F, 0.0F, var12, 1.0F, 1.0F, var13, 0.0F, 1.0F, (Integer)var3.get(0));
                var6.addTriangle(var18, 1.0F, 0.0F, var19, 0.0F, 0.0F, var14, 1.0F, 1.0F, (Integer)var3.get(1));
                var6.addTriangle(var19, 0.0F, 0.0F, var15, 0.0F, 1.0F, var14, 1.0F, 1.0F, (Integer)var3.get(1));
                var6.addTriangle(var12, 1.0F, 0.0F, var14, 1.0F, 0.75F, var13, 0.0F, 0.0F, (Integer)var3.get(2));
                var6.addTriangle(var13, 0.0F, 0.0F, var14, 1.0F, 0.75F, var15, 0.0F, 0.75F, (Integer)var3.get(2));
                var6.addTriangle(var16, 0.0F, 0.0F, var17, 1.0F, 0.0F, var18, 0.0F, 0.75F, (Integer)var3.get(3));
                var6.addTriangle(var17, 1.0F, 0.0F, var19, 1.0F, 0.75F, var18, 0.0F, 0.75F, (Integer)var3.get(3));
                var6.addTriangle(var12, 0.0F, 0.0F, var16, 1.0F, 0.0F, var14, 0.0F, 0.75F, (Integer)var3.get(4));
                var6.addTriangle(var16, 1.0F, 0.0F, var18, 1.0F, 0.75F, var14, 0.0F, 0.75F, (Integer)var3.get(4));
            } catch (Exception var24) {
                var10000 = var24;
                var10001 = false;
                break label51;
            }

            if (var4) {
                try {
                    var6.addTriangle(var13, 1.0F, 0.0F, var15, 1.0F, 0.75F, var17, 0.0F, 0.0F, (Integer)var3.get(5));
                    var6.addTriangle(var17, 0.0F, 0.0F, var15, 1.0F, 0.75F, var19, 0.0F, 0.75F, (Integer)var3.get(5));
                } catch (Exception var23) {
                    var10000 = var23;
                    var10001 = false;
                    break label51;
                }
            } else {
                try {
                    var6.addTriangle(var13, 0.0F, 0.0F, var15, 0.0F, 0.75F, var17, 1.0F, 0.0F, (Integer)var3.get(5));
                    var6.addTriangle(var17, 1.0F, 0.0F, var15, 0.0F, 0.75F, var19, 1.0F, 0.75F, (Integer)var3.get(5));
                } catch (Exception var22) {
                    var10000 = var22;
                    var10001 = false;
                    break label51;
                }
            }

            if (var5) {
                try {
                    var6.setTransparency(333);
                } catch (Exception var21) {
                    var10000 = var21;
                    var10001 = false;
                    break label51;
                }
            }

            try {
                var6.compile();
                var6.strip();
                var6.build();
                return var6;
            } catch (Exception var20) {
                var10000 = var20;
                var10001 = false;
            }
        }

        Exception var25 = var10000;
        var25.printStackTrace();
        return var6;
    }

    private void load_textures(Bitmap var1) {
        TextureManager var2 = TextureManager.getInstance();
        if (var1.getHeight() == 64) {
            this.is64x64 = true;
            System.out.println("Skin: 64x64 version");
        } else {
            this.is64x64 = false;
            System.out.println("Skin: 64x32 version");
        }

        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        Bitmap var3 = Bitmap.createBitmap(var1, 8, 0, 8, 8, (Matrix)null, false);
        Bitmap var4 = Bitmap.createBitmap(var1, 16, 0, 8, 8, (Matrix)null, false);
        Bitmap var5 = Bitmap.createBitmap(var1, 8, 8, 8, 8, (Matrix)null, false);
        Bitmap var6 = Bitmap.createBitmap(var1, 24, 8, 8, 8, matrix, false);
        Bitmap var7 = Bitmap.createBitmap(var1, 16, 8, 8, 8, matrix, false);
        Bitmap var8 = Bitmap.createBitmap(var1, 0, 8, 8, 8, matrix, false);
        this.addOrReplaceTexture(var2, "head_top", new Texture(var3));
        this.addOrReplaceTexture(var2, "head_bottom", new Texture(var4));
        this.addOrReplaceTexture(var2, "head_front", new Texture(var5));
        this.addOrReplaceTexture(var2, "head_back", new Texture(var6));
        this.addOrReplaceTexture(var2, "head_left", new Texture(var8));
        this.addOrReplaceTexture(var2, "head_right", new Texture(var7));
        this.part_head_tex.add(var2.getTextureID("head_top"));
        this.part_head_tex.add(var2.getTextureID("head_bottom"));
        this.part_head_tex.add(var2.getTextureID("head_front"));
        this.part_head_tex.add(var2.getTextureID("head_back"));
        this.part_head_tex.add(var2.getTextureID("head_left"));
        this.part_head_tex.add(var2.getTextureID("head_right"));
        var7 = Bitmap.createBitmap(var1, 40, 0, 8, 8, (Matrix)null, false).copy(Config.ARGB_8888, false);
        var4 = Bitmap.createBitmap(var1, 48, 0, 8, 8, (Matrix)null, false).copy(Config.ARGB_8888, false);
        var8 = Bitmap.createBitmap(var1, 40, 8, 8, 8, (Matrix)null, false);
        var3 = Bitmap.createBitmap(var1, 56, 8, 8, 8, (Matrix)null, false).copy(Config.ARGB_8888, false);
        var5 = Bitmap.createBitmap(var1, 48, 8, 8, 8, matrix, false).copy(Config.ARGB_8888, false);
        var6 = Bitmap.createBitmap(var1, 32, 8, 8, 8, matrix, false).copy(Config.ARGB_8888, false);
        this.addOrReplaceTexture(var2, "hat_top", new Texture(var7));
        this.addOrReplaceTexture(var2, "hat_bottom", new Texture(var4));
        this.addOrReplaceTexture(var2, "hat_front", new Texture(var8));
        this.addOrReplaceTexture(var2, "hat_back", new Texture(var3));
        this.addOrReplaceTexture(var2, "hat_left", new Texture(var6));
        this.addOrReplaceTexture(var2, "hat_right", new Texture(var5));
        this.part_hat_tex.add(var2.getTextureID("hat_top"));
        this.part_hat_tex.add(var2.getTextureID("hat_bottom"));
        this.part_hat_tex.add(var2.getTextureID("hat_front"));
        this.part_hat_tex.add(var2.getTextureID("hat_back"));
        this.part_hat_tex.add(var2.getTextureID("hat_left"));
        this.part_hat_tex.add(var2.getTextureID("hat_right"));
        var6 = Bitmap.createBitmap(var1, 20, 16, 8, 4, (Matrix)null, false);
        var3 = Bitmap.createBitmap(var1, 28, 16, 8, 4, (Matrix)null, false);
        var8 = this.convert_to_8x16(Bitmap.createBitmap(var1, 20, 20, 8, 12, (Matrix)null, false));
        var5 = this.convert_to_8x16(Bitmap.createBitmap(var1, 32, 20, 8, 12, matrix, false));
        var7 = this.convert_to_8x16(Bitmap.createBitmap(var1, 16, 20, 4, 12, matrix, false));
        var4 = this.convert_to_8x16(Bitmap.createBitmap(var1, 28, 20, 4, 12, matrix, false));
        this.addOrReplaceTexture(var2, "body_top", new Texture(var6));
        this.addOrReplaceTexture(var2, "body_bottom", new Texture(var3));
        this.addOrReplaceTexture(var2, "body_front", new Texture(var8));
        this.addOrReplaceTexture(var2, "body_back", new Texture(var5));
        this.addOrReplaceTexture(var2, "body_left", new Texture(var7));
        this.addOrReplaceTexture(var2, "body_right", new Texture(var4));
        this.part_body_tex.add(var2.getTextureID("body_top"));
        this.part_body_tex.add(var2.getTextureID("body_bottom"));
        this.part_body_tex.add(var2.getTextureID("body_front"));
        this.part_body_tex.add(var2.getTextureID("body_back"));
        this.part_body_tex.add(var2.getTextureID("body_left"));
        this.part_body_tex.add(var2.getTextureID("body_right"));
        if (this.is64x64) {
            var5 = Bitmap.createBitmap(var1, 20, 32, 8, 4, (Matrix)null, false);
            var3 = Bitmap.createBitmap(var1, 28, 32, 8, 4, (Matrix)null, false);
            var6 = this.convert_to_8x16(Bitmap.createBitmap(var1, 20, 36, 8, 12, (Matrix)null, false));
            var7 = this.convert_to_8x16(Bitmap.createBitmap(var1, 32, 36, 8, 12, (Matrix)null, false));
            var8 = this.convert_to_8x16(Bitmap.createBitmap(var1, 28, 36, 4, 12, (Matrix)null, false));
            var4 = this.convert_to_8x16(Bitmap.createBitmap(var1, 16, 36, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "jacket_top", new Texture(var5));
            this.addOrReplaceTexture(var2, "jacket_bottom", new Texture(var3));
            this.addOrReplaceTexture(var2, "jacket_front", new Texture(var6));
            this.addOrReplaceTexture(var2, "jacket_back", new Texture(var7));
            this.addOrReplaceTexture(var2, "jacket_left", new Texture(var8));
            this.addOrReplaceTexture(var2, "jacket_right", new Texture(var4));
            this.part_jacket_tex.add(var2.getTextureID("jacket_top"));
            this.part_jacket_tex.add(var2.getTextureID("jacket_bottom"));
            this.part_jacket_tex.add(var2.getTextureID("jacket_front"));
            this.part_jacket_tex.add(var2.getTextureID("jacket_back"));
            this.part_jacket_tex.add(var2.getTextureID("jacket_left"));
            this.part_jacket_tex.add(var2.getTextureID("jacket_right"));
            var3 = Bitmap.createBitmap(var1, 20, 48, 4, 4, matrix, false);
            var4 = Bitmap.createBitmap(var1, 24, 48, 4, 4, matrix, false);
            var6 = this.convert_to_4x16(Bitmap.createBitmap(var1, 20, 52, 4, 12, matrix, false));
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 28, 52, 4, 12, matrix, false));
            var8 = this.convert_to_4x16(Bitmap.createBitmap(var1, 24, 52, 4, 12, (Matrix)null, false));
            var5 = this.convert_to_4x16(Bitmap.createBitmap(var1, 16, 52, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "l_leg_top", new Texture(var3));
            this.addOrReplaceTexture(var2, "l_leg_bottom", new Texture(var4));
            this.addOrReplaceTexture(var2, "l_leg_front", new Texture(var6));
            this.addOrReplaceTexture(var2, "l_leg_back", new Texture(var7));
            this.addOrReplaceTexture(var2, "l_leg_left", new Texture(var5));
            this.addOrReplaceTexture(var2, "l_leg_right", new Texture(var8));
            this.part_leg_l_tex.add(var2.getTextureID("l_leg_top"));
            this.part_leg_l_tex.add(var2.getTextureID("l_leg_bottom"));
            this.part_leg_l_tex.add(var2.getTextureID("l_leg_front"));
            this.part_leg_l_tex.add(var2.getTextureID("l_leg_back"));
            this.part_leg_l_tex.add(var2.getTextureID("l_leg_left"));
            this.part_leg_l_tex.add(var2.getTextureID("l_leg_right"));
            var8 = Bitmap.createBitmap(var1, 4, 48, 4, 4, matrix, false);
            var5 = Bitmap.createBitmap(var1, 8, 48, 4, 4, matrix, false);
            var6 = this.convert_to_4x16(Bitmap.createBitmap(var1, 4, 52, 4, 12, matrix, false));
            var4 = this.convert_to_4x16(Bitmap.createBitmap(var1, 12, 52, 4, 12, matrix, false));
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 8, 52, 4, 12, (Matrix)null, false));
            var3 = this.convert_to_4x16(Bitmap.createBitmap(var1, 0, 52, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "l_leg_top2", new Texture(var8));
            this.addOrReplaceTexture(var2, "l_leg_bottom2", new Texture(var5));
            this.addOrReplaceTexture(var2, "l_leg_front2", new Texture(var6));
            this.addOrReplaceTexture(var2, "l_leg_back2", new Texture(var4));
            this.addOrReplaceTexture(var2, "l_leg_left2", new Texture(var3));
            this.addOrReplaceTexture(var2, "l_leg_right2", new Texture(var7));
            this.part_leg_l_tex_2.add(var2.getTextureID("l_leg_top2"));
            this.part_leg_l_tex_2.add(var2.getTextureID("l_leg_bottom2"));
            this.part_leg_l_tex_2.add(var2.getTextureID("l_leg_front2"));
            this.part_leg_l_tex_2.add(var2.getTextureID("l_leg_back2"));
            this.part_leg_l_tex_2.add(var2.getTextureID("l_leg_left2"));
            this.part_leg_l_tex_2.add(var2.getTextureID("l_leg_right2"));
            var6 = Bitmap.createBitmap(var1, 4, 16, 4, 4, matrix, false);
            var4 = Bitmap.createBitmap(var1, 8, 16, 4, 4, matrix, false);
            var5 = this.convert_to_4x16(Bitmap.createBitmap(var1, 4, 20, 4, 12, matrix, false));
            var8 = this.convert_to_4x16(Bitmap.createBitmap(var1, 12, 20, 4, 12, matrix, false));
            var3 = this.convert_to_4x16(Bitmap.createBitmap(var1, 8, 20, 4, 12, (Matrix)null, false));
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 0, 20, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "r_leg_top", new Texture(var6));
            this.addOrReplaceTexture(var2, "r_leg_bottom", new Texture(var4));
            this.addOrReplaceTexture(var2, "r_leg_front", new Texture(var5));
            this.addOrReplaceTexture(var2, "r_leg_back", new Texture(var8));
            this.addOrReplaceTexture(var2, "r_leg_left", new Texture(var7));
            this.addOrReplaceTexture(var2, "r_leg_right", new Texture(var3));
            this.part_leg_r_tex.add(var2.getTextureID("r_leg_top"));
            this.part_leg_r_tex.add(var2.getTextureID("r_leg_bottom"));
            this.part_leg_r_tex.add(var2.getTextureID("r_leg_front"));
            this.part_leg_r_tex.add(var2.getTextureID("r_leg_back"));
            this.part_leg_r_tex.add(var2.getTextureID("r_leg_left"));
            this.part_leg_r_tex.add(var2.getTextureID("r_leg_right"));
            var7 = Bitmap.createBitmap(var1, 4, 32, 4, 4, matrix, false);
            var4 = Bitmap.createBitmap(var1, 8, 32, 4, 4, matrix, false);
            var3 = this.convert_to_4x16(Bitmap.createBitmap(var1, 4, 36, 4, 12, matrix, false));
            var8 = this.convert_to_4x16(Bitmap.createBitmap(var1, 12, 36, 4, 12, matrix, false));
            var6 = this.convert_to_4x16(Bitmap.createBitmap(var1, 8, 36, 4, 12, (Matrix)null, false));
            var5 = this.convert_to_4x16(Bitmap.createBitmap(var1, 0, 36, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "r_leg_top2", new Texture(var7));
            this.addOrReplaceTexture(var2, "r_leg_bottom2", new Texture(var4));
            this.addOrReplaceTexture(var2, "r_leg_front2", new Texture(var3));
            this.addOrReplaceTexture(var2, "r_leg_back2", new Texture(var8));
            this.addOrReplaceTexture(var2, "r_leg_left2", new Texture(var5));
            this.addOrReplaceTexture(var2, "r_leg_right2", new Texture(var6));
            this.part_leg_r_tex_2.add(var2.getTextureID("r_leg_top2"));
            this.part_leg_r_tex_2.add(var2.getTextureID("r_leg_bottom2"));
            this.part_leg_r_tex_2.add(var2.getTextureID("r_leg_front2"));
            this.part_leg_r_tex_2.add(var2.getTextureID("r_leg_back2"));
            this.part_leg_r_tex_2.add(var2.getTextureID("r_leg_left2"));
            this.part_leg_r_tex_2.add(var2.getTextureID("r_leg_right2"));
            var6 = Bitmap.createBitmap(var1, 36, 48, 4, 4, matrix, false);
            var4 = Bitmap.createBitmap(var1, 40, 48, 4, 4, matrix, false);
            var3 = this.convert_to_4x16(Bitmap.createBitmap(var1, 36, 52, 4, 12, matrix, false));
            var5 = this.convert_to_4x16(Bitmap.createBitmap(var1, 44, 52, 4, 12, matrix, false));
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 40, 52, 4, 12, (Matrix)null, false));
            var8 = this.convert_to_4x16(Bitmap.createBitmap(var1, 32, 52, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "l_arm_top", new Texture(var6));
            this.addOrReplaceTexture(var2, "l_arm_bottom", new Texture(var4));
            this.addOrReplaceTexture(var2, "l_arm_front", new Texture(var3));
            this.addOrReplaceTexture(var2, "l_arm_back", new Texture(var5));
            this.addOrReplaceTexture(var2, "l_arm_left", new Texture(var8));
            this.addOrReplaceTexture(var2, "l_arm_right", new Texture(var7));
            this.part_arm_l_tex.add(var2.getTextureID("l_arm_top"));
            this.part_arm_l_tex.add(var2.getTextureID("l_arm_bottom"));
            this.part_arm_l_tex.add(var2.getTextureID("l_arm_front"));
            this.part_arm_l_tex.add(var2.getTextureID("l_arm_back"));
            this.part_arm_l_tex.add(var2.getTextureID("l_arm_left"));
            this.part_arm_l_tex.add(var2.getTextureID("l_arm_right"));
            var4 = Bitmap.createBitmap(var1, 52, 48, 4, 4, matrix, false);
            var6 = Bitmap.createBitmap(var1, 56, 48, 4, 4, matrix, false);
            var3 = this.convert_to_4x16(Bitmap.createBitmap(var1, 52, 52, 4, 12, matrix, false));
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 60, 52, 4, 12, matrix, false));
            var5 = this.convert_to_4x16(Bitmap.createBitmap(var1, 56, 52, 4, 12, matrix, false));
            var8 = this.convert_to_4x16(Bitmap.createBitmap(var1, 48, 52, 4, 12, matrix, false));
            this.addOrReplaceTexture(var2, "l_arm_top2", new Texture(var4));
            this.addOrReplaceTexture(var2, "l_arm_bottom2", new Texture(var6));
            this.addOrReplaceTexture(var2, "l_arm_front2", new Texture(var3));
            this.addOrReplaceTexture(var2, "l_arm_back2", new Texture(var7));
            this.addOrReplaceTexture(var2, "l_arm_left2", new Texture(var8));
            this.addOrReplaceTexture(var2, "l_arm_right2", new Texture(var5));
            this.part_arm_l_tex_2.add(var2.getTextureID("l_arm_top2"));
            this.part_arm_l_tex_2.add(var2.getTextureID("l_arm_bottom2"));
            this.part_arm_l_tex_2.add(var2.getTextureID("l_arm_front2"));
            this.part_arm_l_tex_2.add(var2.getTextureID("l_arm_back2"));
            this.part_arm_l_tex_2.add(var2.getTextureID("l_arm_left2"));
            this.part_arm_l_tex_2.add(var2.getTextureID("l_arm_right2"));
            var3 = Bitmap.createBitmap(var1, 44, 16, 4, 4, matrix, false);
            var5 = Bitmap.createBitmap(var1, 48, 16, 4, 4, matrix, false);
            var8 = this.convert_to_4x16(Bitmap.createBitmap(var1, 44, 20, 4, 12, matrix, false));
            var4 = this.convert_to_4x16(Bitmap.createBitmap(var1, 52, 20, 4, 12, matrix, false));
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 48, 20, 4, 12, (Matrix)null, false));
            var6 = this.convert_to_4x16(Bitmap.createBitmap(var1, 40, 20, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "r_arm_top", new Texture(var3));
            this.addOrReplaceTexture(var2, "r_arm_bottom", new Texture(var5));
            this.addOrReplaceTexture(var2, "r_arm_front", new Texture(var8));
            this.addOrReplaceTexture(var2, "r_arm_back", new Texture(var4));
            this.addOrReplaceTexture(var2, "r_arm_left", new Texture(var6));
            this.addOrReplaceTexture(var2, "r_arm_right", new Texture(var7));
            this.part_arm_r_tex.add(var2.getTextureID("r_arm_top"));
            this.part_arm_r_tex.add(var2.getTextureID("r_arm_bottom"));
            this.part_arm_r_tex.add(var2.getTextureID("r_arm_front"));
            this.part_arm_r_tex.add(var2.getTextureID("r_arm_back"));
            this.part_arm_r_tex.add(var2.getTextureID("r_arm_left"));
            this.part_arm_r_tex.add(var2.getTextureID("r_arm_right"));
            var3 = Bitmap.createBitmap(var1, 44, 32, 4, 4, (Matrix)null, false);
            var5 = Bitmap.createBitmap(var1, 48, 32, 4, 4, (Matrix)null, false);
            var4 = this.convert_to_4x16(Bitmap.createBitmap(var1, 44, 36, 4, 12, matrix, false));
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 52, 36, 4, 12, matrix, false));
            var6 = this.convert_to_4x16(Bitmap.createBitmap(var1, 48, 36, 4, 12, (Matrix)null, false));
            var1 = this.convert_to_4x16(Bitmap.createBitmap(var1, 40, 36, 4, 12, matrix, false));
            this.addOrReplaceTexture(var2, "r_arm_top2", new Texture(var3));
            this.addOrReplaceTexture(var2, "r_arm_bottom2", new Texture(var5));
            this.addOrReplaceTexture(var2, "r_arm_front2", new Texture(var4));
            this.addOrReplaceTexture(var2, "r_arm_back2", new Texture(var7));
            this.addOrReplaceTexture(var2, "r_arm_left2", new Texture(var1));
            this.addOrReplaceTexture(var2, "r_arm_right2", new Texture(var6));
            this.part_arm_r_tex_2.add(var2.getTextureID("r_arm_top2"));
            this.part_arm_r_tex_2.add(var2.getTextureID("r_arm_bottom2"));
            this.part_arm_r_tex_2.add(var2.getTextureID("r_arm_front2"));
            this.part_arm_r_tex_2.add(var2.getTextureID("r_arm_back2"));
            this.part_arm_r_tex_2.add(var2.getTextureID("r_arm_left2"));
            this.part_arm_r_tex_2.add(var2.getTextureID("r_arm_right2"));
        } else {
            var4 = Bitmap.createBitmap(var1, 4, 16, 4, 4, matrix, false);
            var8 = Bitmap.createBitmap(var1, 8, 16, 4, 4, matrix, false);
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 4, 20, 4, 12, matrix, false));
            var3 = this.convert_to_4x16(Bitmap.createBitmap(var1, 12, 20, 4, 12, matrix, false));
            var5 = this.convert_to_4x16(Bitmap.createBitmap(var1, 0, 20, 4, 12, (Matrix)null, false));
            var6 = this.convert_to_4x16(Bitmap.createBitmap(var1, 8, 20, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "leg_top", new Texture(var4));
            this.addOrReplaceTexture(var2, "leg_bottom", new Texture(var8));
            this.addOrReplaceTexture(var2, "leg_front", new Texture(var7));
            this.addOrReplaceTexture(var2, "leg_back", new Texture(var3));
            this.addOrReplaceTexture(var2, "leg_left", new Texture(var6));
            this.addOrReplaceTexture(var2, "leg_right", new Texture(var5));
            this.part_leg_tex.add(var2.getTextureID("leg_top"));
            this.part_leg_tex.add(var2.getTextureID("leg_bottom"));
            this.part_leg_tex.add(var2.getTextureID("leg_front"));
            this.part_leg_tex.add(var2.getTextureID("leg_back"));
            this.part_leg_tex.add(var2.getTextureID("leg_left"));
            this.part_leg_tex.add(var2.getTextureID("leg_right"));
            var3 = Bitmap.createBitmap(var1, 44, 16, 4, 4, matrix, false);
            var5 = Bitmap.createBitmap(var1, 48, 16, 4, 4, matrix, false);
            var7 = this.convert_to_4x16(Bitmap.createBitmap(var1, 44, 20, 4, 12, matrix, false));
            var6 = this.convert_to_4x16(Bitmap.createBitmap(var1, 52, 20, 4, 12, matrix, false));
            var4 = this.convert_to_4x16(Bitmap.createBitmap(var1, 40, 20, 4, 12, (Matrix)null, false));
            var1 = this.convert_to_4x16(Bitmap.createBitmap(var1, 48, 20, 4, 12, (Matrix)null, false));
            this.addOrReplaceTexture(var2, "arm_top", new Texture(var3));
            this.addOrReplaceTexture(var2, "arm_bottom", new Texture(var5));
            this.addOrReplaceTexture(var2, "arm_front", new Texture(var7));
            this.addOrReplaceTexture(var2, "arm_back", new Texture(var6));
            this.addOrReplaceTexture(var2, "arm_left", new Texture(var1));
            this.addOrReplaceTexture(var2, "arm_right", new Texture(var4));
            this.part_arm_tex.add(var2.getTextureID("arm_top"));
            this.part_arm_tex.add(var2.getTextureID("arm_bottom"));
            this.part_arm_tex.add(var2.getTextureID("arm_front"));
            this.part_arm_tex.add(var2.getTextureID("arm_back"));
            this.part_arm_tex.add(var2.getTextureID("arm_left"));
            this.part_arm_tex.add(var2.getTextureID("arm_right"));
        }

    }

    public Object3D[] getObjects() {
        Object3D[] var1;
        if (this.is64x64) {
            var1 = new Object3D[]{draw_head(0.0F, 0.0F, 0.0F, this.part_head_tex), draw_hat(0.125F, 0.125F, 0.125F, this.part_hat_tex), draw_body(0.0F, 3.0F, 0.0F, this.part_body_tex), draw_jacket(0.0F, 4.0F, 0.0F, this.part_jacket_tex), draw_limb_new(-0.5F, 5.5F, 0.0F, this.part_leg_r_tex, false, false), draw_limb_new(-0.5F, 5.5F, 0.0F, this.part_leg_r_tex_2, true, true), draw_limb_new(0.5F, 5.5F, 0.0F, this.part_leg_l_tex, true, false), draw_limb_new(0.5F, 5.5F, 0.0F, this.part_leg_l_tex_2, true, true), draw_limb_new(-1.5F, 2.5F, 0.0F, this.part_arm_r_tex, false, false), draw_limb_new(-1.5F, 2.5F, 0.0F, this.part_arm_r_tex_2, false, true), draw_limb_new(1.5F, 2.5F, 0.0F, this.part_arm_l_tex, true, false), draw_limb_new(1.5F, 2.5F, 0.0F, this.part_arm_l_tex_2, true, true)};
        } else {
            var1 = new Object3D[]{draw_head(0.0F, 0.0F, 0.0F, this.part_head_tex), draw_hat(0.125F, 0.125F, 0.125F, this.part_hat_tex), draw_body(0.0F, 3.0F, 0.0F, this.part_body_tex), draw_limb(-0.5F, 5.5F, 0.0F, this.part_leg_tex, false), draw_limb(0.5F, 5.5F, 0.0F, this.part_leg_tex, true), draw_limb(-1.5F, 2.5F, 0.0F, this.part_arm_tex, false), draw_limb(1.5F, 2.5F, 0.0F, this.part_arm_tex, true)};
        }

        return var1;
    }
}
