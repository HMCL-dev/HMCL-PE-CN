package com.tungsten.hmclpe.launcher.game;

import java.util.List;

public class Arguments {

    public List<String> game;
    public List<String> jvm;

    public Arguments(List<String> game,List<String> jvm) {
        this.game = game;
        this.jvm = jvm;
    }
}
