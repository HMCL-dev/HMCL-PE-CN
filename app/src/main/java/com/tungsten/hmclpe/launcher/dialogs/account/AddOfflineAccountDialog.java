package com.tungsten.hmclpe.launcher.dialogs.account;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tungsten.hmclpe.R;

public class AddOfflineAccountDialog extends Dialog implements View.OnClickListener, TextWatcher {

    private EditText editName;
    private EditText editUUID;

    private TextView purchaseLink;

    private LinearLayout showAdvanceSetting;
    private ImageView spinView;
    private LinearLayout editUUIDLayout;
    private LinearLayout hintLayout;

    private Button login;
    private Button cancel;
    
    public AddOfflineAccountDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_add_offline_account);
        setCancelable(false);
        init();
    }
    
    private void init(){
        editName = findViewById(R.id.edit_user_name);
        editUUID = findViewById(R.id.edit_uuid);

        editName.addTextChangedListener(this);
        editUUID.addTextChangedListener(this);

        purchaseLink = findViewById(R.id.purchase_link);
        purchaseLink.setMovementMethod(LinkMovementMethod.getInstance());
        showAdvanceSetting = findViewById(R.id.show_advance_setting);
        showAdvanceSetting.setOnClickListener(this);
        spinView = findViewById(R.id.spin_view);
        editUUIDLayout = findViewById(R.id.edit_uuid_layout);
        hintLayout = findViewById(R.id.hint_layout);

        login = findViewById(R.id.login_offline);
        cancel = findViewById(R.id.cancel_login_offline);
        login.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == showAdvanceSetting){
            if (editUUIDLayout.getVisibility() == View.GONE){
                editUUIDLayout.setVisibility(View.VISIBLE);
                hintLayout.setVisibility(View.VISIBLE);
                Animation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(30);//设置动画持续时间
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
                animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                spinView.startAnimation(animation);
            }
            else {
                editUUIDLayout.setVisibility(View.GONE);
                hintLayout.setVisibility(View.GONE);
                Animation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(30);//设置动画持续时间
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
                animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                spinView.startAnimation(animation);
            }
        }
        if (v == login){

        }
        if (v == cancel){
            this.dismiss();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
