package com.tungsten.hmclpe.control;

import android.view.KeyEvent;

import net.kdt.pojavlaunch.keyboard.LWJGLGLFWKeycode;

public class MKManager {

    private final MenuHelper menuHelper;

    public MKManager(MenuHelper menuHelper) {
        this.menuHelper = menuHelper;
        menuHelper.baseLayout.setFocusable(true);
        menuHelper.baseLayout.setClickable(true);
        menuHelper.baseLayout.setupMKController(menuHelper);
    }

    public void enableCursor() {
        menuHelper.baseLayout.enableCursor();
    }

    public void disableCursor() {
        menuHelper.baseLayout.disableCursor();
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_0, true);
            case KeyEvent.KEYCODE_1:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_1, true);
            case KeyEvent.KEYCODE_2:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_2, true);
            case KeyEvent.KEYCODE_3:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_3, true);
            case KeyEvent.KEYCODE_4:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_4, true);
            case KeyEvent.KEYCODE_5:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_5, true);
            case KeyEvent.KEYCODE_6:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_6, true);
            case KeyEvent.KEYCODE_7:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_7, true);
            case KeyEvent.KEYCODE_8:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_8, true);
            case KeyEvent.KEYCODE_9:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_9, true);
        }
    }

    public void onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_0, false);
            case KeyEvent.KEYCODE_1:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_1, false);
            case KeyEvent.KEYCODE_2:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_2, false);
            case KeyEvent.KEYCODE_3:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_3, false);
            case KeyEvent.KEYCODE_4:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_4, false);
            case KeyEvent.KEYCODE_5:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_5, false);
            case KeyEvent.KEYCODE_6:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_6, false);
            case KeyEvent.KEYCODE_7:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_7, false);
            case KeyEvent.KEYCODE_8:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_8, false);
            case KeyEvent.KEYCODE_9:
                InputBridge.sendKeycode(menuHelper.launcher, LWJGLGLFWKeycode.GLFW_KEY_9, false);
        }
    }
}
