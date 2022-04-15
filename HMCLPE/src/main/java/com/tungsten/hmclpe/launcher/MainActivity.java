package com.tungsten.hmclpe.launcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.Config;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.InitializeSetting;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.launcher.setting.game.PublicGameSetting;
import com.tungsten.hmclpe.launcher.setting.launcher.LauncherSetting;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.launcher.uis.tools.UIManager;
import com.tungsten.hmclpe.launcher.uis.universal.setting.right.launcher.ExteriorSettingUI;
import com.tungsten.hmclpe.update.UpdateChecker;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public RelativeLayout loadingLayout;
    public LinearLayout launcherLayout;

    public ProgressBar loadingProgress;
    public TextView loadingText;

    public boolean isLoaded = false;
    public boolean dialogMode = false;

    public LauncherSetting launcherSetting;
    public PublicGameSetting publicGameSetting;
    public PrivateGameSetting privateGameSetting;

    public UpdateChecker updateChecker;

    public Toolbar appBar;
    public LinearLayout appBarTitle;
    public ImageButton backToLastUI;
    public TextView currentUIText;
    public ImageButton backToHome;
    public ImageButton closeCurrentUI;
    public ImageButton backToDesktop;
    public ImageButton closeApp;

    public UIManager uiManager;

    public Config exteriorConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loadingLayout = findViewById(R.id.loading_layout);
        launcherLayout = findViewById(R.id.launcher_layout);

        loadingProgress = findViewById(R.id.download_resource_progress);
        loadingText = findViewById(R.id.launcher_loading_text);

        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                init();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1000);
            }
        } else {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
        }
    }

    private void init(){
        new Thread(){
            @Override
            public void run(){
                runOnUiThread(() -> {
                    loadingText.setText(getString(R.string.loading_hint_setting));
                });

                AppManifest.initializeManifest(MainActivity.this);
                launcherSetting = InitializeSetting.initializeLauncherSetting();
                publicGameSetting = InitializeSetting.initializePublicGameSetting(MainActivity.this,MainActivity.this);
                privateGameSetting = InitializeSetting.initializePrivateGameSetting(MainActivity.this);

                runOnUiThread(() -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (launcherSetting.fullscreen) {
                            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                        } else {
                            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
                        }
                    }
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                    updateChecker = new UpdateChecker(MainActivity.this,MainActivity.this);
                });

                DownloadUrlSource.getBalancedSource(MainActivity.this);

                InitializeSetting.checkLauncherFiles(MainActivity.this);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    public final Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                if (!isLoaded) {
                    exteriorConfig = ATE.config(MainActivity.this, null);

                    appBar = findViewById(R.id.app_bar);
                    appBarTitle = findViewById(R.id.app_bar_title);
                    backToLastUI = findViewById(R.id.back_to_last_ui);
                    currentUIText = findViewById(R.id.text_current_ui);
                    backToHome = findViewById(R.id.back_to_home);
                    closeCurrentUI = findViewById(R.id.close_current_ui);
                    backToDesktop = findViewById(R.id.back_to_desktop);
                    closeApp = findViewById(R.id.close_app);

                    backToLastUI.setOnClickListener(MainActivity.this);
                    backToHome.setOnClickListener(MainActivity.this);
                    closeCurrentUI.setOnClickListener(MainActivity.this);
                    backToDesktop.setOnClickListener(MainActivity.this);
                    closeApp.setOnClickListener(MainActivity.this);

                    uiManager = new UIManager(MainActivity.this,MainActivity.this);

                    exteriorConfig.primaryColor(Color.parseColor(ExteriorSettingUI.getThemeColor(MainActivity.this,launcherSetting.launcherTheme)));
                    exteriorConfig.accentColor(Color.parseColor(ExteriorSettingUI.getThemeColor(MainActivity.this,launcherSetting.launcherTheme)));
                    exteriorConfig.apply(MainActivity.this);

                    isLoaded = true;
                    enterLauncher();
                }
            }
        }
    };

    public void enterLauncher(){
        uiManager.gameManagerUI.gameManagerUIManager.versionSettingUI.onLoaded();
        uiManager.downloadUI.downloadUIManager.downloadMinecraftUI.onLoaded();
        uiManager.settingUI.settingUIManager.universalGameSettingUI.onLoaded();
        loadingLayout.setVisibility(View.GONE);
    }

    public void showBarTitle(String title,boolean home,boolean close){
        if (isLoaded){
            CustomAnimationUtils.hideViewToLeft(appBarTitle,this,this,true);
            CustomAnimationUtils.showViewFromRight(backToLastUI,this,this,true);
            if (home){
                CustomAnimationUtils.showViewFromRight(backToHome,this,this,true);
            }
            else {
                CustomAnimationUtils.hideViewToLeft(backToHome,this,this,true);
            }
            if (close){
                CustomAnimationUtils.showViewFromRight(closeCurrentUI,this,this,true);
            }
            else {
                CustomAnimationUtils.hideViewToLeft(closeCurrentUI,this,this,true);
            }
            CustomAnimationUtils.showViewFromRight(currentUIText,this,this,true);
            currentUIText.setText(title);
        }
    }

    public void hideBarTitle(){
        if (isLoaded){
            CustomAnimationUtils.showViewFromLeft(appBarTitle,this,this,true);
            CustomAnimationUtils.hideViewToLeft(backToLastUI,this,this,true);
            if (backToHome.getVisibility() == View.VISIBLE){
                CustomAnimationUtils.hideViewToLeft(backToHome,this,this,true);
            }
            if (closeCurrentUI.getVisibility() == View.VISIBLE){
                CustomAnimationUtils.hideViewToLeft(closeCurrentUI,this,this,true);
            }
            CustomAnimationUtils.hideViewToLeft(currentUIText,this,this,true);
            currentUIText.setText("");
        }
    }

    public void backToLastUI(){
        if (isLoaded){
            if (uiManager.currentUI == uiManager.mainUI){
                backToDeskTop();
            }
            else {
                uiManager.uis.get(uiManager.uis.size() - 1).onStop();
                uiManager.uis.remove(uiManager.uis.size() - 1);
                uiManager.currentUI = uiManager.uis.get(uiManager.uis.size() - 1);
                uiManager.uis.get(uiManager.uis.size() - 1).onStart();
            }
        }
    }

    public void backToHome(){
        uiManager.switchMainUI(uiManager.mainUI);
        uiManager.uis.clear();
        uiManager.uis.add(uiManager.mainUI);
    }

    public void closeCurrentUI(){
        for (int i = 0;i < uiManager.uis.size();i++){
            if (uiManager.uis.get(i) == uiManager.installGameUI){
                uiManager.uis.remove(i);
            }
        }
        for (int i = 0;i < uiManager.uis.size();i++){
            if (uiManager.uis.get(i) == uiManager.downloadForgeUI){
                uiManager.uis.remove(i);
            }
        }
        for (int i = 0;i < uiManager.uis.size();i++){
            if (uiManager.uis.get(i) == uiManager.downloadFabricUI){
                uiManager.uis.remove(i);
            }
        }
        for (int i = 0;i < uiManager.uis.size();i++){
            if (uiManager.uis.get(i) == uiManager.downloadFabricAPIUI){
                uiManager.uis.remove(i);
            }
        }
        for (int i = 0;i < uiManager.uis.size();i++){
            if (uiManager.uis.get(i) == uiManager.downloadLiteLoaderUI){
                uiManager.uis.remove(i);
            }
        }
        for (int i = 0;i < uiManager.uis.size();i++){
            if (uiManager.uis.get(i) == uiManager.downloadOptifineUI){
                uiManager.uis.remove(i);
            }
        }
        uiManager.downloadUI.onStart();
        if (uiManager.currentUI == uiManager.installGameUI){
            uiManager.installGameUI.onStop();
        }
        if (uiManager.currentUI == uiManager.downloadForgeUI){
            uiManager.downloadForgeUI.onStop();
        }
        if (uiManager.currentUI == uiManager.downloadFabricUI){
            uiManager.downloadFabricUI.onStop();
        }
        if (uiManager.currentUI == uiManager.downloadFabricAPIUI){
            uiManager.downloadFabricAPIUI.onStop();
        }
        if (uiManager.currentUI == uiManager.downloadLiteLoaderUI){
            uiManager.downloadLiteLoaderUI.onStop();
        }
        if (uiManager.currentUI == uiManager.downloadOptifineUI){
            uiManager.downloadOptifineUI.onStop();
        }
    }

    public void backToDeskTop(){
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (!dialogMode){
            backToLastUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isLoaded){
            uiManager.onActivityResult(requestCode,resultCode,data);
        }
        if (requestCode == 1000 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                init();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.storage_permissions_remind)
                        .setPositiveButton("OK", (dialog1, which) ->
                                requestPermission())
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri data = intent.getData();
        if (data != null && data.getScheme().equals("ms-xal-00000000402b5328") && data.getHost().equals("auth")) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                init();
            } else {
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.storage_permissions_remind)
                            .setPositiveButton("OK", (dialog1, which) ->
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            1000))
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backToLastUI){
            backToLastUI();
        }
        if (v == backToHome){
            backToHome();
        }
        if (v == closeCurrentUI){
            closeCurrentUI();
        }
        if (v == backToDesktop){
            backToDeskTop();
        }
        if (v == closeApp){
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && launcherSetting != null) {
            if (launcherSetting.fullscreen) {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}