package net.kdt.pojavlaunch;

import static net.kdt.pojavlaunch.utils.MCOptionUtils.getMcScale;
import static org.lwjgl.glfw.CallbackBridge.sendMouseButton;
import static org.lwjgl.glfw.CallbackBridge.windowHeight;
import static org.lwjgl.glfw.CallbackBridge.windowWidth;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import net.kdt.pojavlaunch.utils.JREUtils;
import net.kdt.pojavlaunch.utils.MCOptionUtils;

import org.lwjgl.glfw.CallbackBridge;

/**
 * Class dealing with showing minecraft surface and taking inputs to dispatch them to minecraft
 */
public class MinecraftGLView extends TextureView {
    /* Pointer Debug textview, used to show info about the pointer state */
    private TextView pointerDebugText;
    /* Resolution scaler option, allow downsizing a window */
    private final float scaleFactor = LauncherPreferences.DEFAULT_PREF.getInt("resolutionRatio",100)/100f;
    /* MC GUI scale, listened by MCOptionUtils */
    private int GUIScale = getMcScale();
    private MCOptionUtils.MCOptionListener GUIScaleListener = () -> GUIScale = getMcScale();
    /* Surface ready listener, used by the activity to launch minecraft */
    SurfaceReadyListener surfaceReadyListener = null;
    /* How much distance a finger has to go for touch sloppiness to be disabled */
    public static final int FINGER_STILL_THRESHOLD = (int) Tools.dpToPx(9);
    /* How much distance a finger has to go to scroll */
    public static final int FINGER_SCROLL_THRESHOLD = (int) Tools.dpToPx(6);
    /* Handle hotbar throw button and mouse mining button */
    public static final int MSG_LEFT_MOUSE_BUTTON_CHECK = 1028;
    public static final int MSG_DROP_ITEM_BUTTON_CHECK = 1029;


    public MinecraftGLView(Context context) {
        this(context, null);
    }

    public MinecraftGLView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        //Fixes freeform and dex mode having transparent glass,
        //since it forces android to used the background color of the view/layout behind it.
        setOpaque(false);
        setFocusable(true);

