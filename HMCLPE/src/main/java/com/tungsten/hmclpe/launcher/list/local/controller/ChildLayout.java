package com.tungsten.hmclpe.launcher.list.local.controller;

import com.google.gson.Gson;
import com.tungsten.hmclpe.control.view.BaseButton;
import com.tungsten.hmclpe.control.view.BaseRockerView;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.file.FileStringUtils;

import java.util.ArrayList;

public class ChildLayout {

    public String name;
    public int visibility;
    public ArrayList<BaseButton> baseButtonList;
    public ArrayList<BaseRockerView> baseRockerViewList;

    public ChildLayout (String name,int visibility,ArrayList<BaseButton> baseButtonList,ArrayList<BaseRockerView> baseRockerViewList){
        this.name = name;
        this.visibility = visibility;
        this.baseButtonList = baseButtonList;
        this.baseRockerViewList = baseRockerViewList;
    }

    public static void saveChildLayout(String pattern,ChildLayout childLayout){
        Gson gson = new Gson();
        String string = gson.toJson(childLayout);
        FileStringUtils.writeFile(AppManifest.CONTROLLER_DIR + "/" + pattern + "/" + childLayout.name + ".json",string);
    }

}
