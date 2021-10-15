package com.tungsten.hmclpe.utils.file;

import android.app.Activity;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static void createDirectory(String path){
        if (!new File(path).exists()){
            new File(path).mkdirs();
        }
    }

    public static void createFile(String path) throws IOException {
        if (!new File(path).exists()){
            new File(path).createNewFile();
        }
    }

    public static void rename(String path,String newName){
        File file = new File(path);
        String newPath = path.substring(0,path.lastIndexOf("/") + 1) + newName;
        File newFile = new File(newPath);
        file.renameTo(newFile);
    }

    public static boolean copyFile(String srcPath,String destPath){
        File src = new File(srcPath);
        File dest = new File(destPath);
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(src));
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] flush = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(flush)) != -1){
                outputStream.write(flush,0,len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void copyFileWithUri(Uri uri, String destPath, Activity activity) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        OutputStream outputStream = new FileOutputStream(new File(destPath));
        byte[] flush = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(flush)) != -1) {
            outputStream.write(flush, 0, len);
        }
    }

    public static boolean deleteDirectory(String path){
        try{
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                return true;
            }
            if (dirFile.isFile()) {
                dirFile.delete();
                return true;
            }
            File[] files = dirFile.listFiles();
            if(files==null){
                return false;
            }
            for (int i = 0; i < files.length; i++) {
                deleteDirectory(files[i].toString());
            }
            dirFile.delete();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
