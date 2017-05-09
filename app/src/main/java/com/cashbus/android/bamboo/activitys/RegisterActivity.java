package com.cashbus.android.bamboo.activitys;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.utils.ExpandAnimation;
import com.cashbus.android.bamboo.views.ExtendClearEditText;

/**
 * Created by zenghui on 2017/5/9.
 */

public class RegisterActivity extends BasicActivity implements View.OnClickListener,TextWatcher{

    private ExtendClearEditText edtPhone,edtSms,edtPwd;
    private Button btnGetSms,btnRegister;
    private CheckBox cbEye;
    private TextView tvLogin;
    private LinearLayout pwdError,smsError,phoneError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initListener();

    }

    private void initListener() {

        edtPhone.addTextChangedListener(this);
        edtSms.addTextChangedListener(this);
        edtPwd.addTextChangedListener(this);

        edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if (b){
                    if (edtPhone.getText().length() > 0)
                        edtPhone.setClearIconVisible(true);
                    edtPhone.setTextColor(Color.parseColor("#333333"));
                    if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() > 0){
                        hiddenAnimation(phoneError);
                    }
                }else {
                    edtPhone.setClearIconVisible(false);
                    if (!Common.isPhoneValid(edtPhone.getText().toString().trim())){
                        showAnimation(phoneError);
                        edtPhone.setTextColor(Color.parseColor("#D31D00"));
                        if (shakeAnimation == null){
                            initEditTextShake(mContext);
                        }
                        shake(edtPhone);
                    }else {
                        if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() > 0){
                            hiddenAnimation(phoneError);
                        }
                    }
                }
            }
        });


        edtSms.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if (b){
                    if (edtSms.getText().length() > 0)
                        edtSms.setClearIconVisible(true);
                    edtSms.setTextColor(Color.parseColor("#333333"));
                    if (smsError.getVisibility() == View.VISIBLE && smsError.getHeight() > 0){
                        hiddenAnimation(smsError);
                    }
                }else {
                    edtSms.setClearIconVisible(false);
                    if (edtSms.getText().length() < 4){
                        showAnimation(smsError);
                        edtSms.setTextColor(Color.parseColor("#D31D00"));
                        if (shakeAnimation == null){
                            initEditTextShake(mContext);
                        }
                        shake(edtSms);
                    }else {
                        if (smsError.getVisibility() == View.VISIBLE && smsError.getHeight() > 0){
                            hiddenAnimation(smsError);
                        }
                    }
                }
            }
        });


        edtPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if (b){
                    if (edtPwd.getText().length() > 0)
                        edtPwd.setClearIconVisible(true);
                    edtPwd.setTextColor(Color.parseColor("#333333"));
                    if (pwdError.getVisibility() == View.VISIBLE && pwdError.getHeight() > 0){
                        hiddenAnimation(pwdError);
                    }
                }else {
                    edtPwd.setClearIconVisible(false);
                    if (edtPwd.getText().length() < 6){
                        showAnimation(pwdError);
                        edtPwd.setTextColor(Color.parseColor("#D31D00"));
                        if (shakeAnimation == null){
                            initEditTextShake(mContext);
                        }
                        shake(edtPwd);
                    }else {
                        if (pwdError.getVisibility() == View.VISIBLE && pwdError.getHeight() > 0){
                            hiddenAnimation(pwdError);
                        }
                    }
                }
            }
        });


        tvLogin.setOnClickListener(this);
        btnGetSms.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    private void initView() {

        edtPhone = (ExtendClearEditText) findViewById(R.id.edtPhone);
        edtSms = (ExtendClearEditText) findViewById(R.id.edtSms);
        edtPwd = (ExtendClearEditText) findViewById(R.id.edtPwd);
        btnGetSms = (Button) findViewById(R.id.btnGetSms);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        cbEye = (CheckBox) findViewById(R.id.cbEye);
        tvLogin = (Button) findViewById(R.id.tvLogin);
        pwdError = (LinearLayout) findViewById(R.id.pwdError);
        smsError = (LinearLayout) findViewById(R.id.smsError);
        phoneError = (LinearLayout) findViewById(R.id.phoneError);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvLogin:
                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
                break;
            case R.id.btnGetSms:
                break;
            case R.id.btnRegister:
                break;
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
        if (s == edtPwd.getEditableText()){
            if (pwdError.getVisibility() == View.VISIBLE && pwdError.getHeight() > 0){
                hiddenAnimation(pwdError);
            }
        }else  if (s == edtSms.getEditableText()){
            if (smsError.getVisibility() == View.VISIBLE && smsError.getHeight() > 0){
                hiddenAnimation(smsError);
            }
        }else  if (s == edtPhone.getEditableText()){
            if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() > 0){
                hiddenAnimation(phoneError);
            }
        }
    }

    private int height = 0;

    void showAnimation(View view){
        view.setVisibility(View.VISIBLE);
        if (height == 0)
            height = getResources().getDimensionPixelSize(R.dimen.public_space_value_24);
        if (view.getHeight() == height){
            return;
        }
        ExpandAnimation expandAnimation = new ExpandAnimation(0,height,view);
        expandAnimation.setDuration(500);
        expandAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(expandAnimation);
    }

    void hiddenAnimation(View view){
        if (height == 0)
            height = getResources().getDimensionPixelSize(R.dimen.public_space_value_24);
        ExpandAnimation expandAnimation = new ExpandAnimation(height,0,view);
        expandAnimation.setDuration(500);
        expandAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(expandAnimation);
    }

    private Animation shakeAnimation;
    private CycleInterpolator cycleInterpolator;
    private Vibrator shakeVibrator;

    public void initEditTextShake(Context context) {
        shakeVibrator = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
        shakeAnimation.setDuration(300);
        cycleInterpolator = new CycleInterpolator(8);
        shakeAnimation.setInterpolator(cycleInterpolator);

    }

    public void shake(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.startAnimation(shakeAnimation);
        }
        shakeVibrator.vibrate(new long[]{0, 500}, -1);
    }



}
