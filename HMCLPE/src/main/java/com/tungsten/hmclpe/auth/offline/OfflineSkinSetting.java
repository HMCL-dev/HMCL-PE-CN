package com.tungsten.hmclpe.auth.offline;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.yggdrasil.TextureModel;
import com.tungsten.hmclpe.skin.utils.Avatar;

public class OfflineSkinSetting implements Cloneable{

    public int type;
    public TextureModel model;
    public String skinPath;
    public String capePath;
    public String server;
    public String skin;
    public String cape;

    public OfflineSkinSetting(Context context){
        this(0,TextureModel.ALEX,"","","", Avatar.bitmapToString(Avatar.getBitmapFromRes(context, R.drawable.skin_alex)),"");
    }

    public OfflineSkinSetting(int type,TextureModel model,String skinPath,String capePath,String server,String skin,String cape) {
        this.type = type;
        this.model = model;
        this.skinPath = skinPath;
        this.capePath = capePath;
        this.server = server;
        this.skin = skin;
        this.cape = cape;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
