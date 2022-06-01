package com.tungsten.hmclpe.utils.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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

    @SuppressWarnings("unchecked")
    public static void unzipFile(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {
        if (isEmpty(zipFilePath) || isEmpty(unzipFilePath)) {
            throw new Exception("PARAMETER_IS_NULL");
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