        MCOptionUtils.addMCOptionListener(GUIScaleListener);
    }

    /** Initialize the view and all its settings */
    public void start(){
        // Add the pointer debug textview
        pointerDebugText = new TextView(getContext());
        pointerDebugText.setX(0);
        pointerDebugText.setY(0);
        pointerDebugText.setVisibility(GONE);
        ((ViewGroup)getParent()).addView(pointerDebugText);

        setSurfaceTextureListener(new SurfaceTextureListener() {
            private boolean isCalled = false;
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
                windowWidth = Tools.getDisplayFriendlyRes(width, scaleFactor);
                windowHeight = Tools.getDisplayFriendlyRes(height, scaleFactor);
                texture.setDefaultBufferSize(windowWidth, windowHeight);

                //Load Minecraft options:
                MCOptionUtils.load();
                MCOptionUtils.set("overrideWidth", String.valueOf(windowWidth));
                MCOptionUtils.set("overrideHeight", String.valueOf(windowHeight));
                MCOptionUtils.save();
                getMcScale();
                // Should we do that?
                if(isCalled) return;
                isCalled = true;

                JREUtils.setupBridgeWindow(new Surface(texture));

                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        if(surfaceReadyListener != null){
                            surfaceReadyListener.isReady();
                        }
                    } catch (Throwable e) {

                    }
                }, "JVM Main thread").start();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
                return true;
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
                windowWidth = Tools.getDisplayFriendlyRes(width, scaleFactor);
                windowHeight = Tools.getDisplayFriendlyRes(height, scaleFactor);
                CallbackBridge.sendUpdateWindowSize(windowWidth, windowHeight);
                getMcScale();
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture texture) {
                texture.setDefaultBufferSize(windowWidth, windowHeight);
            }
        });
    }

    /**
     * The event for mouse/joystick movements
     * We don't do the gamepad right now.
     */
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        int mouseCursorIndex = -1;

        for(int i = 0; i < event.getPointerCount(); i++) {
            if(event.getToolType(i) != MotionEvent.TOOL_TYPE_MOUSE) continue;
            // Mouse found
            mouseCursorIndex = i;
            break;
        }
        if(mouseCursorIndex == -1) return false; // we cant consoom that, theres no mice!
        if(CallbackBridge.isGrabbing()) {
            requestFocus();
            requestPointerCapture();

        }
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_HOVER_MOVE:
                CallbackBridge.mouseX = (event.getX(mouseCursorIndex) * scaleFactor);
                CallbackBridge.mouseY = (event.getY(mouseCursorIndex) * scaleFactor);
                CallbackBridge.sendCursorPos(CallbackBridge.mouseX, CallbackBridge.mouseY);
                //debugText.setText(CallbackBridge.DEBUG_STRING.toString());
                CallbackBridge.DEBUG_STRING.setLength(0);
                return true;
            case MotionEvent.ACTION_SCROLL:
                CallbackBridge.sendScroll((double) event.getAxisValue(MotionEvent.AXIS_VSCROLL), (double) event.getAxisValue(MotionEvent.AXIS_HSCROLL));
                return true;
            case MotionEvent.ACTION_BUTTON_PRESS:
                return sendMouseButtonUnconverted(event.getActionButton(),true);
            case MotionEvent.ACTION_BUTTON_RELEASE:
                return sendMouseButtonUnconverted(event.getActionButton(),false);
            default:
                return false;
        }
    }

    //TODO MOVE THIS SOMEWHERE ELSE
    private boolean debugErrored = false;
    /** The input event for mouse with a captured pointer */
    @RequiresApi(26)
    @Override
    public boolean dispatchCapturedPointerEvent(MotionEvent e) {
        CallbackBridge.mouseX += (e.getX()*scaleFactor);
        CallbackBridge.mouseY += (e.getY()*scaleFactor);
        if(!CallbackBridge.isGrabbing()){
            releasePointerCapture();
            clearFocus();
        }

        if (pointerDebugText.getVisibility() == View.VISIBLE && !debugErrored) {
            StringBuilder builder = new StringBuilder();
            try {
                builder.append("PointerCapture debug\n");
                builder.append("MotionEvent=").append(e.getActionMasked()).append("\n");
                builder.append("PressingBtn=").append(MotionEvent.class.getDeclaredMethod("buttonStateToString").invoke(null, e.getButtonState())).append("\n\n");

                builder.append("PointerX=").append(e.getX()).append("\n");
                builder.append("PointerY=").append(e.getY()).append("\n");
                builder.append("RawX=").append(e.getRawX()).append("\n");
                builder.append("RawY=").append(e.getRawY()).append("\n\n");

                builder.append("XPos=").append(CallbackBridge.mouseX).append("\n");
                builder.append("YPos=").append(CallbackBridge.mouseY).append("\n\n");
                builder.append("MovingX=").append(getMoving(e.getX(), true)).append("\n");
                builder.append("MovingY=").append(getMoving(e.getY(), false)).append("\n");
            } catch (Throwable th) {
                debugErrored = true;
                builder.append("Error getting debug. The debug will be stopped!\n").append(Log.getStackTraceString(th));
            } finally {
                pointerDebugText.setText(builder.toString());
                builder.setLength(0);
            }
        }

        pointerDebugText.setText(CallbackBridge.DEBUG_STRING.toString());
        CallbackBridge.DEBUG_STRING.setLength(0);
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                CallbackBridge.sendCursorPos(CallbackBridge.mouseX, CallbackBridge.mouseY);
                return true;
            case MotionEvent.ACTION_BUTTON_PRESS:
                return sendMouseButtonUnconverted(e.getActionButton(), true);
            case MotionEvent.ACTION_BUTTON_RELEASE:
                return sendMouseButtonUnconverted(e.getActionButton(), false);
            case MotionEvent.ACTION_SCROLL:
                CallbackBridge.sendScroll(e.getAxisValue(MotionEvent.AXIS_HSCROLL), e.getAxisValue(MotionEvent.AXIS_VSCROLL));
                return true;
            default:
                return false;
        }
    }

    /** Get the mouse direction as a string */
    private String getMoving(float pos, boolean xOrY) {
        if (pos == 0) return "STOPPED";
        if (pos > 0) return xOrY ? "RIGHT" : "DOWN";
        return xOrY ? "LEFT" : "UP";
    }

    /** Convert the mouse button, then send it
     * @return Whether the event was processed
     */
    public static boolean sendMouseButtonUnconverted(int button, boolean status) {
        int glfwButton = -256;
        switch (button) {
            case MotionEvent.BUTTON_PRIMARY:
                glfwButton = LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_LEFT;
                break;
            case MotionEvent.BUTTON_TERTIARY:
                glfwButton = LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_MIDDLE;
                break;
            case MotionEvent.BUTTON_SECONDARY:
                glfwButton = LWJGLGLFWKeycode.GLFW_MOUSE_BUTTON_RIGHT;
                break;
        }
        if(glfwButton == -256) return false;
        sendMouseButton(glfwButton, status);
        return true;
    }

    /** Return the size, given the UI scale size */
    private int mcscale(int input) {
        return (int)((GUIScale * input)/scaleFactor);
    }

    /** A small interface called when the listener is ready for the first time */
    public interface SurfaceReadyListener {
        void isReady();
    }

    public void setSurfaceReadyListener(SurfaceReadyListener listener){
        surfaceReadyListener = listener;
    }
}
