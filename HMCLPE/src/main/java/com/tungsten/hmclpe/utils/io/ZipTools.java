package com.tungsten.hmclpe.utils.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipTools {

    public static String readNormalMeta(String file,String targetName) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        ZipFile zf = new ZipFile(file, charset);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (!ze.isDirectory() && ze.getName().equals(targetName)) {
                InputStream inputStream = zf.getInputStream(ze);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();
                zin.closeEntry();
                return new String(bytes);
            }
        }
        zin.closeEntry();

        return "";
    }

    public static InputStream getFileInputStream(String file,String targetName) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        ZipFile zf = new ZipFile(file, charset);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (!ze.isDirectory() && ze.getName().equals(targetName)) {
                InputStream inputStream = zf.getInputStream(ze);
                zin.closeEntry();
                return inputStream;
            }
        }
        zin.closeEntry();

        return null;
    }

    public static boolean isFileExist(String file,String targetName) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (!ze.isDirectory() && ze.getName().equals(targetName)) {
                return true;
            }
        }
        zin.closeEntry();
        return false;
    }

    public static void zip(String[] files,String destZip){

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destZip);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ZipOutputStream zos = new ZipOutputStream(bos);

        /*循环读取文件，压缩到zip中*/
        BufferedInputStream bis = null;
        FileInputStream fis = null;
        try {
            for (int i = 0; i < files.length; i++) {

                String file = files[i];

                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                /*获取文件名，创建条目并添加到zip中*/
                File f = new File(file);
                ZipEntry z1 = new ZipEntry(f.getName());
                zos.putNextEntry(z1);
                /*读取文件中的字节信息，压入条目*/
                int tmp = -1;
                while((tmp = bis.read()) != -1){
                    /*写入到目标zip中*/
                    zos.write(tmp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zip(String zipFileName,String inputFile) throws IOException{

        File file = new File(inputFile);

        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream out = new ZipOutputStream(fos);
        zipDir(out,file,"");
        System.out.println("ZIP done..");
        out.close();
    }

    private static void zipDir(ZipOutputStream out, File file, String base) throws IOException {

        System.out.println("Zipping："+file.getName());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            out.putNextEntry(new ZipEntry(base+"/"));
            base = base.length()==0?"":base+"/";
            for (int i = 0; i < files.length; i++) {
                /*进行递归*/
                zipDir(out,files[i], base+files[i].getName());
            }
        }else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fis = new FileInputStream(file);

            int len = 0;
            while((len = fis.read()) != -1){
                /*写入到目标zip中*/
                out.write(len);
            }
            fis.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static void unzipFile(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws IOException {
        if (isEmpty(zipFilePath) || isEmpty(unzipFilePath)) {
            throw new IOException("PARAMETER_IS_NULL");
        }
        File zipFile = new File(zipFilePath);
        if (includeZipFileName) {
            String fileName = zipFile.getName();
            if (isNotEmpty(fileName)) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            unzipFilePath = unzipFilePath + File.separator + fileName;
        }
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
            unzipFileDir.mkdirs();
        }
        ZipEntry entry = null;
        String entryFilePath = null, entryDirPath = null;
        File entryFile = null, entryDir = null;
        int index = 0, count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            entryFilePath = unzipFilePath + File.separator + entry.getName();
            index = Math.max(entryFilePath.lastIndexOf(File.separator), entryFilePath.lastIndexOf("/"));
            if (index != -1) {
                entryDirPath = entryFilePath.substring(0, index);
            } else {
                entryDirPath = "";
            }
            entryDir = new File(entryDirPath);
            if (!entryDir.exists() || !entryDir.isDirectory()) {
                entryDir.mkdirs();
            }
            entryFile = new File(entryFilePath);
            if (entryFile.exists()) {
                entryFile.delete();
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                bis = new BufferedInputStream(zip.getInputStream(entry));
                while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                    bos.write(buffer, 0, count);
                }
                bos.flush();
                bos.close();
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

}
