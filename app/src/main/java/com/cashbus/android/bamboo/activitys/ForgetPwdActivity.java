package com.cashbus.android.bamboo.activitys;

import android.app.Service;
import android.content.Context;
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

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.utils.ExpandAnimation;
import com.cashbus.android.bamboo.views.ExtendClearEditText;

/**
 * Created by zenghui on 2017/5/9.
 */

public class ForgetPwdActivity extends BasicActivity implements TextWatcher{

    private ExtendClearEditText edtPhone,edtSms,edtNewPwd;
    private LinearLayout phoneError,smsError,newPwdError;
    private Button btnGetSms;
    private CheckBox cbEye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        initView();

        initListener();
    }

    private void initListener() {
        edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
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
            public void onFocusChange(View view, boolean b) {
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

        edtNewPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    if (edtNewPwd.getText().length() > 0)
                        edtNewPwd.setClearIconVisible(true);
                    edtNewPwd.setTextColor(Color.parseColor("#333333"));
                    if (newPwdError.getVisibility() == View.VISIBLE && newPwdError.getHeight() > 0){
                        hiddenAnimation(newPwdError);
                    }
                }else {
                    edtNewPwd.setClearIconVisible(false);
                    if (edtNewPwd.getText().length() < 4){
                        showAnimation(newPwdError);
                        edtNewPwd.setTextColor(Color.parseColor("#D31D00"));
                        if (shakeAnimation == null){
                            initEditTextShake(mContext);
                        }
                        shake(edtNewPwd);
                    }else {
                        if (newPwdError.getVisibility() == View.VISIBLE && newPwdError.getHeight() > 0){
                            hiddenAnimation(newPwdError);
                        }
                    }
                }
            }
        });

        edtPhone.addTextChangedListener(this);
        edtSms.addTextChangedListener(this);
        edtNewPwd.addTextChangedListener(this);



    }

    private void initView() {

        edtPhone = (ExtendClearEditText) findViewById(R.id.edtPhone);
        edtSms = (ExtendClearEditText) findViewById(R.id.edtSms);
        edtNewPwd = (ExtendClearEditText) findViewById(R.id.edtNewPwd);
        phoneError = (LinearLayout) findViewById(R.id.phoneError);
        smsError = (LinearLayout) findViewById(R.id.smsError);
        newPwdError = (LinearLayout) findViewById(R.id.newPwdError);
        btnGetSms = (Button) findViewById(R.id.btnGetSms);
        cbEye = (CheckBox) findViewById(R.id.cbEye);

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


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
            if (s == edtSms.getEditableText()){
                if (smsError.getVisibility() == View.VISIBLE && smsError.getHeight() > 0){
                    hiddenAnimation(smsError);
                }
            }else if (s == edtPhone.getEditableText()){
                if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() > 0){
                    hiddenAnimation(phoneError);
                }
            }else if (s == edtNewPwd.getEditableText()){
                if (newPwdError.getVisibility() == View.VISIBLE && newPwdError.getHeight() > 0){
                    hiddenAnimation(newPwdError);
                }
            }
    }
}
