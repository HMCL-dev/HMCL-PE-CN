package com.tungsten.hmclpe.launcher.uis.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.AuthenticationException;
import com.tungsten.hmclpe.auth.yggdrasil.MojangYggdrasilProvider;
import com.tungsten.hmclpe.auth.yggdrasil.Texture;
import com.tungsten.hmclpe.auth.yggdrasil.TextureType;
import com.tungsten.hmclpe.auth.yggdrasil.YggdrasilService;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.launch.boat.BoatMinecraftActivity;
import com.tungsten.hmclpe.launcher.launch.pojav.PojavMinecraftActivity;
import com.tungsten.hmclpe.launcher.list.local.game.GameListBean;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class MainUI extends BaseUI implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public boolean isLoaded = false;

    public LinearLayout mainUI;

    private LinearLayout startAccountUI;
    private LinearLayout startGameManagerUI;
    private LinearLayout startVersionListUI;
    private LinearLayout startDownloadUI;
    private LinearLayout startMultiPlayerUI;
    private LinearLayout startSettingUI;

    private LinearLayout startGame;
    private TextView launchVersionText;

    public ImageView accountSkinFace;
    public ImageView accountSkinHat;
    public TextView accountName;
    public TextView accountType;

    private ImageView versionIcon;
    private LinearLayout noVersionAlert;
    private TextView currentVersionText;

    public MainUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainUI = activity.findViewById(R.id.ui_main);

        startAccountUI = activity.findViewById(R.id.start_ui_account);
        startGameManagerUI = activity.findViewById(R.id.start_ui_game_manager);
        startVersionListUI = activity.findViewById(R.id.start_ui_version_list);
        startDownloadUI = activity.findViewById(R.id.start_ui_download);
        startMultiPlayerUI = activity.findViewById(R.id.start_ui_multi_player);
        startSettingUI = activity.findViewById(R.id.start_ui_setting);

        startGame = activity.findViewById(R.id.launcher_play_button);
        launchVersionText = activity.findViewById(R.id.launch_version_text);

        accountSkinFace = activity.findViewById(R.id.account_skin_face);
        accountSkinHat = activity.findViewById(R.id.account_skin_hat);
        accountName = activity.findViewById(R.id.account_name_text);
        accountType = activity.findViewById(R.id.account_state_text);

        versionIcon = activity.findViewById(R.id.current_version_icon);
        noVersionAlert = activity.findViewById(R.id.no_version_alert_text);
        currentVersionText = activity.findViewById(R.id.current_version_name_text);

        startAccountUI.setOnClickListener(this);
        startGameManagerUI.setOnClickListener(this);
        startVersionListUI.setOnClickListener(this);
        startDownloadUI.setOnClickListener(this);
        startMultiPlayerUI.setOnClickListener(this);
        startSettingUI.setOnClickListener(this);

        startGame.setOnClickListener(this);

        switch (activity.publicGameSetting.account.loginType){
            case 1:
                accountName.setText(activity.publicGameSetting.account.auth_player_name);
                accountType.setText(context.getString(R.string.item_account_type_offline));
                AssetManager manager = context.getAssets();
                InputStream inputStream;
                Bitmap bitmap;
                try {
                    if (UUID.fromString(activity.publicGameSetting.account.auth_uuid).toString().equals("00000000-0000-0000-0000-000000000000")){
                        inputStream = manager.open("img/steve.png");
                    }
                    else {
                        inputStream = manager.open("img/alex.png");
                    }
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap faceBitmap;
                    Bitmap faceBitmapSec;
                    faceBitmap = Bitmap.createBitmap(bitmap, 8, 8, 8, 8, (Matrix)null, false);
                    faceBitmapSec = Bitmap.createBitmap(bitmap, 40, 8, 8, 8, (Matrix)null, false);
                    Matrix matrix = new Matrix();
                    float scale = (accountSkinFace.getWidth() / 8);
                    Matrix matrixSec = new Matrix();
                    float scaleSec = (accountSkinHat.getWidth() / 8);
                    matrix.postScale(scale,scale);
                    Bitmap newBitmap = Bitmap.createBitmap(faceBitmap,0,0,8,8,matrix,false);
                    matrixSec.postScale(scaleSec,scaleSec);
                    Bitmap newBitmapSec = Bitmap.createBitmap(faceBitmapSec,0,0,8,8,matrixSec,false);
                    accountSkinFace.setImageBitmap(newBitmap);
                    accountSkinHat.setImageBitmap(newBitmapSec);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if (!isLoaded && activity.isLoaded){
                    activity.enterLauncher();
                }
                isLoaded = true;
                break;
            case 2:
                accountName.setText(activity.publicGameSetting.account.auth_player_name);
                accountType.setText(context.getString(R.string.item_account_type_mojang));
                new Thread(){
                    @Override
                    public void run() {
                        YggdrasilService yggdrasilService = new YggdrasilService(new MojangYggdrasilProvider());
                        try {
                            Map<TextureType, Texture> map = YggdrasilService.getTextures(yggdrasilService.getCompleteGameProfile(UUID.fromString(activity.publicGameSetting.account.auth_uuid)).get()).get();
                            Texture texture = map.get(TextureType.SKIN);
                            URL url = new URL(texture.getUrl().replaceFirst("http","https"));
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.connect();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap skin = BitmapFactory.decodeStream(inputStream);
                            Bitmap faceBitmap;
                            Bitmap faceBitmapSec;
                            faceBitmap = Bitmap.createBitmap(skin, 8, 8, 8, 8, (Matrix)null, false);
                            faceBitmapSec = Bitmap.createBitmap(skin, 40, 8, 8, 8, (Matrix)null, false);
                            Matrix matrix = new Matrix();
                            float scale = (accountSkinFace.getWidth() / 8);
                            Matrix matrixSec = new Matrix();
                            float scaleSec = (accountSkinHat.getWidth() / 8);
                            matrix.postScale(scale,scale);
                            Bitmap newBitmap = Bitmap.createBitmap(faceBitmap,0,0,8,8,matrix,false);
                            matrixSec.postScale(scaleSec,scaleSec);
                            Bitmap newBitmapSec = Bitmap.createBitmap(faceBitmapSec,0,0,8,8,matrixSec,false);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    accountSkinFace.setImageBitmap(newBitmap);
                                    accountSkinHat.setImageBitmap(newBitmapSec);
                                }
                            });
                        } catch (AuthenticationException | IOException e) {
                            e.printStackTrace();
                            //handler.sendEmptyMessage(0);
                            AssetManager manager = context.getAssets();
                            InputStream inputStream;
                            Bitmap bitmap;
                            try {
                                inputStream = manager.open("img/alex.png");
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                Bitmap faceBitmap;
                                Bitmap faceBitmapSec;
                                faceBitmap = Bitmap.createBitmap(bitmap, 8, 8, 8, 8, (Matrix)null, false);
                                faceBitmapSec = Bitmap.createBitmap(bitmap, 40, 8, 8, 8, (Matrix)null, false);
                                Matrix matrix = new Matrix();
                                float scale = (accountSkinFace.getWidth() / 8);
                                Matrix matrixSec = new Matrix();
                                float scaleSec = (accountSkinHat.getWidth() / 8);
                                matrix.postScale(scale,scale);
                                Bitmap newBitmap = Bitmap.createBitmap(faceBitmap,0,0,8,8,matrix,false);
                                matrixSec.postScale(scaleSec,scaleSec);
                                Bitmap newBitmapSec = Bitmap.createBitmap(faceBitmapSec,0,0,8,8,matrixSec,false);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        accountSkinFace.setImageBitmap(newBitmap);
                                        accountSkinHat.setImageBitmap(newBitmapSec);
                                    }
                                });
                            }
                            catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        if (!isLoaded && activity.isLoaded){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    activity.enterLauncher();
                                }
                            });
                        }
                        isLoaded = true;
                    }
                }.start();
                break;
            case 3:
                accountName.setText(activity.publicGameSetting.account.auth_player_name);
                accountType.setText(context.getString(R.string.item_account_type_microsoft));
                if (!isLoaded && activity.isLoaded){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            activity.enterLauncher();
                        }
                    });
                }
                isLoaded = true;
                break;
            case 4:
                accountName.setText(activity.publicGameSetting.account.auth_player_name);
                //accountType.setText(context.getString(R.string.item_account_type_auth_lib));
                if (!isLoaded && activity.isLoaded){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            activity.enterLauncher();
                        }
                    });
                }
                isLoaded = true;
                break;
            default:
                accountName.setText(context.getString(R.string.launcher_scroll_account_name));
                accountType.setText(context.getString(R.string.launcher_scroll_account_state));
                if (!isLoaded && activity.isLoaded){
                    activity.enterLauncher();
                }
                isLoaded = true;
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(mainUI,activity,context,true);
        activity.hideBarTitle();

        ArrayList<GameListBean> gameList = SettingUtils.getLocalVersionInfo(activity.launcherSetting.gameFileDirectory,activity.publicGameSetting.currentVersion);
        ArrayList<String> names = new ArrayList<>();
        String currentVersion = "";
        if (!activity.publicGameSetting.currentVersion.equals("")){
            currentVersion = activity.publicGameSetting.currentVersion.substring(activity.publicGameSetting.currentVersion.lastIndexOf("/") + 1);
        }
        for (int i = 0;i < gameList.size();i++){
            names.add(gameList.get(i).name);
        }
        ArrayAdapter<String> launcherVersionAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,names);
        launcherVersionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner gameVersionSpinner = activity.findViewById(R.id.launcher_spinner_version);
        gameVersionSpinner.setAdapter(launcherVersionAdapter);
        gameVersionSpinner.setSelection(launcherVersionAdapter.getPosition(currentVersion));
        gameVersionSpinner.setOnItemSelectedListener(this);
        if (!currentVersion.equals("") && names.contains(currentVersion)){
            noVersionAlert.setVisibility(View.GONE);
            currentVersionText.setVisibility(View.VISIBLE);
            currentVersionText.setText(currentVersion);
            launchVersionText.setText(currentVersion);
        }
        else {
            noVersionAlert.setVisibility(View.VISIBLE);
            currentVersionText.setVisibility(View.GONE);
            launchVersionText.setText(context.getString(R.string.launcher_button_current_version));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(mainUI,activity,context,true);
    }

    @Override
    public void onClick(View v) {
        if (v == startAccountUI){
            activity.uiManager.switchMainUI(activity.uiManager.accountUI);
        }
        if (v == startGameManagerUI){
            if (noVersionAlert.getVisibility() == View.VISIBLE){
                activity.uiManager.switchMainUI(activity.uiManager.versionListUI);
            }
            else {
                activity.uiManager.gameManagerUI.versionName = activity.publicGameSetting.currentVersion.substring(activity.publicGameSetting.currentVersion.lastIndexOf("/") + 1);
                activity.uiManager.switchMainUI(activity.uiManager.gameManagerUI);
            }
        }
        if (v == startVersionListUI){
            activity.uiManager.switchMainUI(activity.uiManager.versionListUI);
        }
        if (v == startDownloadUI){
            activity.uiManager.switchMainUI(activity.uiManager.downloadUI);
        }
        if (v == startMultiPlayerUI){

        }
        if (v == startSettingUI){
            activity.uiManager.switchMainUI(activity.uiManager.settingUI);
        }
        if (v == startGame){
            Intent intent = new Intent(context, BoatMinecraftActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("setting_path",AppManifest.SETTING_DIR + "/private_game_setting.json");
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activity.publicGameSetting.currentVersion = activity.launcherSetting.gameFileDirectory + "/versions/" + parent.getItemAtPosition(position).toString();
        GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
        currentVersionText.setText(parent.getItemAtPosition(position).toString());
        launchVersionText.setText(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
