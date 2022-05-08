package com.tungsten.hmclpe.launcher.download;

import android.os.Handler;

import com.leo618.zip.IZipCallback;
import com.leo618.zip.ZipManager;
import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.utils.file.FileUtils;

import java.io.File;

public class InstallerAnalyzer {

    public static void checkType(String path,CheckInstallerTypeCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            if (FileUtils.copyFile(path, AppManifest.INSTALL_DIR + "/local/installer.jar")) {
                handler.post(() -> {
                    ZipManager.unzip(AppManifest.INSTALL_DIR + "/local/installer.jar", AppManifest.INSTALL_DIR + "/local/installer", new IZipCallback() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onProgress(int percentDone) {

                        }

                        @Override
                        public void onFinish(boolean success) {
                            if (success) {
                                String root = AppManifest.INSTALL_DIR + "/local/installer/";
                                if (new File(root + "install_profile.json").exists()) {
                                    callback.onFinish(Type.FORGE);
                                }
                                else if (new File(root + "Config.class").exists() || new File(root + "net/optifine/Config.class").exists() || new File(root + "notch/net/optifine/Config.class").exists()) {
                                    callback.onFinish(Type.OPTIFINE);
                                }
                                else {
                                    callback.onFinish(Type.UNKNOWN);
                                }
                            }
                            else {
                                handler.post(() -> {
                                    callback.onFinish(Type.UNKNOWN);
                                });
                            }
                        }
                    });
                });
            }
            else {
                handler.post(() -> {
                    callback.onFinish(Type.UNKNOWN);
                });
            }
        }).start();
    }

    public interface CheckInstallerTypeCallback{
        void onStart();
        void onFinish(Type type);
    }

    public enum Type{
        FORGE,
        OPTIFINE,
        UNKNOWN
    }
}
