package com.tungsten.hmclpe.control.view;

import static com.tungsten.hmclpe.control.bean.BaseButtonInfo.POSITION_TYPE_PERCENT;
import static com.tungsten.hmclpe.control.bean.BaseButtonInfo.SIZE_OBJECT_WIDTH;
import static com.tungsten.hmclpe.control.bean.BaseButtonInfo.SIZE_TYPE_PERCENT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.control.bean.BaseButtonInfo;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;

@SuppressLint("ViewConstructor")
public class BaseButton extends androidx.appcompat.widget.AppCompatButton {

    public int screenWidth;
    public int screenHeight;
    public BaseButtonInfo info;
    public MenuHelper menuHelper;

    public GradientDrawable drawableNormal;
    public GradientDrawable drawablePress;

    public BaseButton(Context context, int screenWidth, int screenHeight, BaseButtonInfo info, MenuHelper menuHelper) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.menuHelper = menuHelper;
        refreshStyle(info);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void refreshStyle (BaseButtonInfo info) {
        this.info = info;
        drawableNormal = new GradientDrawable();
        drawablePress = new GradientDrawable();
        drawableNormal.setCornerRadius(ConvertUtils.dip2px(getContext(),info.buttonStyle.cornerRadius));
        drawableNormal.setStroke(ConvertUtils.dip2px(getContext(),info.buttonStyle.strokeWidth), Color.parseColor(info.buttonStyle.strokeColor));
        drawableNormal.setColor(Color.parseColor(info.buttonStyle.fillColor));
        drawablePress.setCornerRadius(ConvertUtils.dip2px(getContext(),info.buttonStyle.cornerRadiusPress));
        drawablePress.setStroke(ConvertUtils.dip2px(getContext(),info.buttonStyle.strokeWidthPress), Color.parseColor(info.buttonStyle.strokeColorPress));
        drawablePress.setColor(Color.parseColor(info.buttonStyle.fillColorPress));
        this.setText(info.text);
        this.setGravity(Gravity.CENTER);
        this.setPadding(0,0,0,0);
        this.setAllCaps(false);
        setNormalDrawable();
    }

    public void setNormalDrawable(){
        setTextSize(info.buttonStyle.textSize);
        setTextColor(Color.parseColor(info.buttonStyle.textColor));
        setBackground(drawableNormal);
    }

    public void setPressDrawable(){
        setTextSize(info.buttonStyle.textSizePress);
        setTextColor(Color.parseColor(info.buttonStyle.textColorPress));
        setBackground(drawablePress);
    }

    public void updateSizeAndPosition (BaseButtonInfo info) {
        this.info = info;
        int width;
        int height;
        if (info.sizeType == SIZE_TYPE_PERCENT){
            if (info.width.object == SIZE_OBJECT_WIDTH){
                width = (int) (screenWidth * info.width.percentSize);
            }
            else {
                width = (int) (screenHeight * info.width.percentSize);
            }
            if (info.height.object == SIZE_OBJECT_WIDTH){
                height = (int) (screenWidth * info.height.percentSize);
            }
            else {
                height = (int) (screenHeight * info.height.percentSize);
            }
        }
        else {
            width = ConvertUtils.dip2px(getContext(),info.width.absoluteSize);
            height = ConvertUtils.dip2px(getContext(),info.height.absoluteSize);
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        if (info.positionType == POSITION_TYPE_PERCENT){
            setX((screenWidth - width) * info.xPosition.percentPosition);
            setY((screenHeight - height) * info.yPosition.percentPosition);
        }
        else {
            setX(ConvertUtils.dip2px(getContext(),info.xPosition.absolutePosition));
            setY(ConvertUtils.dip2px(getContext(),info.yPosition.absolutePosition));
        }
    }
}
