package com.tungsten.hmclpe.skin.body.cube;

import com.tungsten.hmclpe.skin.body.MainCube;

public class BodyOverlay extends MainCube
{
    protected float[] jacket_texcoords;
    
    public BodyOverlay() {
        super(8.470589f, 12.705883f, 4.2352943f, 0.0f, 2.0f, 0.0f, -0.15f, 0.0f, 1.0f, 0.0f, 3.0f, -3.0f);
        this.AddTextures(this.jacket_texcoords = new float[] { 0.3125f, 0.75f, 0.3125f, 0.5625f, 0.4375f, 0.5625f, 0.4375f, 0.75f, 0.3125f, 0.5625f, 0.3125f, 0.5f, 0.4375f, 0.5f, 0.4375f, 0.5625f, 0.4375f, 0.5625f, 0.4375f, 0.5f, 0.5625f, 0.5f, 0.5625f, 0.5625f, 0.4375f, 0.75f, 0.4375f, 0.5625f, 0.5f, 0.5625f, 0.5f, 0.75f, 0.25f, 0.75f, 0.25f, 0.5625f, 0.3125f, 0.5625f, 0.3125f, 0.75f, 0.5f, 0.75f, 0.5f, 0.5625f, 0.625f, 0.5625f, 0.625f, 0.75f });
    }
}
