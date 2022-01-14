package com.tungsten.hmclpe.skin;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.util.SkyBox;
import com.tungsten.hmclpe.R;

import java.io.PrintStream;

public class Draw3DSupport {
    public Draw3DSupport() {
    }

    public static Object3D createUniversalLine(SimpleVector var0, SimpleVector var1, float var2, String var3) {
        Object3D var4 = new Object3D(8);
        var2 /= 2.0F;
        var4.addTriangle(new SimpleVector(var0.x - var2, var0.y, var0.z), 0.0F, 0.0F, new SimpleVector(var0.x + var2, var0.y, var0.z), 0.0F, 1.0F, new SimpleVector(var1.x + var2, var1.y, var1.z), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.addTriangle(new SimpleVector(var1.x + var2, var1.y, var1.z), 0.0F, 0.0F, new SimpleVector(var1.x - var2, var1.y, var1.z), 0.0F, 1.0F, new SimpleVector(var0.x - var2, var0.y, var0.z), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.addTriangle(new SimpleVector(var1.x - var2, var1.y, var1.z), 0.0F, 0.0F, new SimpleVector(var1.x + var2, var1.y, var1.z), 0.0F, 1.0F, new SimpleVector(var0.x + var2, var0.y, var0.z), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.addTriangle(new SimpleVector(var0.x + var2, var0.y, var0.z), 0.0F, 0.0F, new SimpleVector(var0.x - var2, var0.y, var0.z), 0.0F, 1.0F, new SimpleVector(var1.x - var2, var1.y, var1.z), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.addTriangle(new SimpleVector(var0.x, var0.y, var0.z + var2), 0.0F, 0.0F, new SimpleVector(var0.x, var0.y, var0.z - var2), 0.0F, 1.0F, new SimpleVector(var1.x, var1.y, var1.z - var2), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.addTriangle(new SimpleVector(var1.x, var1.y, var1.z - var2), 0.0F, 0.0F, new SimpleVector(var1.x, var1.y, var1.z + var2), 0.0F, 1.0F, new SimpleVector(var0.x, var0.y, var0.z + var2), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.addTriangle(new SimpleVector(var1.x, var1.y, var1.z + var2), 0.0F, 0.0F, new SimpleVector(var1.x, var1.y, var1.z - var2), 0.0F, 1.0F, new SimpleVector(var0.x, var0.y, var0.z - var2), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.addTriangle(new SimpleVector(var0.x, var0.y, var0.z - var2), 0.0F, 0.0F, new SimpleVector(var0.x, var0.y, var0.z + var2), 0.0F, 1.0F, new SimpleVector(var1.x, var1.y, var1.z + var2), 1.0F, 1.0F, TextureManager.getInstance().getTextureID(var3));
        var4.setAdditionalColor(new RGBColor(255, 255, 255));
        return var4;
    }

    public static SkyBox initSkybox(Context var0) {
        ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");
        MemoryInfo var2 = new MemoryInfo();
        var1.getMemoryInfo(var2);
        long var3 = var2.totalMem;
        PrintStream var9 = System.out;
        StringBuilder var6 = new StringBuilder();
        var6.append("MEM_");
        var6.append(var3);
        var9.println(var6.toString());
        PrintStream var7 = System.out;
        StringBuilder var10 = new StringBuilder();
        var10.append("MEM_");
        var3 /= 1048576L;
        var10.append(var3);
        var7.println(var10.toString());
        TextureManager var8 = TextureManager.getInstance();
        Texture var5;
        Texture var11;
        if (var3 < 1500L) {
            if (!var8.containsTexture("sky_bottom")) {
                var11 = new Texture(var0.getResources().openRawResource(R.raw.sky_bottom_low));
                var11.setFiltering(false);
                var8.addTexture("sky_bottom", var11);
            }

            if (!var8.containsTexture("sky_main")) {
                var11 = new Texture(var0.getResources().openRawResource(R.raw.sky_main_low));
                var11.setFiltering(false);
                var8.addTexture("sky_main", var11);
            }

            if (!var8.containsTexture("sky_right")) {
                var11 = new Texture(var0.getResources().openRawResource(R.raw.sky_right_low));
                var11.setFiltering(false);
                var8.addTexture("sky_right", var11);
            }

            if (!var8.containsTexture("sky_top")) {
                var5 = new Texture(var0.getResources().openRawResource(R.raw.sky_top_low));
                var5.setFiltering(false);
                var8.addTexture("sky_top", var5);
            }

            System.out.println("MEM_LOW");
        } else {
            if (!var8.containsTexture("sky_bottom")) {
                var11 = new Texture(var0.getResources().openRawResource(R.raw.sky_bottom));
                var11.setFiltering(false);
                var8.addTexture("sky_bottom", var11);
            }

            if (!var8.containsTexture("sky_main")) {
                var11 = new Texture(var0.getResources().openRawResource(R.raw.sky_main));
                var11.setFiltering(false);
                var8.addTexture("sky_main", var11);
            }

            if (!var8.containsTexture("sky_right")) {
                var11 = new Texture(var0.getResources().openRawResource(R.raw.sky_right));
                var11.setFiltering(false);
                var8.addTexture("sky_right", var11);
            }

            if (!var8.containsTexture("sky_top")) {
                var5 = new Texture(var0.getResources().openRawResource(R.raw.sky_top));
                var5.setFiltering(false);
                var8.addTexture("sky_top", var5);
            }

            System.out.println("MEM_NORMAL");
        }

        return new SkyBox("sky_main", "sky_main", "sky_right", "sky_main", "sky_top", "sky_bottom", 1024.0F);
    }
}