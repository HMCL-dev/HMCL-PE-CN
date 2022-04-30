package com.tungsten.hmclpe.skin.body.cube;

import com.tungsten.hmclpe.skin.body.MainCube;

public class Head extends MainCube
{
    protected float[] head_texcoords;
    
    public Head() {
        super(8.0f, 8.0f, 8.0f, 0.0f, 12.0f, 0.0f, 0.25f, 0.0f, 1.0f, 0.0f, 5.0f, -5.0f);
        this.AddTextures(this.head_texcoords = new float[] { 0.125f, 0.25f, 0.125f, 0.125f, 0.25f, 0.125f, 0.25f, 0.25f, 0.125f, 0.125f, 0.125f, 0.0f, 0.25f, 0.0f, 0.25f, 0.125f, 0.25f, 0.125f, 0.25f, 0.0f, 0.375f, 0.0f, 0.375f, 0.125f, 0.25f, 0.25f, 0.25f, 0.125f, 0.375f, 0.125f, 0.375f, 0.25f, 0.0f, 0.25f, 0.0f, 0.125f, 0.125f, 0.125f, 0.125f, 0.25f, 0.375f, 0.25f, 0.375f, 0.125f, 0.5f, 0.125f, 0.5f, 0.25f });
    }
}
