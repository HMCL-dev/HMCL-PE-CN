package net.kdt.pojavlaunch;

import static net.kdt.pojavlaunch.Tools.currentDisplayMetrics;
import static org.lwjgl.glfw.CallbackBridge.windowHeight;
import static org.lwjgl.glfw.CallbackBridge.windowWidth;

import net.kdt.pojavlaunch.utils.JREUtils;

import java.util.Vector;

public class BaseMainActivity extends BaseActivity {

    public float scaleFactor = 1;

    private MinecraftGLView minecraftGLView;

    protected void initLayout(String javaPath,String home,boolean highVersion,final Vector<String> args, String renderer) {
        setContentView(R.layout.activity_pojav);

        try {
            windowWidth = Tools.getDisplayFriendlyRes(currentDisplayMetrics.widthPixels, scaleFactor);
            windowHeight = Tools.getDisplayFriendlyRes(currentDisplayMetrics.heightPixels, scaleFactor);
            System.out.println("WidthHeight: " + windowWidth + ":" + windowHeight);

            this.minecraftGLView = findViewById(R.id.main_game_render_view);

            minecraftGLView.setSurfaceReadyListener(() -> {
                try {
                    runCraft(javaPath,home,highVersion,args,renderer);
                }catch (Throwable e){

                }
            });

            minecraftGLView.start();

        } catch (Throwable e) {

        }
    }

    public static void fullyExit() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void runCraft(String javaPath,String home,boolean highVersion,final Vector<String> args, String renderer) throws Throwable {
        
        JREUtils.jreReleaseList = JREUtils.readJREReleaseProperties(javaPath);

        JREUtils.redirectAndPrintJRELog(this);
        Tools.launchMinecraft(this, javaPath,home,renderer, args);
    }

}
