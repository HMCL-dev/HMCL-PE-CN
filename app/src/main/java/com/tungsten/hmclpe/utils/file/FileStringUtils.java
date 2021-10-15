package com.tungsten.hmclpe.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileStringUtils {
    public static String getStringFromFile(String path){
        try {
            FileInputStream inputStream = new FileInputStream(path);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            String string = new String(bytes);
            return string;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static void writeFile(String path,String string){
        try {
            FileUtils.createFile(path);
            FileWriter fileWriter = new FileWriter(new File(path));
            fileWriter.write(string);
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
