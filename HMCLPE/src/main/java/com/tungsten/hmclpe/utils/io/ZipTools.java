package com.tungsten.hmclpe.utils.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    public static InputStream readForgeNewMeta(String file) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        ZipFile zf = new ZipFile(file, charset);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (!ze.isDirectory() && ze.getName().equals("META-INF/MANIFEST.MF")) {
                InputStream inputStream = zf.getInputStream(ze);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();
                zin.closeEntry();
                return inputStream;
            }
        }
        zin.closeEntry();

        return null;
    }

    public static boolean isFileExist(String file,String targetName) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        ZipFile zf = new ZipFile(file, charset);
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

}
