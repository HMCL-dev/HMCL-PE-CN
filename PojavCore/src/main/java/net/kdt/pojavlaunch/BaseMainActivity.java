package net.kdt.pojavlaunch;

import static net.kdt.pojavlaunch.Architecture.ARCH_X86;
import static org.lwjgl.glfw.CallbackBridge.windowHeight;
import static org.lwjgl.glfw.CallbackBridge.windowWidth;

import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.os.*;
import android.view.*;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import net.kdt.pojavlaunch.prefs.*;
import net.kdt.pojavlaunch.utils.*;
import org.lwjgl.glfw.*;

public class BaseMainActivity extends LoggableActivity {
    public static volatile ClipboardManager GLOBAL_CLIPBOARD;

    volatile public static boolean isInputStackCall;

    public float scaleFactor = 1;

    private MinecraftGLView minecraftGLView;
    private int guiScale;

    private File logFile;
    private PrintStream logStream;

    private GameSetting gameSetting;

    // @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initActivity(int resId,GameSetting gameSetting) {
        setContentView(resId);

        this.gameSetting = gameSetting;

        try {
            logFile = new File(gameSetting.gameFileDirectory, "latestlog.txt");
            logFile.delete();
            logFile.createNewFile();
            logStream = new PrintStream(logFile.getAbsolutePath());

            this.minecraftGLView = findViewById(R.id.main_game_render_view);

            minecraftGLView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener(){
                
                    private boolean isCalled = false;
                    @Override
                    public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
                        scaleFactor = (LauncherPreferences.DEFAULT_PREF.getInt("resolutionRatio",100)/100f);
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
                        if (!isCalled) {
                            isCalled = true;
                            
                            JREUtils.setupBridgeWindow(new Surface(texture));
                            
                            new Thread(() -> {
                                try {
                                    Thread.sleep(200);
                                    runCraft();
                                } catch (Throwable e) {

                                }
                            }, "JVM Main thread").start();
                        }
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
        } catch (Throwable e) {

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        if (CallbackBridge.isGrabbing()){
            sendKeyPress(LWJGLGLFWKeycode.GLFW_KEY_ESCAPE);
        }
        super.onPause();
    }

    public static void fullyExit() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean isAndroid8OrHigher() {
        return Build.VERSION.SDK_INT >= 26; 
    }

    private void runCraft() throws Throwable {
        if (gameSetting.renderer.equals("ZINK")){
            checkVulkanZinkIsSupported();
        }
        checkLWJGL3Installed();
        jreReleaseList = JREUtils.readJREReleaseProperties(gameSetting.runtimePath);
        JREUtils.checkJavaArchitecture(this, jreReleaseList.get("OS_ARCH"));
        //checkJavaArgsIsLaunchable(jreReleaseList.get("JAVA_VERSION"));
        JREUtils.redirectAndPrintJRELog(this);
        Tools.launchMinecraft(this, gameSetting);
    }

    public static String fromArray(List<String> arr) {
        StringBuilder s = new StringBuilder();
        for (String exec : arr) {
            s.append(" ").append(exec);
        }
        return s.toString();
    }

    private void checkJavaArgsIsLaunchable(String jreVersion) throws Throwable {
        appendlnToLog("Info: Custom Java arguments: \"" + LauncherPreferences.PREF_CUSTOM_JAVA_ARGS + "\"");
    }

    private void checkLWJGL3Installed() {
        File lwjgl3dir = new File(gameSetting.runtimePath, "lwjgl3");
        if (!lwjgl3dir.exists() || lwjgl3dir.isFile() || lwjgl3dir.list().length == 0) {
            appendlnToLog("Error: LWJGL3 was not installed!");
            throw new RuntimeException("");
        } else {
            appendlnToLog("Info: LWJGL3 directory: " + Arrays.toString(lwjgl3dir.list()));
        }
    }

    private void checkVulkanZinkIsSupported() {
        if (Tools.DEVICE_ARCHITECTURE == ARCH_X86
                || Build.VERSION.SDK_INT < 25
                || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_LEVEL)
                || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_VERSION)) {
            appendlnToLog("Error: Vulkan Zink renderer is not supported!");
            throw new RuntimeException("");
        }
    }

    @Override
    public void onBackPressed() {
        // Prevent back
        // Catch back as Esc keycode at another place
        sendKeyPress(LWJGLGLFWKeycode.GLFW_KEY_ESCAPE);
    }

    public static void sendKeyPress(int keyCode, int modifiers, boolean status) {
        sendKeyPress(keyCode, 0, modifiers, status);
    }

    public static void sendKeyPress(int keyCode, int scancode, int modifiers, boolean status) {
        sendKeyPress(keyCode, '\u0000', scancode, modifiers, status);
    }

    public static void sendKeyPress(int keyCode, char keyChar, int scancode, int modifiers, boolean status) {
        CallbackBridge.sendKeycode(keyCode, keyChar, scancode, modifiers, status);
    }

    public static void sendKeyPress(int keyCode) {
        sendKeyPress(keyCode, CallbackBridge.getCurrentMods(), true);
        sendKeyPress(keyCode, CallbackBridge.getCurrentMods(), false);
    }
    
    public static void sendMouseButton(int button, boolean status) {
        CallbackBridge.sendMouseKeycode(button, CallbackBridge.getCurrentMods(), status);
    }

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

    public int getMcScale() {
        //Get the scale stored in game files, used auto scale if found or if the stored scaled is bigger than the authorized size.
        MCOptionUtils.load();
        String str = MCOptionUtils.get("guiScale");
        this.guiScale = (str == null ? 0 :Integer.parseInt(str));
        

        int scale = Math.max(Math.min(windowWidth / 320, windowHeight / 240), 1);
        if(scale < this.guiScale || guiScale == 0){
            this.guiScale = scale;
        }
        return this.guiScale;
    }

    @Override
    public void appendToLog(String text, boolean checkAllow) {
        logStream.print(text);
    }
}
