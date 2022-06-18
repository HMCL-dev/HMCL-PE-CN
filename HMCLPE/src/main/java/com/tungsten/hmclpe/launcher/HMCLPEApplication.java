package com.tungsten.hmclpe.launcher;

import android.app.Application;
import android.content.Context;


public class HMCLPEApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
    public static void releaseContext(){
        context=null;
    }

}