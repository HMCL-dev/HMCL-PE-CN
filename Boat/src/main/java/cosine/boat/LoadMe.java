package cosine.boat;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.*;

public class LoadMe {

    @SuppressLint("SdCardPath")
    public static String BOAT_LIB_DIR;

    public static native int chdir(String path);
    public static native void redirectStdio(String file);
    public static native void setenv(String name, String value);
    public static native int dlopen(String name);
    public static native void patchLinker();
    public static native int dlexec(String[] args);

    static {
        System.loadLibrary("loadme");
    }

    public static int launchMinecraft(Context context,String javaPath, String home, boolean highVersion, Vector<String> args, String renderer) {

        BOAT_LIB_DIR = context.getDir("runtime",0).getAbsolutePath() + "/boat";

        boolean isJava17 = javaPath.endsWith("JRE17");

		patchLinker();

        try {

			setenv("HOME", home);
			setenv("JAVA_HOME" , javaPath);
			setenv("LIBGL_MIPMAP","3");
			setenv("LIBGL_NORMALIZE","1");
			if (highVersion) {
                setenv("LIBGL_GL","32");
            }
			setenv("LIBGL_VSYNC","1");
			setenv("MESA_LOADER_DRIVER_OVERRIDE","virtio_gpu");
            setenv("LIBGL_DRIVERS_PATH",BOAT_LIB_DIR + "/renderer/virgl/");
//            setenv("REGAL_GL_VENDOR", "Android");
//            setenv("REGAL_GL_RENDERER", "Regal");
//            setenv("REGAL_GL_VERSION", "4.5");
//            setenv("force_glsl_extensions_warn", "true");
//            setenv("allow_higher_compat_version", "true");
//            setenv("allow_glsl_extension_directive_midshader", "true");
////            setenv("MESA_LOADER_DRIVER_OVERRIDE", "zink");
            setenv("VIRGL_VTEST_SOCKET_NAME", BOAT_LIB_DIR + "/renderer/virgl/virgl_test_server");
            setenv("GALLIUM_DRIVER","virpipe");
            // openjdk
            if (isJava17) {
//                setenv("LIBGL_ES","3");
//                setenv("LIBGL_SHADERCONVERTER", "1");

                dlopen(javaPath + "/lib/libpng16.so.16");
                dlopen(javaPath + "/lib/libpng16.so");
                dlopen(javaPath + "/lib/libfreetype.so");
                dlopen(javaPath + "/lib/libjli.so");
                dlopen(javaPath + "/lib/server/libjvm.so");
                dlopen(javaPath + "/lib/libverify.so");
                dlopen(javaPath + "/lib/libjava.so");
                dlopen(javaPath + "/lib/libnet.so");
                dlopen(javaPath + "/lib/libnio.so");
                dlopen(javaPath + "/lib/libawt.so");
                dlopen(javaPath + "/lib/libawt_headless.so");
                dlopen(javaPath + "/lib/libinstrument.so");
                dlopen(javaPath + "/lib/libfontmanager.so");

                dlopen(BOAT_LIB_DIR + "/libglslang.so.11");
                dlopen(BOAT_LIB_DIR + "/libglslconv.so");
            }
            else {
                dlopen(javaPath + "/lib/aarch64/libpng16.so.16");
                dlopen(javaPath + "/lib/aarch64/libpng16.so");
                dlopen(javaPath + "/lib/aarch64/libfreetype.so");
                dlopen(javaPath + "/lib/aarch64/jli/libjli.so");
                dlopen(javaPath + "/lib/aarch64/server/libjvm.so");
                dlopen(javaPath + "/lib/aarch64/libverify.so");
                dlopen(javaPath + "/lib/aarch64/libjava.so");
                dlopen(javaPath + "/lib/aarch64/libnet.so");
                dlopen(javaPath + "/lib/aarch64/libnio.so");
                dlopen(javaPath + "/lib/aarch64/libawt.so");
                dlopen(javaPath + "/lib/aarch64/libawt_headless.so");
                dlopen(javaPath + "/lib/aarch64/libinstrument.so");
                dlopen(javaPath + "/lib/aarch64/libfontmanager.so");
            }
            dlopen(BOAT_LIB_DIR + "/libopenal.so.1");
//            dlopen(BOAT_LIB_DIR + "/renderer/libGL112.so.1");
//            dlopen(BOAT_LIB_DIR + "/libEGL.so.1");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/libexpat.so.1");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/libepoxy.so.0");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/libglapi.so.0");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/libGLESv2.so.2");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/libGLESv1_CM.so.1");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/swrast_dri.so");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/libvirglrenderer.so");
            dlopen(BOAT_LIB_DIR + "/renderer/libGL.so.1");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/libEGL.so.1");
            dlopen(BOAT_LIB_DIR + "/renderer/virgl/virgl_test_server");


            if (!highVersion) {
                dlopen(BOAT_LIB_DIR + "/lwjgl-2/liblwjgl.so");
            }
            else {
                dlopen(BOAT_LIB_DIR + "/libglfw.so");
                dlopen(BOAT_LIB_DIR + "/lwjgl-3/liblwjgl.so");
                dlopen(BOAT_LIB_DIR + "/lwjgl-3/liblwjgl_stb.so");
                dlopen(BOAT_LIB_DIR + "/lwjgl-3/liblwjgl_tinyfd.so");
                dlopen(BOAT_LIB_DIR + "/lwjgl-3/liblwjgl_opengl.so");
            }

            redirectStdio(home + "/boat_latest_log.txt");
            chdir(home);

			String finalArgs[] = new String[args.size()];
			for (int i = 0; i < args.size(); i++) {
                if (!args.get(i).equals(" ")) {
                    finalArgs[i] = args.get(i);
                    System.out.println("Minecraft Args:" + finalArgs[i]);
                }
			}
            System.out.println("OpenJDK exited with code : " + dlexec(finalArgs));
        }
        catch (Exception e) {
            e.printStackTrace();
			return 1;
        }
		return 0;
    }

    public static int launchJVM (String javaPath,Vector<String> args,String home) {

        patchLinker();

        try {
            setenv("HOME", home);
            setenv("JAVA_HOME" , javaPath);

            dlopen(javaPath + "/lib/aarch64/libfreetype.so");
            dlopen(javaPath + "/lib/aarch64/jli/libjli.so");
            dlopen(javaPath + "/lib/aarch64/server/libjvm.so");
            dlopen(javaPath + "/lib/aarch64/libverify.so");
            dlopen(javaPath + "/lib/aarch64/libjava.so");
            dlopen(javaPath + "/lib/aarch64/libnet.so");
            dlopen(javaPath + "/lib/aarch64/libnio.so");
            dlopen(javaPath + "/lib/aarch64/libawt.so");
            dlopen(javaPath + "/lib/aarch64/libawt_headless.so");
            dlopen(javaPath + "/lib/aarch64/libfontmanager.so");

            String libraryPath = javaPath + "/lib/jli:" + javaPath + "/lib/aarch64";

            redirectStdio(home + "/boat_api_installer_log.txt");
            chdir(home);

            String finalArgs[] = new String[args.size()];
            for (int i = 0; i < args.size(); i++) {
                if (!args.get(i).equals(" ")) {
                    finalArgs[i] = args.get(i);
                    System.out.println("JVM Args:" + finalArgs[i]);
                }
            }
            System.out.println("OpenJDK exited with code : " + dlexec(finalArgs));
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

}





