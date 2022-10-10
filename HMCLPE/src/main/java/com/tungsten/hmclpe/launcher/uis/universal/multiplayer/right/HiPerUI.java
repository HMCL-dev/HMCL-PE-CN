package com.tungsten.hmclpe.launcher.uis.universal.multiplayer.right;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.multiplayer.hiper.HiPerService;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.io.HttpRequest;
import com.tungsten.hmclpe.utils.string.StringUtils;

import net.matrix.mobile_hiper.HiPerCallback;
import net.matrix.mobile_hiper.Sites;

import java.io.IOException;

public class HiPerUI extends BaseUI implements View.OnClickListener, TextWatcher {

    public static final int START_HIPER_CODE = 2357;

    public LinearLayout hiPerUI;

    private LinearLayout hiperLayout;
    private ProgressBar progressBar;

    private LinearLayout verifyLayout;
    private EditText editToken;
    private TextView apply;
    private Button launchHiper;
    private TextView about;
    private TextView eula;

    private LinearLayout startLayout;

    public HiPerUI(Context context, MainActivity activity) {
        super(context, activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        hiPerUI = activity.findViewById(R.id.ui_hiper);

        hiperLayout = activity.findViewById(R.id.hiper_layout);
        progressBar = activity.findViewById(R.id.load_hiper_conf_progress);

        verifyLayout = activity.findViewById(R.id.hiper_verify_page);
        editToken = activity.findViewById(R.id.hiper_token);
        apply = activity.findViewById(R.id.apply_hiper_token);
        launchHiper = activity.findViewById(R.id.hiper_launch);
        about = activity.findViewById(R.id.hiper_about_link);
        eula = activity.findViewById(R.id.hiper_eula_link);

        startLayout = activity.findViewById(R.id.hiper_start_page);

        apply.setOnClickListener(this);
        launchHiper.setOnClickListener(this);
        about.setOnClickListener(this);
        eula.setOnClickListener(this);

        editToken.setText(FileStringUtils.getStringFromFile(context.getFilesDir().getAbsolutePath() + "/hiper/hiper.token"));
        editToken.addTextChangedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        CustomAnimationUtils.showViewFromLeft(hiPerUI,activity,context,false);
    }

    @Override
    public void onStop() {
        super.onStop();
        CustomAnimationUtils.hideViewToLeft(hiPerUI,activity,context,false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_HIPER_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent(context, HiPerService.class);
            HiPerService.setHiPerCallback(new HiPerCallback() {
                @Override
                public void run(int code) {
                    System.out.println(code == 1 ? "success" : "failed");
                }

                @Override
                public void onExit(String error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error");
                    builder.setMessage(error);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }
            });
            activity.startService(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == apply) {

        }
        if (view == about) {

        }
        if (view == eula) {

        }
        if (view == launchHiper) {
            if (!HiPerService.isRunning()) {
                String token = editToken.getText().toString();
                hiperLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                verifyLayout.setVisibility(View.GONE);
                startLayout.setVisibility(View.GONE);
                new Thread(() -> {
                    try {
                        String conf = HttpRequest.GET(String.format("https://cert.mcer.cn/%s.yml", token)).getString();
                        if (StringUtils.isNotBlank(conf)) {
                            Sites.IncomingSite incomingSite = Sites.IncomingSite.parse(conf);
                            incomingSite.save(context);
                            activity.runOnUiThread(() -> {
                                hiperLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                verifyLayout.setVisibility(View.GONE);
                                startLayout.setVisibility(View.VISIBLE);
                                startVpn();
                            });
                        }
                        else {
                            activity.runOnUiThread(() -> {
                                Toast.makeText(context, "Invalid token!", Toast.LENGTH_SHORT).show();
                                hiperLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                verifyLayout.setVisibility(View.VISIBLE);
                                startLayout.setVisibility(View.GONE);
                            });
                        }
                    } catch (IOException e) {
                        activity.runOnUiThread(() -> {
                            Toast.makeText(context, "Failed to get configuration, please check your network and token!", Toast.LENGTH_SHORT).show();
                            hiperLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            verifyLayout.setVisibility(View.VISIBLE);
                            startLayout.setVisibility(View.GONE);
                        });
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String token = editToken.getText().toString();
        String path = context.getFilesDir().getAbsolutePath() + "/hiper/hiper.token";
        net.matrix.mobile_hiper.utils.StringUtils.writeFile(path, token);
    }

    private void startVpn() {
        Intent vpnPrepareIntent = VpnService.prepare(context);
        if (vpnPrepareIntent != null) {
            activity.startActivityForResult(vpnPrepareIntent, START_HIPER_CODE);
        }
        else {
            onActivityResult(START_HIPER_CODE, RESULT_OK, null);
        }
    }
}
