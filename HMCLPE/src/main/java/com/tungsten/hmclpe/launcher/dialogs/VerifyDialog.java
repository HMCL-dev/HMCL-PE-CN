package com.tungsten.hmclpe.launcher.dialogs;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.VerifyInterface;

public class VerifyDialog extends Dialog implements View.OnClickListener {

    private MainActivity activity;
    private SharedPreferences.Editor editor;
    private VerifyInterface verifyInterface;

    private String code;

    private TextView textView;
    private EditText editText;
    private Button cancel;
    private Button copy;
    private Button verify;

    public VerifyDialog(@NonNull Context context, MainActivity activity, SharedPreferences.Editor editor, VerifyInterface verifyInterface) {
        super(context);
        this.activity = activity;
        this.editor = editor;
        this.verifyInterface = verifyInterface;
        setContentView(R.layout.dialog_verify);
        setCancelable(false);
        init();
    }

    private void init() {
        code = DeviceIdentifier.getOAID(getContext());

        textView = findViewById(R.id.oaid_text);
        editText = findViewById(R.id.edit_verify_code);
        cancel = findViewById(R.id.cancel);
        copy = findViewById(R.id.copy_oaid);
        verify = findViewById(R.id.verify);

        textView.setText(getContext().getString(R.string.dialog_verify_msg).replace("%s", code));
        cancel.setOnClickListener(this);
        copy.setOnClickListener(this);
        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == cancel) {
            verifyInterface.onCancel();
            dismiss();
        }
        if (view == copy) {
            ClipboardManager clip = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText(null, code);
            clip.setPrimaryClip(data);
            Toast.makeText(getContext(), getContext().getString(R.string.dialog_verify_copy_success), Toast.LENGTH_SHORT).show();
        }
        if (view == verify) {
            if (activity.isValid(editText.getText().toString())){
                editor.putString("code",editText.getText().toString());
                editor.putBoolean("verified",true);
                editor.commit();
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_verify_verify_success), Toast.LENGTH_SHORT).show();
                dismiss();
                verifyInterface.onSuccess();
            }
            else {
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_verify_verify_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
