package com.tungsten.hmclpe.launcher.uis.universal.multiplayer.right;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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

import com.google.gson.Gson;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.uis.tools.BaseUI;
import com.tungsten.hmclpe.multiplayer.hiper.HiPerService;
import com.tungsten.hmclpe.multiplayer.hiper.LocalServerBroadcaster;
import com.tungsten.hmclpe.utils.animation.CustomAnimationUtils;
import com.tungsten.hmclpe.utils.file.FileStringUtils;
import com.tungsten.hmclpe.utils.io.HttpRequest;
import com.tungsten.hmclpe.utils.string.StringUtils;

import net.matrix.mobile_hiper.HiPerCallback;
import net.matrix.mobile_hiper.Sites;

import java.io.IOException;
import java.util.Objects;

public class HiPerUI extends BaseUI implements View.OnClickListener, TextWatcher {

    public static final int START_HIPER_CODE = 2357;

    public static LocalServerBroadcaster localServerBroadcaster;

    private Sites.Site site;

    public LinearLayout hiPerUI;

    private LinearLayout hiperLayout;
    private ProgressBar progressBar;

    private LinearLayout verifyLayout;
    private EditText editToken;
    private TextView apply;
    private Button launchHiper;

    private LinearLayout startLayout;
    private TextView creatorTutorial;
    private TextView slaveTutorial;
    private EditText editPort;
    private TextView address;
    private Button copy;
    private EditText editAddress;
    private Button join;

    private TextView about;
    private TextView eula;

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

        startLayout = activity.findViewById(R.id.hiper_start_page);
        creatorTutorial = activity.findViewById(R.id.creator_tutorial);
        slaveTutorial = activity.findViewById(R.id.slave_tutorial);
        editPort = activity.findViewById(R.id.hiper_port);
        address = activity.findViewById(R.id.hiper_gen);
        copy = activity.findViewById(R.id.hiper_copy_address);
        editAddress = activity.findViewById(R.id.hiper_address);
        join = activity.findViewById(R.id.hiper_join);

        about = activity.findViewById(R.id.hiper_about_link);
        eula = activity.findViewById(R.id.hiper_eula_link);

        apply.setOnClickListener(this);
        launchHiper.setOnClickListener(this);

        creatorTutorial.setOnClickListener(this);
        slaveTutorial.setOnClickListener(this);
        copy.setOnClickListener(this);
        join.setOnClickListener(this);

        about.setOnClickListener(this);
        eula.setOnClickListener(this);

        editToken.setText(FileStringUtils.getStringFromFile(context.getFilesDir().getAbsolutePath() + "/hiper/hiper.token"));
        editToken.addTextChangedListener(this);
        editPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                if (site != null) {
                    String ip = site.getCert().getCert().getDetails().getIps().get(0).split("/")[0];
                    address.setText(ip + ":" + editPort.getText().toString());
                }
            }
        });
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
        if (view == creatorTutorial) {

        }
        if (view == slaveTutorial) {

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
                        if (Objects.requireNonNull(Sites.Site.fromFile(context)).getId().equals(token)) {
                            String url = "https://cert.mcer.cn/point.yml";
                            String conf = HttpRequest.GET(url).getString();
                            String path = context.getFilesDir().getAbsolutePath() + "/hiper/hiper_config.json";
                            String s = FileStringUtils.getStringFromFile(path);
                            Sites.IncomingSite incomingSite = new Gson().fromJson(s, Sites.IncomingSite.class);
                            incomingSite.update(conf);
                            incomingSite.save(context);
                            activity.runOnUiThread(() -> {
                                hiperLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                verifyLayout.setVisibility(View.GONE);
                                startLayout.setVisibility(View.VISIBLE);
                                startVpn();
                                site = Sites.Site.fromFile(context);
                                editPort.setText("0");
                            });
                        }
                        else {
                            String conf = HttpRequest.GET(String.format("https://cert.mcer.cn/%s.yml", token)).getString();
                            if (StringUtils.isNotBlank(conf)) {
                                Sites.IncomingSite incomingSite = Sites.IncomingSite.parse(conf, token);
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
        if (view == copy) {
            ClipboardManager clip = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText(null, address.getText().toString());
            clip.setPrimaryClip(data);
            Toast.makeText(context, context.getString(R.string.dialog_community_copy_success), Toast.LENGTH_SHORT).show();
        }
        if (view == join) {
            if (localServerBroadcaster != null && localServerBroadcaster.isRunning()) {
                localServerBroadcaster.close();
                join.setText(context.getString(R.string.hiper_ui_start_join));
            }
            else {
                localServerBroadcaster = new LocalServerBroadcaster(editAddress.getText().toString());
                localServerBroadcaster.start();
                join.setText(context.getString(R.string.hiper_ui_start_exit));
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
