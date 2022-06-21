package com.tungsten.hmclpe.launcher.uis.game.version.universal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import com.tungsten.filepicker.Constants;
import com.tungsten.filepicker.FileChooser;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.mod.ManuallyCreatedModpackException;
import com.tungsten.hmclpe.launcher.mod.Modpack;
import com.tungsten.hmclpe.launcher.mod.ModpackHelper;
import com.tungsten.hmclpe.launcher.mod.UnsupportedModpackException;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.file.UriUtils;
import com.tungsten.hmclpe.utils.io.ZipTools;

import java.io.File;
import java.io.IOException;

public class InstallPackageUI extends BaseUI implements View.OnClickListener {

    public static final int SELECT_PACKAGE_REQUEST = 5700;

    public LinearLayout installPackageUI;

    private LinearLayout installLocal;
    private LinearLayout installOnline;

    public InstallPackageUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        installPackageUI = activity.findViewById(R.id.ui_install_package);

        installLocal = activity.findViewById(R.id.install_package_local);
        installOnline = activity.findViewById(R.id.install_package_online);
        installLocal.setOnClickListener(this);
        installOnline.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.showBarTitle(context.getResources().getString(R.string.install_package_ui_title),false,true);
        CustomAnimationUtils.showViewFromLeft(installPackageUI,activity,context,true);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(installPackageUI,activity,context,true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PACKAGE_REQUEST && resultCode != Activity.RESULT_OK) {
            activity.backToLastUI();
        }
        if (requestCode == SELECT_PACKAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            String path = UriUtils.getRealPathFromUri_AboveApi19(context,uri);
            new Thread(() -> {
                try {
                    Modpack modpack = ModpackHelper.readModpackManifest(new File(path).toPath(), ZipTools.findSuitableEncoding(new File(path).toPath()));
                    System.out.println(modpack.getName());
                    System.out.println(modpack.getVersion());
                    System.out.println(modpack.getGameVersion());
                    System.out.println(modpack.getAuthor());
                } catch (ManuallyCreatedModpackException e) {
                    e.printStackTrace();
                } catch (UnsupportedModpackException | IOException e) {
                    e.printStackTrace();
                    activity.backToLastUI();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.dialog_package_not_support_title));
                    builder.setMessage(context.getString(R.string.dialog_package_not_support_msg));
                    builder.setPositiveButton(context.getString(R.string.dialog_package_not_support_exit), null);
                }
            }).start();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == installLocal) {
            Intent intent = new Intent(context, FileChooser.class);
            intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
            intent.putExtra(Constants.ALLOWED_FILE_EXTENSIONS, "zip");
            intent.putExtra(Constants.INITIAL_DIRECTORY, new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getAbsolutePath());
            activity.startActivityForResult(intent, SELECT_PACKAGE_REQUEST);
        }
        if (view == installOnline) {

        }
    }
}
