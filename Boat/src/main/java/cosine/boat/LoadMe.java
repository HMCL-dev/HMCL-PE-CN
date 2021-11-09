package cosine.boat;

import java.io.File;
import java.util.Vector;

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

    public static int exec(LauncherConfig config) {
        try {

            MinecraftVersion mcVersion = MinecraftVersion.fromDirectory(new File(config.get("currentVersion")));
            String runtimePath = config.get("runtimePath");

			String arch = "aarch64";
			String vm_variant = "server";

			String token =config.get("auth_access_token");

            //String libraryPath = runtimePath + "/j2re-image/lib/" + arch + "/jli:" + runtimePath + "/j2re-image/lib/" + arch + ":" + runtimePath + "/lwjgl-3:" + runtimePath;

            //setLibraryPath(libraryPath);
			patchLinker();

			String pdir = LauncherConfig.privateDir();

			//String ppdir = LauncherConfig.ppdir();

			String home = config.get("home");

            String strd = config.get("currentVersion");

			if (pdir.equals("false")) {
				setenv("HOME", home);
			} else {
				setenv("HOME", strd);
			}

			//setenv("HOME", home);

            setenv("JAVA_HOME" , runtimePath + "/j2re-image");
			setenv("LIBGL_MIPMAP", "3");
			setenv("LIBGL_NORMALIZE", "1");

            // openjdk
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libfreetype.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libpng16.so.16");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libfontmanager.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libpng16.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/jli/libjli.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/" + vm_variant + "/libjvm.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libverify.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libjava.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libnet.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libnio.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libawt.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libawt_headless.so");

			boolean isLwjgl3=false;
            if (mcVersion.minimumLauncherVersion >= 21) {
                isLwjgl3 = true;
            }

			String libraryPath;
			String classPath;

            // others
			dlopen(runtimePath + "/libopenal.so.1");
			String gl = LauncherConfig.loadgl();
			if (gl.equals("libGL112.so.1")){
				dlopen(runtimePath + "/libGL112.so.1");
			} else {
				dlopen(runtimePath + "/libGL115.so.1");
			}

			if (!isLwjgl3) {
				libraryPath = runtimePath + "/j2re-image/lib/" + arch + "/jli:" + runtimePath + "/j2re-image/lib/" + arch + ":" + runtimePath + "/lwjgl-2:" + runtimePath;
				classPath = config.get("runtimePath") + "/lwjgl-2/lwjgl.jar:" + config.get("runtimePath") + "/lwjgl-2/lwjgl_util.jar:" + mcVersion.getClassPath(config);
				dlopen(runtimePath + "/lwjgl-2/liblwjgl64.so");
				setLibraryPath(libraryPath);
			} else {
				libraryPath = runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-3:" + runtimePath;
				classPath = runtimePath + "/lwjgl-3/lwjgl-jemalloc.jar:" + runtimePath + "/lwjgl-3/lwjgl-tinyfd.jar:" + runtimePath + "/lwjgl-3/lwjgl-opengl.jar:" + runtimePath + "/lwjgl-3/lwjgl-openal.jar:" + runtimePath + "/lwjgl-3/lwjgl-glfw.jar:" + runtimePath + "/lwjgl-3/lwjgl-stb.jar:" + runtimePath + "/lwjgl-3/lwjgl.jar:" +  mcVersion.getClassPath(config);
				dlopen(runtimePath + "/libglfw.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl_stb.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl_tinyfd.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl_opengl.so");
				setLibraryPath(libraryPath);
            }

            setupJLI();	

            redirectStdio(home + "/client_output.txt");

            /*

			if (pdir.equals("public")) {
				if (ppdir.equals("public")) {
					chdir(home);
				} else {
					chdir(strd);
				}
			} else {
				chdir(strd);
			}

             */

			if (pdir.equals("false")) {
				chdir(home);
			} else {
				chdir(strd);
			}

            Vector<String> args = new Vector<String>();

            args.add(runtimePath +  "/j2re-image/bin/java");
            args.add("-cp");
            args.add(classPath);
            args.add("-Djava.library.path=" + libraryPath);

			args.add("-Dorg.lwjgl.util.Debug=true");
			args.add("-Dorg.lwjgl.util.DebugLoader=true");

			args.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
			args.add("-Dfml.ignorePatchDiscrepancies=true");
			//禁止显示加载窗口
			args.add("-Dfml.earlyprogresswindow=false");
			//添加tｍpdir以获得权限读取json
			args.add("-Djava.io.tmpdir=/data/data/com.koishi.launcher.h2o2/cache");


            String extraJavaFlags[] = config.get("extraJavaFlags").split(" ");

            for (String flag : extraJavaFlags) {
                args.add(flag);
                if (LauncherConfig.api().equals("Mojang")||LauncherConfig.api().equals("Microsoft")){

				} else {
                	args.add("-javaagent:/storage/emulated/0/games/com.koishi.launcher/h2o2/authlib/authlib-injector-1.1.39.jar="+LauncherConfig.api());
				}
            }

            args.add(mcVersion.mainClass);

			String minecraftArgs[]=null;
            if (isLwjgl3) {
                minecraftArgs = mcVersion.getMinecraftArguments(config, true);	
            } else {
                minecraftArgs = mcVersion.getMinecraftArguments(config, false);	
            }
            for (String flag : minecraftArgs) {
                args.add(flag);
            }

            args.add("Update20210321");
			args.add("--width");
            args.add(Integer.toString(BoatApplication.getCurrentActivity().getResources().getDisplayMetrics().widthPixels));
            args.add("--height");
            args.add(Integer.toString(BoatApplication.getCurrentActivity().getResources().getDisplayMetrics().heightPixels));

			if (mcVersion.minimumLauncherVersion >= 21) {
				//判断minimumLauncherVersion是否大于21，大于的是高版本，高版本需要用方法加载不同版本的loader，这个只是其中一个
				/*
				 args.add("--launchTarget");
				 args.add("fmlclient");
				 args.add("--fml.forgeVersion");
				 args.add("31.2.47");
				 args.add("--fml.mcVersion");
				 args.add("1.15.2");
				 args.add("--fml.forgeGroup");
				 args.add("net.minecraftforge");
				 args.add("--fml.mcpVersion");
				 args.add("20200515.085601");
				 */
				args.add("--tweakClass");
				args.add("optifine.OptiFineTweaker");
			}
			//判断minimumLauncherVersion是否在14-21之间以及size的值，1.7.10forge的size为72996，需要单独加载cpw.mods.xx
			if (mcVersion.minimumLauncherVersion >= 14 && mcVersion.minimumLauncherVersion < 21 && mcVersion.assetIndex.size != 72996) {
				// 1.8-1.12.2
				args.add("--tweakClass");
				if (mcVersion.id.indexOf("OptiFine") != -1) {
					args.add("optifine.OptiFineTweaker");
				} else {
					args.add("net.minecraftforge.fml.common.launcher.FMLTweaker");
				}
			}

			if (mcVersion.minimumLauncherVersion >= 14 && mcVersion.minimumLauncherVersion < 21 && mcVersion.assetIndex.size == 72996) {
				// Below 1.7.10
				args.add("--tweakClass");
				if (mcVersion.id.indexOf("OptiFine") != -1) {
					args.add("optifine.OptiFineTweaker");
				} else {
					args.add("cpw.mods.fml.common.launcher.FMLTweaker");
				}

            }
			if (token.equals("0000")) {
				//args.add("--demo");
			}

			String extraMinecraftArgs[] = config.get("extraMinecraftFlags").split(" ");
			for (String flag : extraMinecraftArgs) {
				args.add(flag);
			}

			String finalArgs[] = new String[args.size()];
			for (int i = 0; i < args.size(); i++) {

				finalArgs[i] = args.get(i);
				System.out.println(finalArgs[i]);
			}

            System.out.println("OpenJDK exited with code : " + jliLaunch(finalArgs));
			/*
			 File so_map = new File("/proc/self/maps");
			 BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(so_map)));

			 File map_log= new File("/sdcard/boat/boat_mem_maps.txt");
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(map_log)));
			 String line;
			 while((line = br.readLine()) != null){
			 bw.write(line + "\n");

			 }
			 */

        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
		return 0;
    }

}
