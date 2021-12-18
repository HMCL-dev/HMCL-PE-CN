package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.jfoenix.controls.JFXDialogLayout;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.skin.SkinCanvas;
import com.tungsten.hmclpe.skin.animation.SkinAniRunning;
import com.tungsten.hmclpe.skin.animation.SkinAniWavingArms;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class OfflineAccountSkinDialog extends Dialog {

    public OfflineAccountSkinDialog(@NonNull Context context) {
        super(context);

        //SkinCanvas canvas = new SkinCanvas(SkinCanvas.STEVE, 300, 300, true);
        //StackPane canvasPane = new StackPane(canvas);
        //canvasPane.setPrefWidth(300);
        //canvasPane.setPrefHeight(300);
        //pane.setCenter(canvas);
        //canvas.getAnimationplayer().addSkinAnimation(new SkinAniWavingArms(100, 2000, 7.5, canvas), new SkinAniRunning(100, 100, 30, canvas));
        //canvas.enableRotation(.5);
        setContentView(R.layout.dialog_offline_skin);
    }
}
