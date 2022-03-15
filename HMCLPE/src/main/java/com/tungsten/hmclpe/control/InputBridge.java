package com.tungsten.hmclpe.control;

import net.kdt.pojavlaunch.LWJGLGLFWKeycode;

import org.lwjgl.glfw.CallbackBridge;

import cosine.boat.BoatInput;
import cosine.boat.keyboard.BoatKeycodes;

public class InputBridge {

    public static final int MOUSE_LEFT        = 0;
    public static final int MOUSE_RIGHT       = 1;
    public static final int MOUSE_MIDDLE      = 2;
    public static final int MOUSE_SCROLL_UP   = 3;
    public static final int MOUSE_SCROLL_DOWN = 4;

    public static void sendEvent(int launcher,int keyCode,boolean press) {
        if (keyCode == 0 || keyCode == 1 || keyCode == 2 || keyCode == 3 || keyCode == 4) {
            sendMouseEvent(launcher,keyCode,press);
        }
        else {
            sendKeycode(launcher,getKeyCode(launcher,keyCode),press);
        }
    }

    public static void sendKeycode(int launcher,int keyCode,boolean press){
        if (launcher == 1){
            BoatInput.setKey(keyCode,0,press);
        }
        if (launcher == 2){
            CallbackBridge.sendKeyPress(keyCode, CallbackBridge.getCurrentMods(), press);
        }
    }

    public static void sendMouseEvent(int launcher,int bridge,boolean press){
        if (launcher == 1){
            BoatInput.setMouseButton(getMouseEvent(launcher,bridge),press);
        }
        if (launcher == 2){
            if (getMouseEvent(launcher,bridge) == 10) {
                if (press){
                    CallbackBridge.sendScroll(0, 1d);
                }
            }
            else if (getMouseEvent(launcher,bridge) == 11) {
                if (press){
                    CallbackBridge.sendScroll(0, -1d);
                }
            }
            else {
                CallbackBridge.sendMouseButton(getMouseEvent(launcher,bridge),press);
            }
        }
    }

    public static boolean setPointer(int launcher,int x,int y){
        if (launcher == 1){
            BoatInput.setPointer(x,y);
            return true;
        }
        if (launcher == 2){
            return CallbackBridge.sendCursorPos(x,y);
        }
        return false;
    }

    public static void sendKeyChar(int launcher,char keyChar) {
        if (launcher == 1) {
            BoatInput.setKey(0,keyChar,true);
            BoatInput.setKey(0,keyChar,false);
        }
        if (launcher == 2) {
            CallbackBridge.sendChar(keyChar,CallbackBridge.getCurrentMods());
        }
    }

    public static int getMouseEvent(int launcher,int bridge){
        switch (bridge) {
            case MOUSE_LEFT:
                if (launcher == 1){
                    return BoatInput.Button1;
                }
                else {
                    return LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_LEFT;
                }
            case MOUSE_RIGHT:
                if (launcher == 1){
                    return BoatInput.Button3;
                }
                else {
                    return LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_RIGHT;
                }
            case MOUSE_MIDDLE:
                if (launcher == 1){
                    return BoatInput.Button2;
                }
                else {
                    return LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_MIDDLE;
                }
            case MOUSE_SCROLL_UP:
                if (launcher == 1){
                    return BoatInput.Button4;
                }
                else {
                    return 10;
                }
            case MOUSE_SCROLL_DOWN:
                if (launcher == 1){
                    return BoatInput.Button5;
                }
                else {
                    return 11;
                }
            default:
                return -1;
        }
    }

