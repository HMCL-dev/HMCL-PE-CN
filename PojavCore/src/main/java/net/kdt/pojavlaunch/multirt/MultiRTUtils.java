package net.kdt.pojavlaunch.multirt;

import android.content.Context;

import net.kdt.pojavlaunch.Tools;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MultiRTUtils {
    public static HashMap<String,Runtime> cache = new HashMap<>();
    public static class Runtime {
        public Runtime(String name) {
            this.name = name;
        }
        public String name;
        public String versionString;
        public String arch;
        public int javaVersion;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Runtime runtime = (Runtime) o;
            return name.equals(runtime.name);
        }
        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
    private static final File runtimeFolder = new File(Tools.MULTIRT_HOME);
    private static final String JAVA_VERSION_str = "JAVA_VERSION=\"";
    private static final String OS_ARCH_str = "OS_ARCH=\"";
    public static List<Runtime> getRuntimes() {
        if(!runtimeFolder.exists()) runtimeFolder.mkdirs();
        ArrayList<Runtime> ret = new ArrayList<>();
        System.out.println("Fetch runtime list");
        for(File f : runtimeFolder.listFiles()) {
            ret.add(read(f.getName()));
        }

        return ret;
    }
    public static void postPrepare(Context ctx, String name) throws IOException {
        File dest = new File(runtimeFolder,"/"+name);
        if(!dest.exists()) return;
        Runtime r = read(name);
        String libFolder = "lib";
        if(new File(dest,libFolder+"/"+r.arch).exists()) libFolder = libFolder+"/"+r.arch;
        File ftIn = new File(dest, libFolder+ "/libfreetype.so.6");
        File ftOut = new File(dest, libFolder + "/libfreetype.so");
        if (ftIn.exists() && (!ftOut.exists() || ftIn.length() != ftOut.length())) {
            ftIn.renameTo(ftOut);
        }

        // Refresh libraries
        copyDummyNativeLib(ctx,"libawt_xawt.so",dest,libFolder);
    }
    private static void copyDummyNativeLib(Context ctx, String name, File dest, String libFolder) throws IOException {

        File fileLib = new File(dest, "/"+libFolder + "/" + name);
        fileLib.delete();
        FileInputStream is = new FileInputStream(new File(ctx.getApplicationInfo().nativeLibraryDir, name));
        FileOutputStream os = new FileOutputStream(fileLib);
        IOUtils.copy(is, os);
        is.close();
        os.close();
    }
    public static Runtime read(String name) {
        if(cache.containsKey(name)) return cache.get(name);
        Runtime retur;
        File release = new File(runtimeFolder,"/"+name+"/release");
        if(!release.exists()) {
            return new Runtime(name);
        }
        try {
            String content = Tools.read(release.getAbsolutePath());
            int _JAVA_VERSION_index = content.indexOf(JAVA_VERSION_str);
            int _OS_ARCH_index = content.indexOf(OS_ARCH_str);
            if(_JAVA_VERSION_index != -1 && _OS_ARCH_index != -1) {
                _JAVA_VERSION_index += JAVA_VERSION_str.length();
                _OS_ARCH_index += OS_ARCH_str.length();
                String javaVersion = content.substring(_JAVA_VERSION_index,content.indexOf('"',_JAVA_VERSION_index));
                    String[] javaVersionSplit = javaVersion.split("\\.");
                    int javaVersionInt;
                    if (javaVersionSplit[0].equals("1")) {
                        javaVersionInt = Integer.parseInt(javaVersionSplit[1]);
                    } else {
                        javaVersionInt = Integer.parseInt(javaVersionSplit[0]);
                    }
                    Runtime r = new Runtime(name);
                    r.arch = content.substring(_OS_ARCH_index,content.indexOf('"',_OS_ARCH_index));
                    r.javaVersion = javaVersionInt;
                    r.versionString = javaVersion;
                    retur = r;
            }else{
                retur =  new Runtime(name);
            }
        }catch(IOException e) {
            retur =  new Runtime(name);
        }
        cache.put(name,retur);
        return retur;
    }
}
