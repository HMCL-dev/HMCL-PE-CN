package com.tungsten.hmclpe.skin.animation;

import com.tungsten.hmclpe.skin.FunctionHelper;
import com.tungsten.hmclpe.skin.SkinAnimation;
import com.tungsten.hmclpe.skin.SkinCanvas;
import com.tungsten.hmclpe.skin.SkinTransition;

import javafx.util.Duration;

public final class SkinAniWavingArms extends SkinAnimation {

    public SkinAniWavingArms(int weight, int time, double angle, SkinCanvas canvas) {
        SkinTransition larmTransition = new SkinTransition(Duration.millis(time), v -> v * angle,
                canvas.larm.getZRotate().angleProperty());

        SkinTransition rarmTransition = new SkinTransition(Duration.millis(time), v -> v * -angle,
                canvas.rarm.getZRotate().angleProperty());

        FunctionHelper.alwaysB(SkinTransition::setAutoReverse, true, larmTransition, rarmTransition);
        FunctionHelper.alwaysB(SkinTransition::setCycleCount, 2, larmTransition, rarmTransition);
        FunctionHelper.always(transitions::add, larmTransition, rarmTransition);
        this.weight = weight;
        init();
    }

}