    public static int getKeyCode(int launcher,int raw) {
        if (launcher == 1) {
            int code;
            switch (raw) {
                case LWJGLGLFWKeycode.GLFW_KEY_F1:
                    code = BoatKeycodes.BOAT_KEYBOARD_F1;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F2:
                    code = BoatKeycodes.BOAT_KEYBOARD_F2;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F3:
                    code = BoatKeycodes.BOAT_KEYBOARD_F3;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F4:
                    code = BoatKeycodes.BOAT_KEYBOARD_F4;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F5:
                    code = BoatKeycodes.BOAT_KEYBOARD_F5;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F6:
                    code = BoatKeycodes.BOAT_KEYBOARD_F6;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F7:
                    code = BoatKeycodes.BOAT_KEYBOARD_F7;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F8:
                    code = BoatKeycodes.BOAT_KEYBOARD_F8;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F9:
                    code = BoatKeycodes.BOAT_KEYBOARD_F9;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F10:
                    code = BoatKeycodes.BOAT_KEYBOARD_F10;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F11:
                    code = BoatKeycodes.BOAT_KEYBOARD_F11;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_F12:
                    code = BoatKeycodes.BOAT_KEYBOARD_F12;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_ESCAPE:
                    code = BoatKeycodes.BOAT_KEYBOARD_Escape;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_PRINT_SCREEN:
                    code = BoatKeycodes.BOAT_KEYBOARD_Print;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_SCROLL_LOCK:
                    code = BoatKeycodes.BOAT_KEYBOARD_Scroll_Lock;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_PAUSE:
                    code = BoatKeycodes.BOAT_KEYBOARD_Pause;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_GRAVE_ACCENT:
                    code = BoatKeycodes.BOAT_KEYBOARD_grave;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_MINUS:
                    code = BoatKeycodes.BOAT_KEYBOARD_minus;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_EQUAL:
                    code = BoatKeycodes.BOAT_KEYBOARD_equal;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_BACKSPACE:
                    code = BoatKeycodes.BOAT_KEYBOARD_BackSpace;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_TAB:
                    code = BoatKeycodes.BOAT_KEYBOARD_Tab;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_LEFT_BRACKET:
                    code = BoatKeycodes.BOAT_KEYBOARD_bracketleft;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_RIGHT_BRACKET:
                    code = BoatKeycodes.BOAT_KEYBOARD_bracketright;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_BACKSLASH:
                    code = BoatKeycodes.BOAT_KEYBOARD_backslash;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_CAPS_LOCK:
                    code = BoatKeycodes.BOAT_KEYBOARD_Caps_Lock;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_SEMICOLON:
                    code = BoatKeycodes.BOAT_KEYBOARD_semicolon;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_APOSTROPHE:
                    code = BoatKeycodes.BOAT_KEYBOARD_apostrophe;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_ENTER:
                    code = BoatKeycodes.BOAT_KEYBOARD_Return;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_LEFT_SHIFT:
                    code = BoatKeycodes.BOAT_KEYBOARD_Shift_L;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_COMMA:
                    code = BoatKeycodes.BOAT_KEYBOARD_comma;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_PERIOD:
                    code = BoatKeycodes.BOAT_KEYBOARD_period;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_SLASH:
                    code = BoatKeycodes.BOAT_KEYBOARD_slash;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_RIGHT_SHIFT:
                    code = BoatKeycodes.BOAT_KEYBOARD_Shift_R;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_LEFT_CONTROL:
                    code = BoatKeycodes.BOAT_KEYBOARD_Control_L;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_LEFT_SUPER:
                    code = BoatKeycodes.BOAT_KEYBOARD_Super_L;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_LEFT_ALT:
                    code = BoatKeycodes.BOAT_KEYBOARD_Alt_L;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_SPACE:
                    code = BoatKeycodes.BOAT_KEYBOARD_space;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_RIGHT_ALT:
                    code = BoatKeycodes.BOAT_KEYBOARD_Alt_R;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_RIGHT_SUPER:
                    code = BoatKeycodes.BOAT_KEYBOARD_Super_R;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_MENU:
                    code = BoatKeycodes.BOAT_KEYBOARD_Menu;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_RIGHT_CONTROL:
                    code = BoatKeycodes.BOAT_KEYBOARD_Control_R;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_INSERT:
                    code = BoatKeycodes.BOAT_KEYBOARD_Insert;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_HOME:
                    code = BoatKeycodes.BOAT_KEYBOARD_Home;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_PAGE_UP:
                    code = BoatKeycodes.BOAT_KEYBOARD_Page_Up;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_DELETE:
                    code = BoatKeycodes.BOAT_KEYBOARD_Delete;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_END:
                    code = BoatKeycodes.BOAT_KEYBOARD_End;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_PAGE_DOWN:
                    code = BoatKeycodes.BOAT_KEYBOARD_Page_Down;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_UP:
                    code = BoatKeycodes.BOAT_KEYBOARD_Up;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_DOWN:
                    code = BoatKeycodes.BOAT_KEYBOARD_Down;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_LEFT:
                    code = BoatKeycodes.BOAT_KEYBOARD_Left;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_RIGHT:
                    code = BoatKeycodes.BOAT_KEYBOARD_Right;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_NUM_LOCK:
                    code = BoatKeycodes.BOAT_KEYBOARD_Num_Lock;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_DIVIDE:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_Divide;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_MULTIPLY:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_Multiply;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_SUBTRACT:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_Subtract;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_ADD:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_Add;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_ENTER:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_Enter;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_EQUAL:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_Equal;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_0:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_0;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_1:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_1;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_2:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_2;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_3:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_3;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_4:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_4;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_5:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_5;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_6:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_6;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_7:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_7;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_8:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_8;
                    break;
                case LWJGLGLFWKeycode.GLFW_KEY_KP_9:
                    code = BoatKeycodes.BOAT_KEYBOARD_KP_9;
                    break;
                default:
                    code = raw;
                    break;
            }
            return code;
        }
        else {
            return raw;
        }
    }

}
