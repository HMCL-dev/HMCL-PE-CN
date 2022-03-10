package com.tungsten.hmclpe.control.view;

import android.content.Context;
import android.util.AttributeSet;

public class BaseRockerView extends RockerView{

    public BaseRockerView(Context context, String pointerColor, String pointerColorPress, int followType, boolean doubleClick, OnShakeListener onShakeListener) {
        super(context, pointerColor, pointerColorPress, followType, doubleClick, onShakeListener);
    }

}
