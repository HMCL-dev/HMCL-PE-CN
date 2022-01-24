package com.tungsten.hmclpe.launcher.list.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.auth.AuthInfo;
import com.tungsten.hmclpe.auth.AuthenticationException;
import com.tungsten.hmclpe.auth.yggdrasil.GameProfile;
import com.tungsten.hmclpe.auth.yggdrasil.MojangYggdrasilProvider;
import com.tungsten.hmclpe.auth.yggdrasil.Texture;
import com.tungsten.hmclpe.auth.yggdrasil.TextureType;
import com.tungsten.hmclpe.auth.yggdrasil.YggdrasilService;
import com.tungsten.hmclpe.auth.yggdrasil.YggdrasilSession;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.account.SkinPreviewDialog;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class AccountListAdapter extends BaseAdapter {

    private Context context;
    private MainActivity activity;
    private ArrayList<Account> accounts;

    public AccountListAdapter (Context context, MainActivity activity, ArrayList<Account> accounts){
        this.context = context;
        this.activity = activity;
        this.accounts = accounts;
    }

    private class ViewHolder{
        RadioButton check;
        ImageView face;
        ImageView hat;
        TextView name;
        TextView type;
        ImageButton refresh;
        ImageButton skin;
        ImageButton delete;
    }

    @SuppressLint("HandlerLeak")
    public final android.os.Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {

            }
        }
    };

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account,null);
            viewHolder.check = convertView.findViewById(R.id.select_account);
            viewHolder.face = convertView.findViewById(R.id.skin_face);
            viewHolder.hat = convertView.findViewById(R.id.skin_hat);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.type = convertView.findViewById(R.id.type);
            viewHolder.refresh = convertView.findViewById(R.id.refresh);
            viewHolder.skin = convertView.findViewById(R.id.skin);
            viewHolder.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Account account = accounts.get(position);
        boolean isSelected = account.email.equals(activity.publicGameSetting.account.email) && account.auth_player_name.equals(activity.publicGameSetting.account.auth_player_name) && account.auth_uuid.equals(activity.publicGameSetting.account.auth_uuid) && account.loginServer.equals(activity.publicGameSetting.account.loginServer);
        if (account.loginType == 1){
            viewHolder.name.setText(account.auth_player_name);
            viewHolder.type.setText(context.getString(R.string.item_account_type_offline));
            if (isSelected){
                activity.uiManager.mainUI.accountName.setText(account.auth_player_name);
                activity.uiManager.mainUI.accountType.setText(context.getString(R.string.item_account_type_offline));
            }
            viewHolder.face.post(new Runnable() {
                @Override
                public void run() {
                    AssetManager manager = context.getAssets();
                    InputStream inputStream;
                    Bitmap bitmap;
                    try {
                        if (UUID.fromString(account.auth_uuid).toString().equals("00000000-0000-0000-0000-000000000000")){
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
                        float scale = (viewHolder.face.getWidth() / 8);
                        Matrix matrixSec = new Matrix();
                        float scaleSec = (viewHolder.hat.getWidth() / 8);
                        matrix.postScale(scale,scale);
                        Bitmap newBitmap = Bitmap.createBitmap(faceBitmap,0,0,8,8,matrix,false);
                        matrixSec.postScale(scaleSec,scaleSec);
                        Bitmap newBitmapSec = Bitmap.createBitmap(faceBitmapSec,0,0,8,8,matrixSec,false);
                        viewHolder.face.setImageBitmap(newBitmap);
                        viewHolder.hat.setImageBitmap(newBitmapSec);
                        if (isSelected){
                            activity.uiManager.mainUI.accountSkinFace.setImageBitmap(newBitmap);
                            activity.uiManager.mainUI.accountSkinHat.setImageBitmap(newBitmapSec);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        if (account.loginType == 2){
            viewHolder.name.setText(account.email + " - " +account.auth_player_name);
            viewHolder.type.setText(context.getString(R.string.item_account_type_mojang));
            if (isSelected){
                activity.uiManager.mainUI.accountName.setText(account.auth_player_name);
                activity.uiManager.mainUI.accountType.setText(context.getString(R.string.item_account_type_mojang));
            }
            new Thread(){
                @Override
                public void run() {
                    YggdrasilService yggdrasilService = new YggdrasilService(new MojangYggdrasilProvider());
                    try {
                        Map<TextureType, Texture> map = YggdrasilService.getTextures(yggdrasilService.getCompleteGameProfile(UUID.fromString(account.auth_uuid)).get()).get();
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
                        float scale = (viewHolder.face.getWidth() / 8);
                        Matrix matrixSec = new Matrix();
                        float scaleSec = (viewHolder.hat.getWidth() / 8);
                        matrix.postScale(scale,scale);
                        Bitmap newBitmap = Bitmap.createBitmap(faceBitmap,0,0,8,8,matrix,false);
                        matrixSec.postScale(scaleSec,scaleSec);
                        Bitmap newBitmapSec = Bitmap.createBitmap(faceBitmapSec,0,0,8,8,matrixSec,false);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.face.setImageBitmap(newBitmap);
                                viewHolder.hat.setImageBitmap(newBitmapSec);
                                if (isSelected){
                                    activity.uiManager.mainUI.accountSkinFace.setImageBitmap(newBitmap);
                                    activity.uiManager.mainUI.accountSkinHat.setImageBitmap(newBitmapSec);
                                }
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
                            float scale = (viewHolder.face.getWidth() / 8);
                            Matrix matrixSec = new Matrix();
                            float scaleSec = (viewHolder.hat.getWidth() / 8);
                            matrix.postScale(scale,scale);
                            Bitmap newBitmap = Bitmap.createBitmap(faceBitmap,0,0,8,8,matrix,false);
                            matrixSec.postScale(scaleSec,scaleSec);
                            Bitmap newBitmapSec = Bitmap.createBitmap(faceBitmapSec,0,0,8,8,matrixSec,false);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.face.setImageBitmap(newBitmap);
                                    viewHolder.hat.setImageBitmap(newBitmapSec);
                                    if (isSelected){
                                        activity.uiManager.mainUI.accountSkinFace.setImageBitmap(newBitmap);
                                        activity.uiManager.mainUI.accountSkinHat.setImageBitmap(newBitmapSec);
                                    }
                                }
                            });
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        if (account.loginType == 3){

        }
        if (account.loginType == 4){

        }
        viewHolder.check.setChecked(isSelected);
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.publicGameSetting.account = account;
                GsonUtils.savePublicGameSetting(activity.publicGameSetting, AppManifest.SETTING_DIR + "/public_game_setting.json");
                notifyDataSetChanged();
            }
        });
        viewHolder.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.loginType == 1){

                }
                if (account.loginType == 2){
                    YggdrasilService yggdrasilService = new YggdrasilService(new MojangYggdrasilProvider());
                    try {
                        YggdrasilSession yggdrasilSession = yggdrasilService.refresh(account.auth_access_token, account.auth_client_token, new GameProfile(UUID.fromString(account.auth_uuid),account.auth_player_name));
                        AuthInfo authInfo = yggdrasilSession.toAuthInfo();
                    }catch (AuthenticationException e){
                        e.printStackTrace();
                    }
                }
                if (account.loginType == 3){

                }
                if (account.loginType == 4){

                }
            }
        });
        viewHolder.skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.loginType == 1){
                    SkinPreviewDialog skinPreviewDialog = new SkinPreviewDialog(context,activity,account);
                    skinPreviewDialog.show();
                }
                else {

                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
