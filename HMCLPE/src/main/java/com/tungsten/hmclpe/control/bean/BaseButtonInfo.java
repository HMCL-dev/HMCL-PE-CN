package com.tungsten.hmclpe.control.bean;

import android.content.Context;
import android.view.Gravity;

import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.control.bean.button.ButtonPosition;
import com.tungsten.hmclpe.control.bean.button.ButtonSize;
import com.tungsten.hmclpe.control.bean.button.ButtonStyle;
import com.tungsten.hmclpe.control.view.BaseButton;
import com.tungsten.hmclpe.control.view.LayoutPanel;

import java.util.ArrayList;

public class BaseButtonInfo {

    public static final int SIZE_TYPE_PERCENT = 0;
    public static final int SIZE_TYPE_ABSOLUTE = 1;

    public static final int SIZE_OBJECT_WIDTH = 0;
    public static final int SIZE_OBJECT_HEIGHT = 1;

    public static final int POSITION_TYPE_PERCENT = 0;
    public static final int POSITION_TYPE_ABSOLUTE = 1;

    public static final int FUNCTION_TYPE_TOUCH = 0;
    public static final int FUNCTION_TYPE_DOUBLE_CLICK = 1;

    public String text;
    public int sizeType;
    public ButtonSize width;
    public ButtonSize height;
    public int positionType;
    public ButtonPosition xPosition;
    public ButtonPosition yPosition;
    public int functionType;
    public boolean viewMove;
    public boolean autoKeep;
    public boolean autoClick;
    public boolean openMenu;
    public boolean movable;
    public boolean switchTouchMode;
    public boolean switchSensor;
    public boolean switchLeftPad;
    public boolean showInputDialog;
    public ArrayList<String> visibilityControl;
    public String outputText;
    public ArrayList<Integer> outputKeycode;
    public boolean usingExist;
    public ButtonStyle buttonStyle;

    public BaseButtonInfo (String text, int sizeType, ButtonSize width, ButtonSize height, int positionType, ButtonPosition xPosition, ButtonPosition yPosition, int functionType, boolean viewMove, boolean autoKeep, boolean autoClick,boolean openMenu,boolean movable, boolean switchTouchMode, boolean switchSensor, boolean switchLeftPad, boolean showInputDialog, ArrayList<String> visibilityControl, String outputText, ArrayList<Integer> outputKeycode, boolean usingExist, ButtonStyle buttonStyle) {
        this.text = text;
        this.sizeType = sizeType;
        this.width = width;
        this.height = height;
        this.positionType = positionType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.functionType = functionType;
        this.viewMove = viewMove;
        this.autoKeep = autoKeep;
        this.autoClick = autoClick;
        this.openMenu = openMenu;
        this.movable = movable;
        this.switchTouchMode = switchTouchMode;
        this.switchSensor = switchSensor;
        this.switchLeftPad = switchLeftPad;
        this.showInputDialog = showInputDialog;
        this.visibilityControl = visibilityControl;
        this.outputText = outputText;
        this.outputKeycode = outputKeycode;
        this.usingExist = usingExist;
        this.buttonStyle = buttonStyle;
    }

    public static void addButton(Context context, MenuHelper menuHelper, BaseButtonInfo info, int screenWidth, int screenHeight, LayoutPanel layoutPanel){
        BaseButton baseButton = new BaseButton(context,screenWidth,screenHeight,info,menuHelper);
        layoutPanel.addView(baseButton);
        int width;
        int height;
        if (info.sizeType == SIZE_TYPE_PERCENT){
            if (info.width.object == SIZE_OBJECT_WIDTH){
                baseButton.setWidth((int) (screenWidth * info.width.percentSize));
                width = (int) (screenWidth * info.width.percentSize);
            }
            else {
                baseButton.setWidth((int) (screenHeight * info.width.percentSize));
                width = (int) (screenHeight * info.width.percentSize);
            }
            if (info.height.object == SIZE_OBJECT_WIDTH){
                baseButton.setHeight((int) (screenWidth * info.height.percentSize));
                height = (int) (screenWidth * info.height.percentSize);
            }
            else {
                baseButton.setHeight((int) (screenHeight * info.height.percentSize));
                height = (int) (screenHeight * info.height.percentSize);
            }
        }
        else {
            baseButton.setWidth(info.width.absoluteSize);
            baseButton.setHeight(info.height.absoluteSize);
            width = info.width.absoluteSize;
            height = info.height.absoluteSize;
        }
        baseButton.setText(info.text);
        baseButton.setGravity(Gravity.CENTER);
        if (info.positionType == POSITION_TYPE_PERCENT){
            baseButton.setX((screenWidth - width) * info.xPosition.percentPosition);
            baseButton.setY((screenHeight - height) * info.yPosition.percentPosition);
        }
        else {
            baseButton.setX(info.xPosition.absolutePosition);
            baseButton.setY(info.yPosition.absolutePosition);
        }
    }

}
