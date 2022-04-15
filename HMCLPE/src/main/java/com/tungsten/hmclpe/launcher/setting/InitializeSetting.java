package com.tungsten.hmclpe.launcher.setting;

import static com.tungsten.hmclpe.update.UpdateChecker.UPDATE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.leo618.zip.IZipCallback;
import com.leo618.zip.ZipManager;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.list.info.contents.ContentListBean;
import com.tungsten.hmclpe.launcher.list.install.DownloadTaskListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.manifest.info.AppInfo;
import com.tungsten.hmclpe.launcher.setting.game.child.BoatLauncherSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.GameDirSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.JavaSetting;
import com.tungsten.hmclpe.launcher.setting.game.child.PojavLauncherSetting;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.setting.game.child.RamSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.child.BackgroundSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.child.SourceSetting;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.task.LanzouUrlGetTask;
import com.tungsten.hmclpe.update.LauncherVersion;
import com.tungsten.hmclpe.update.UpdateJSON;
import com.tungsten.hmclpe.utils.file.AssetsUtils;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.file.FileUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;
import com.tungsten.hmclpe.utils.io.DownloadUtil;
import com.tungsten.hmclpe.utils.io.NetworkUtils;
import com.tungsten.hmclpe.utils.platform.MemoryUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class InitializeSetting {

    public static void checkLauncherFiles(MainActivity activity){
        /*
         *检查forge-install-bootstrapper.jar
         */
        activity.runOnUiThread(() -> {
            activity.loadingText.setText(activity.getString(R.string.loading_hint_plugin));
        });
        if (!new File(AppManifest.PLUGIN_DIR + "/installer/forge-install-bootstrapper.jar").exists()) {
            AssetsUtils.getInstance(activity).copyOnMainThread("plugin",AppManifest.PLUGIN_DIR);
        }
        /*
         *检查布局方案，如果没有，就生产一个默认布局
         */
        activity.runOnUiThread(() -> {
            activity.loadingText.setText(activity.getString(R.string.loading_hint_control));
        });
        if (SettingUtils.getControlPatternList().size() == 0) {
            AssetsUtils.getInstance(activity.getApplicationContext()).copyOnMainThread("control", AppManifest.CONTROLLER_DIR);
        }
        /*
         *检查除Java外的运行环境，这些文件一定会被内置进启动器，所以进行统一处理
         */
        activity.runOnUiThread(() -> {
            activity.loadingText.setText(activity.getString(R.string.loading_hint_lib));
        });
        if (!new File(AppManifest.DEFAULT_RUNTIME_DIR + "/version").exists() || Integer.parseInt(Objects.requireNonNull(FileStringUtils.getStringFromFile(AppManifest.DEFAULT_RUNTIME_DIR + "/version"))) < Integer.parseInt(Objects.requireNonNull(AssetsUtils.readAssetsTxt(activity, "app_runtime/version")))) {
            FileUtils.deleteDirectory(AppManifest.BOAT_LIB_DIR);
            FileUtils.deleteDirectory(AppManifest.POJAV_LIB_DIR);
            FileUtils.deleteDirectory(AppManifest.CACIOCAVALLO_DIR);
            if (new File(AppManifest.DEFAULT_RUNTIME_DIR + "/version").exists()) {
                new File(AppManifest.DEFAULT_RUNTIME_DIR + "/version").delete();
            }
            AssetsUtils.getInstance(activity).copyOnMainThread("app_runtime/boat",AppManifest.BOAT_LIB_DIR);
            AssetsUtils.getInstance(activity).copyOnMainThread("app_runtime/pojav",AppManifest.POJAV_LIB_DIR);
            AssetsUtils.getInstance(activity).copyOnMainThread("app_runtime/caciocavallo",AppManifest.CACIOCAVALLO_DIR);
            AssetsUtils.getInstance(activity).copyOnMainThread("app_runtime/version",AppManifest.DEFAULT_RUNTIME_DIR + "/version");
        }
        /*
         *检查Java运行时，Java可能内置在启动器也可能需要下载，因此单独处理
         */
        checkJava8(activity);
        checkJava17(activity);
    }

    public static void checkJava8(MainActivity activity){
        activity.runOnUiThread(() -> {
            activity.loadingText.setText(activity.getString(R.string.loading_hint_java_8));
        });
        if (!new File(AppManifest.JAVA_DIR + "/default").exists() || !new File(AppManifest.JAVA_DIR + "/default/version").exists() || Integer.parseInt(Objects.requireNonNull(FileStringUtils.getStringFromFile(AppManifest.JAVA_DIR + "/default/version"))) < Integer.parseInt(Objects.requireNonNull(AssetsUtils.readAssetsTxt(activity, "app_runtime/java/default/version")))) {
            FileUtils.deleteDirectory(AppManifest.JAVA_DIR + "/default");
            AssetsUtils.getInstance(activity).copyOnMainThread("app_runtime/java/default",AppManifest.JAVA_DIR + "/default");
        }
    }

    public static void checkJava17(MainActivity activity){
        activity.runOnUiThread(() -> {
            activity.loadingText.setText(activity.getString(R.string.loading_hint_java_17));
        });
        if (!new File(AppManifest.JAVA_DIR + "/JRE17").exists() || !new File(AppManifest.JAVA_DIR + "/JRE17/version").exists() || Integer.parseInt(Objects.requireNonNull(FileStringUtils.getStringFromFile(AppManifest.JAVA_DIR + "/JRE17/version"))) < AppInfo.JAVA_17_VERSION_CODE) {
            activity.runOnUiThread(() -> {
                activity.loadingText.setText(activity.getString(R.string.loading_hint_java_17_install));
                activity.loadingProgress.setVisibility(View.VISIBLE);
            });
            FileUtils.deleteDirectory(AppManifest.JAVA_DIR + "/JRE17");
            FileUtils.deleteDirectory(AppManifest.DEFAULT_CACHE_DIR + "/java");
            LanzouUrlGetTask task = new LanzouUrlGetTask(activity, new LanzouUrlGetTask.Callback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish(String url) {
                    if (url == null){
                        activity.runOnUiThread(() -> {
                            activity.loadingText.setText(activity.getString(R.string.loading_hint_failed));
                            activity.loadingText.setTextColor(Color.RED);
                        });
                    }
                    else {
                        new Thread(() -> {
                            DownloadUtil.downloadSingleFile(activity, new DownloadTaskListBean("", url, AppManifest.DEFAULT_CACHE_DIR + "/java/JRE17.zip"), new DownloadTask.Feedback() {
                                @Override
                                public void addTask(DownloadTaskListBean bean) {

                                }

                                @Override
                                public void updateProgress(DownloadTaskListBean bean) {
                                    activity.runOnUiThread(() -> {
                                        activity.loadingProgress.setProgress(bean.progress);
                                    });
                                }

                                @Override
                                public void updateSpeed(String speed) {

                                }

                                @Override
                                public void removeTask(DownloadTaskListBean bean) {

                                }

                                @Override
                                public void onFinished(Map<String, String> failedFile) {
                                    if (failedFile.containsKey(url)) {
                                        activity.runOnUiThread(() -> {
                                            activity.loadingText.setText(activity.getString(R.string.loading_hint_failed));
                                            activity.loadingText.setTextColor(Color.RED);
                                        });
                                    }
                                    else {
                                        activity.runOnUiThread(() -> {
                                            activity.loadingProgress.setProgress(0);
                                            unZipJava(activity,"JRE17.zip");
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled() {

                                }
                            });
                        }).start();
                    }
                }
            });
            activity.runOnUiThread(() -> {
                task.execute(AppInfo.JAVA_17_DOWNLOAD_URL);
            });
        }
        else {
            /*
             *检查完成，进入启动器
             */
            activity.loadingHandler.sendEmptyMessage(0);
        }
    }

    public static void unZipJava(MainActivity activity,String name){
        activity.loadingText.setText(activity.getString(R.string.loading_hint_java_17_unzip));
        ZipManager.unzip(AppManifest.DEFAULT_CACHE_DIR + "/java/" + name, AppManifest.JAVA_DIR, new IZipCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int percentDone) {
                activity.loadingProgress.setProgress(percentDone);
            }

            @Override
            public void onFinish(boolean success) {
                if (success) {
                    /*
                     *检查完成，进入启动器
                     */
                    activity.loadingProgress.setProgress(100);
                    new Thread(() -> {
                        if (new File(AppManifest.JAVA_DIR + "/java17").exists()) {
                            FileUtils.rename(AppManifest.JAVA_DIR + "/java17","JRE17");
                        }
                        activity.runOnUiThread(() -> {
                            activity.loadingProgress.setProgress(0);
                            activity.loadingProgress.setVisibility(View.GONE);
                        });
                        activity.loadingHandler.sendEmptyMessage(0);
                    }).start();
                }
                else {
                    activity.loadingText.setText(activity.getString(R.string.loading_hint_failed));
                    activity.loadingText.setTextColor(Color.RED);
                }
            }
        });
    }

    public static void initializeControlPattern (Activity activity, AssetsUtils.FileOperateCallback callback) {
        String[] string = new File(AppManifest.CONTROLLER_DIR + "/").list();
        if (new File(AppManifest.CONTROLLER_DIR + "/").exists()){
            assert string != null;
            if (string.length == 0) {
                AssetsUtils.getInstance(activity.getApplicationContext()).copyAssetsToSD("control", AppManifest.CONTROLLER_DIR).setFileOperateCallback(callback);
            }
        }
        else {
            AssetsUtils.getInstance(activity.getApplicationContext()).copyAssetsToSD("control", AppManifest.CONTROLLER_DIR).setFileOperateCallback(callback);
        }
    }

    public static ArrayList<Account> initializeAccounts(Context context){
        ArrayList<Account> accountList = new ArrayList<>();
        if (new File(AppManifest.ACCOUNT_DIR + "/accounts.json").exists() && GsonUtils.getContentListFromFile(AppManifest.ACCOUNT_DIR + "/accounts.json").size() != 0){
            accountList = GsonUtils.getAccountListFromFile(AppManifest.ACCOUNT_DIR + "/accounts.json");
        }
        else {
            GsonUtils.saveAccounts(accountList,AppManifest.ACCOUNT_DIR + "/accounts.json");
        }
        return accountList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<AuthlibInjectorServer> initializeAuthlibInjectorServer(Context context){
        ArrayList<AuthlibInjectorServer> serverListBeans = new ArrayList<>();
        if (new File(AppManifest.ACCOUNT_DIR + "/authlib_injector_server.json").exists() && GsonUtils.getContentListFromFile(AppManifest.ACCOUNT_DIR + "/authlib_injector_server.json").size() != 0){
            serverListBeans = GsonUtils.getServerListFromFile(AppManifest.ACCOUNT_DIR + "/authlib_injector_server.json");
        }
        else {
            GsonUtils.saveServer(serverListBeans,AppManifest.ACCOUNT_DIR + "/authlib_injector_server.json");
        }
        return serverListBeans;
    }

    public static ArrayList<ContentListBean> initializeContents(Context context){
        ArrayList<ContentListBean> contentList = new ArrayList<>();
        if (new File(AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json").exists() && GsonUtils.getContentListFromFile(AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json").size() != 0){
            contentList = GsonUtils.getContentListFromFile(AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json");
        }
        else {
            contentList.add(new ContentListBean(context.getString(R.string.default_game_file_directory_list_pri),AppManifest.DEFAULT_GAME_DIR,true));
            contentList.add(new ContentListBean(context.getString(R.string.default_game_file_directory_list_sec),AppManifest.INNER_GAME_DIR,false));
            GsonUtils.saveContents(contentList,AppManifest.GAME_FILE_DIRECTORY_DIR + "/game_file_directories.json");
        }
        return contentList;
    }

    public static LauncherSetting initializeLauncherSetting(){
        LauncherSetting launcherSetting;
        if (new File(AppManifest.SETTING_DIR + "/launcher_setting.json").exists()){
            launcherSetting = GsonUtils.getLauncherSettingFromFile(AppManifest.SETTING_DIR + "/launcher_setting.json");
        }
        else {
            launcherSetting = new LauncherSetting(AppManifest.DEFAULT_GAME_DIR,new SourceSetting(true,1,0),0,64,false,true,false,false,false,"DEFAULT",new BackgroundSetting(0,"",""),AppManifest.DEFAULT_CACHE_DIR);
            GsonUtils.saveLauncherSetting(launcherSetting,AppManifest.SETTING_DIR + "/launcher_setting.json");
        }
        return launcherSetting;
    }

    public static PublicGameSetting initializePublicGameSetting(Context context, MainActivity activity){
        PublicGameSetting publicGameSetting;
        if (new File(AppManifest.SETTING_DIR + "/public_game_setting.json").exists()){
            publicGameSetting = GsonUtils.getPublicGameSettingFromFile(AppManifest.SETTING_DIR + "/public_game_setting.json");
        }
        else {
            String currentVersion;
            if (SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory,"").size() != 0){
                currentVersion = SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory,"").get(0).name;
            }
            else {
                currentVersion = "";
            }
            Account account = new Account(0,"","","","","","","","","","","");
            publicGameSetting = new PublicGameSetting(account,AppManifest.DEBUG_DIR,currentVersion);
            GsonUtils.savePublicGameSetting(publicGameSetting,AppManifest.SETTING_DIR + "/public_game_setting.json");
        }
        return publicGameSetting;
    }

    public static PrivateGameSetting initializePrivateGameSetting(Context context){
        PrivateGameSetting privateGameSetting;
        if (new File(AppManifest.SETTING_DIR + "/private_game_setting.json").exists()){
            privateGameSetting = GsonUtils.getPrivateGameSettingFromFile(AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        else {
            int ram = MemoryUtils.findBestRAMAllocation(context);
            privateGameSetting = new PrivateGameSetting(false,false,false,false,false,false,new JavaSetting(true,AppManifest.JAVA_DIR + "/default"),"","","",new GameDirSetting(0,AppManifest.DEFAULT_GAME_DIR),new BoatLauncherSetting(false,"libGL112.so.1","default"),new PojavLauncherSetting(true,"opengles2","default"),new RamSetting(ram,ram,true),0,"Default",1.0F);
            GsonUtils.savePrivateGameSetting(privateGameSetting,AppManifest.SETTING_DIR + "/private_game_setting.json");
        }
        return privateGameSetting;
    }
}
