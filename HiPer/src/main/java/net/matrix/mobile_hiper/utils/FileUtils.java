package net.matrix.mobile_hiper.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void createDirectory(String path) {
        if (!new File(path).exists()){
            new File(path).mkdirs();
        }
    }

    public static void createFile(String path) throws IOException {
        if (!new File(path).exists()){
            new File(path).createNewFile();
        }
    }

}
