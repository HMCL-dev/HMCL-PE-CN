package com.tungsten.hmclpe.launcher.list.local.controller;

import com.tungsten.hmclpe.control.view.BaseView;

import java.util.ArrayList;

public class ChildLayout {

    public String name;
    public int visibility;
    public ArrayList<BaseView> viewList;

    public ChildLayout (String name,int visibility,ArrayList<BaseView> viewList){
        this.name = name;
        this.visibility = visibility;
        this.viewList = viewList;
    }

}
