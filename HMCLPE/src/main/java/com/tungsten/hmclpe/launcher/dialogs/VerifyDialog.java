package com.tungsten.hmclpe.launcher.dialogs;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.VerifyInterface;
import com.tungsten.hmclpe.utils.io.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class VerifyDialog extends Dialog implements View.OnClickListener {

    private final MainActivity activity;
    private final Tencent mTencent;
    private final IUiListener iUiListener;
    private final VerifyInterface verifyInterface;

    private String code;

    private TextView textView;
    private Button obtainPermission;
    private Button cancel;
    private Button copy;
    private Button login;
    private Button verify;

    public VerifyDialog(@NonNull Context context, MainActivity activity, Tencent mTencent, IUiListener iUiListener, VerifyInterface verifyInterface) {
        super(context);
        this.activity = activity;
        this.mTencent = mTencent;
        this.iUiListener = iUiListener;
        this.verifyInterface = verifyInterface;
        setContentView(R.layout.dialog_verify);
        setCancelable(false);
        init();
    }

    private void init() {
        textView = findViewById(R.id.id_text);
        obtainPermission = findViewById(R.id.obtain_permission);
        cancel = findViewById(R.id.cancel);
        login = findViewById(R.id.login);
        copy = findViewById(R.id.copy);
        verify = findViewById(R.id.verify);

        textView.setText(getContext().getString(R.string.dialog_verify_msg));
        obtainPermission.setOnClickListener(this);
        cancel.setOnClickListener(this);
        login.setOnClickListener(this);
        copy.setOnClickListener(this);
        verify.setOnClickListener(this);
    }

    public void onLoginSuccess(String id) {
        code = id;
        textView.setText(getContext().getString(R.string.dialog_verify_msg_sec).replace("%s", code));
        copy.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        verify.setVisibility(View.VISIBLE);
    }

    public void verify() {
        textView.setText(getContext().getString(R.string.dialog_verify_verifying));
        String url = "http://101.43.66.4:8080/verify/idverify?id=" + code;
        verify.setEnabled(false);
        new Thread(() -> {
            try {
                String result = NetworkUtils.doGet(NetworkUtils.toURL(url));
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("isValid")) {
                    activity.runOnUiThread(() -> {
                        Toast.makeText(getContext(), getContext().getString(R.string.dialog_verify_verify_success), Toast.LENGTH_SHORT).show();
                        verifyInterface.onSuccess();
                        dismiss();
                    });
                }
                else {
                    activity.runOnUiThread(() -> {
                        verify.setEnabled(true);
                        try {
                            Toast.makeText(getContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(() -> {
                textView.setText(getContext().getString(R.string.dialog_verify_msg_sec).replace("%s", code));
            });
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view == obtainPermission) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getContext().getString(R.string.dialog_obtain_permission_title));
            builder.setMessage(getContext().getString(R.string.dialog_obtain_permission_msg));
            builder.setPositiveButton(getContext().getString(R.string.dialog_obtain_permission_positive), (dialogInterface, i) -> {
                Uri uri = Uri.parse("https://afdian.net/@tungs");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            });
            builder.setNegativeButton(getContext().getString(R.string.dialog_obtain_permission_negative), (dialogInterface, i) -> {});
            builder.create().show();
        }
        if (view == cancel) {
            verifyInterface.onCancel();
            dismiss();
        }
        if (view == login) {
            textView.setText(getContext().getString(R.string.dialog_verify_logging));
            new Thread(() -> mTencent.login(activity, "get_simple_userinfo", iUiListener)).start();
        }
        if (view == copy) {
            ClipboardManager clip = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText(null, code);
            clip.setPrimaryClip(data);
            Toast.makeText(getContext(), getContext().getString(R.string.dialog_verify_copy_success), Toast.LENGTH_SHORT).show();
        }
        if (view == verify) {
            verify();
        }
    }

}
