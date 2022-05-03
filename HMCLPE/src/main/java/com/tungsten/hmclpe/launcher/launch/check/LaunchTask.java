package com.tungsten.hmclpe.launcher.launch.check;

import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.tungsten.hmclpe.auth.Account;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.launch.AccountPatch;
import com.tungsten.hmclpe.launcher.launch.GameLaunchSetting;
import com.tungsten.hmclpe.launcher.launch.boat.BoatLauncher;
import com.tungsten.hmclpe.launcher.launch.pojav.PojavLauncher;
import com.tungsten.hmclpe.launcher.setting.game.PrivateGameSetting;
import com.tungsten.hmclpe.manifest.AppManifest;
import com.tungsten.hmclpe.utils.gson.GsonUtils;

import java.io.File;
import java.util.Collections;
import java.util.Vector;

public class LaunchTask extends AsyncTask<Account,Integer, Vector<String>> {

    private final MainActivity activity;
    private final LaunchCallback callback;

    public LaunchTask (MainActivity activity,LaunchCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onStart();
    }

    @Override
    protected Vector<String> doInBackground(Account... accounts) {
        Vector<String> args = new Vector<>();
        String[] accountArgs;
        accountArgs = AccountPatch.getAccountArgs(activity,accounts[0]);
        Collections.addAll(args,accountArgs);
        return args;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Vector<String> vector) {
        super.onPostExecute(vector);
        callback.onFinish(vector);
    }

    public interface LaunchCallback{
        void onStart();
        void onFinish(Vector<String> args);
    }
}
