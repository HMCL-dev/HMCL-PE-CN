package com.tungsten.hmclpe.control.view;

import static com.tungsten.hmclpe.control.bean.BaseButtonInfo.POSITION_TYPE_PERCENT;
import static com.tungsten.hmclpe.control.bean.BaseButtonInfo.SIZE_OBJECT_WIDTH;
import static com.tungsten.hmclpe.control.bean.BaseButtonInfo.SIZE_TYPE_PERCENT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.MenuHelper;
import com.tungsten.hmclpe.control.bean.BaseButtonInfo;
import com.tungsten.hmclpe.launcher.dialogs.control.EditButtonDialog;
import com.tungsten.hmclpe.launcher.list.local.controller.ChildLayout;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;

@SuppressLint("ViewConstructor")
public class BaseButton extends androidx.appcompat.widget.AppCompatButton {

    public int screenWidth;
    public int screenHeight;
    public BaseButtonInfo info;
    public MenuHelper menuHelper;

    public GradientDrawable drawableNormal;
    public GradientDrawable drawablePress;

    private long downTime;
    private float initialX;
    private float initialY;

    private final Paint outlinePaint;

    private final Handler deleteHandler = new Handler();
    private final Runnable deleteRunnable = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getContext().getString(R.string.dialog_delete_button_title));
            builder.setMessage(getContext().getString(R.string.dialog_delete_button_content));
            builder.setPositiveButton(getContext().getString(R.string.dialog_delete_button_positive), (dialogInterface, i) -> {
                deleteButton();
            });
            builder.setNegativeButton(getContext().getString(R.string.dialog_delete_button_negative), (dialogInterface, i) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    public BaseButton(Context context, int screenWidth, int screenHeight, BaseButtonInfo info, MenuHelper menuHelper) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.menuHelper = menuHelper;

        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setColor(getContext().getColor(R.color.colorRed));
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(3);

        refreshStyle(info);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (menuHelper.showOutline) {
            Path outlinePath = new Path();
            outlinePath.moveTo(0,0);
            outlinePath.lineTo(getWidth(),0);
            outlinePath.lineTo(getWidth(),getHeight());
            outlinePath.lineTo(0,getHeight());
            outlinePath.lineTo(0,0);
            canvas.drawPath(outlinePath,outlinePaint);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (menuHelper.editMode) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downTime = System.currentTimeMillis();
                    initialX = event.getX();
                    initialY = event.getY();
                    deleteHandler.postDelayed(deleteRunnable,600);
                    setPressDrawable();
                    menuHelper.viewManager.layoutPanel.showReference(info.positionType,getX(),getY(),this.getWidth(),this.getHeight());
                    break;
                case MotionEvent.ACTION_MOVE:
                    float targetX;
                    float targetY;
                    if (getX() + event.getX() - initialX >= 0 && getX() + event.getX() - initialX <= screenWidth - getWidth()){
                        targetX = getX() + event.getX() - initialX;
                    }
                    else if (getX() + event.getX() - initialX < 0){
                        targetX = 0;
                    }
                    else {
                        targetX = screenWidth - getWidth();
                    }
                    if (getY() + event.getY() - initialY >= 0 && getY() + event.getY() - initialY <= screenHeight - getHeight()){
                        targetY = getY() + event.getY() - initialY;
                    }
                    else if (getY() + event.getY() - initialY < 0){
                        targetY = 0;
                    }
                    else {
                        targetY = screenHeight - getHeight();
                    }
                    setX(targetX);
                    setY(targetY);
                    info.xPosition.absolutePosition = ConvertUtils.px2dip(getContext(),targetX);
                    info.yPosition.absolutePosition = ConvertUtils.px2dip(getContext(),targetY);
                    info.xPosition.percentPosition = targetX / (screenWidth - getWidth());
                    info.yPosition.percentPosition = targetY / (screenHeight - getHeight());
                    saveButtonInfo();
                    menuHelper.viewManager.layoutPanel.showReference(info.positionType,getX(),getY(),this.getWidth(),this.getHeight());
                    if (Math.abs(event.getX() - initialX) > 5 || Math.abs(event.getY() - initialY) > 5){
                        deleteHandler.removeCallbacks(deleteRunnable);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    deleteHandler.removeCallbacks(deleteRunnable);
                    if (System.currentTimeMillis() - downTime <= 200 && Math.abs(event.getX() - initialX) <= 10 && Math.abs(event.getY() - initialY) <= 10){
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            EditButtonDialog dialog = new EditButtonDialog(getContext(), info.pattern, info.child,screenWidth,screenHeight,this,menuHelper.fullscreen);
                            dialog.show();
                        }
                    }
                    setNormalDrawable();
                    menuHelper.viewManager.layoutPanel.hideReference();
                    break;
            }
        }
        else {

        }
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

    public void saveButtonInfo() {
        if (menuHelper.editMode) {
            ChildLayout childLayout = null;
            for (ChildLayout child : SettingUtils.getChildList(info.pattern)) {
                if (child.name.equals(menuHelper.currentChild)) {
                    childLayout = child;
                }
            }
            assert childLayout != null;
            boolean exist = false;
            for (int i = 0;i < childLayout.baseButtonList.size();i++) {
                if (childLayout.baseButtonList.get(i).uuid.equals(info.uuid)) {
                    childLayout.baseButtonList.get(i).refresh(info);
                    exist = true;
                }
            }
            if (!exist) {
                childLayout.baseButtonList.add(info);
            }
            ChildLayout.saveChildLayout(info.pattern,childLayout);
        }
    }

    public void deleteButton () {
        if (menuHelper.editMode) {
            ChildLayout childLayout = null;
            for (ChildLayout child : SettingUtils.getChildList(info.pattern)) {
                if (child.name.equals(menuHelper.currentChild)) {
                    childLayout = child;
                }
            }
            assert childLayout != null;
            for (int i = 0;i < childLayout.baseButtonList.size();i++) {
                if (childLayout.baseButtonList.get(i).uuid.equals(info.uuid)) {
                    childLayout.baseButtonList.remove(i);
                    break;
                }
            }
            ChildLayout.saveChildLayout(info.pattern,childLayout);
            menuHelper.viewManager.layoutPanel.removeView(this);
        }
    }
}
