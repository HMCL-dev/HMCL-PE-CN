package cosine.boat;

import java.util.*;

public class LoadMe {

    public static native int chdir(String str);
    public static native int jliLaunch(String[] strArr);
    public static native void redirectStdio(String file);
    public static native void setenv(String str, String str2);
	public static native void setupJLI();
	public static native int dlopen(String name);
    public static native void setLibraryPath(String path);
	public static native void patchLinker();

    static {
        System.loadLibrary("boat");
    }

    public static int launchMinecraft(String runtimePath, String home, boolean highVersion, Vector<String> args) {

		patchLinker();

        try {

			setenv("HOME", home);
			setenv("JAVA_HOME" , runtimePath + "/j2re-image");
			setenv("LIBGL_MIPMAP","3");
			setenv("LIBGL_NORMALIZE","1");
			setenv("LIBGL_GL","21");

            // openjdk
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libpng16.so.16");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libpng16.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libfreetype.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libinstrument.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libfontmanager.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/jli/libjli.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/server/libjvm.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/libverify.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/libjava.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/libnet.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/libnio.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/libawt.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch64/libawt_headless.so");
            dlopen(runtimePath + "/libopenal.so.1");
            dlopen(runtimePath + "/libGL.so.1");

            String libraryPath;

            if (!highVersion) {
                libraryPath = runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-2:" + runtimePath;
                dlopen(runtimePath + "/lwjgl-2/liblwjgl.so");
            }
            else {
                libraryPath = runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-3:" + runtimePath;
                dlopen(runtimePath + "/libglfw.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_stb.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_tinyfd.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_opengl.so");
            }

            setLibraryPath(libraryPath);
			setupJLI();

            redirectStdio(home + "/boat_output.json");
            chdir(home);

			String finalArgs[] = new String[args.size()];
			for (int i = 0; i < args.size(); i++) {
                if (!args.get(i).equals(" ")) {
                    finalArgs[i] = args.get(i);
                    System.out.println("Minecraft Args:" + finalArgs[i]);
                }
			}
            System.out.println("OpenJDK exited with code : " + jliLaunch(finalArgs));
        } catch (Exception e) {
            e.printStackTrace();
			return 1;
        }
		return 0;
    }

    public static int launchJavaVM(String runtimePath,String home,boolean highVersion,Vector<String> args) {

        patchLinker();

        try {

            setenv("HOME", home);
            setenv("JAVA_HOME" , runtimePath + "/j2re-image");
            setenv("LIBGL_MIPMAP","3");
            setenv("LIBGL_NORMALIZE","1");
            setenv("LIBGL_GL","21");

            // openjdk
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libpng16.so.16");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libpng16.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libfreetype.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libinstrument.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libfontmanager.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/jli/libjli.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/server/libjvm.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libverify.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libjava.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libnet.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libnio.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libawt.so");
            dlopen(runtimePath + "/j2re-image/lib/aarch64/libawt_headless.so");
            dlopen(runtimePath + "/libopenal.so.1");
            dlopen(runtimePath + "/libGL.so.1");

            String libraryPath;

            if (!highVersion) {
                libraryPath = runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-2:" + runtimePath;
                dlopen(runtimePath + "/lwjgl-2/liblwjgl.so");
            }
            else {
                libraryPath = runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-3:" + runtimePath;
                dlopen(runtimePath + "/libglfw.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_stb.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_tinyfd.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_opengl.so");
            }

            setLibraryPath(libraryPath);
            setupJLI();

            redirectStdio(home + "/boat_output.json");
            chdir(home);

            String finalArgs[] = new String[args.size()];
            for (int i = 0; i < args.size(); i++) {
                if (!args.get(i).equals(" ")) {
                    finalArgs[i] = args.get(i);
                    System.out.println("Minecraft Args:" + finalArgs[i]);
                }
            }
            System.out.println("OpenJDK exited with code : " + jliLaunch(finalArgs));
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

}





