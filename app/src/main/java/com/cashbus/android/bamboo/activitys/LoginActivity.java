package com.cashbus.android.bamboo.activitys;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
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
import com.cashbus.android.bamboo.http.HttpCookieCallBack;
import com.cashbus.android.bamboo.http.HttpTask;
import com.cashbus.android.bamboo.http.HttpUtils;
import com.cashbus.android.bamboo.modle.BasicResponse;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.utils.DialogUtils;
import com.cashbus.android.bamboo.utils.ExpandAnimation;
import com.cashbus.android.bamboo.views.ExtendClearEditText;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zenghui on 2017/5/9.
 */

public class LoginActivity extends BasicActivity implements View.OnClickListener{

    private ExtendClearEditText edtPhone;
    private ExtendClearEditText edtPwd;
    private TextView tvForgetPwd;
    private CheckBox ckRemindPwd;
    private Button btnLogin;
    private TextView tvSmsLogin;
    private LinearLayout registerLayout,phoneError,pwdError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initListener();
    }

    private void initViews() {

        edtPhone = (ExtendClearEditText) findViewById(R.id.edtPhone);
        edtPwd = (ExtendClearEditText) findViewById(R.id.edtPwd);
        tvForgetPwd = (TextView) findViewById(R.id.tvForgetPwd);
        ckRemindPwd = (CheckBox) findViewById(R.id.ckRemindPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvSmsLogin = (TextView) findViewById(R.id.tvSmsLogin);
        registerLayout = (LinearLayout) findViewById(R.id.registerLayout);

        phoneError = (LinearLayout) findViewById(R.id.phoneError);
        pwdError = (LinearLayout) findViewById(R.id.pwdError);

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

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() > 0){
                    hiddenAnimation(phoneError);
                }
            }
        });


        edtPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
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

        edtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pwdError.getVisibility() == View.VISIBLE && pwdError.getHeight() > 0){
                    hiddenAnimation(pwdError);
                }
            }
        });

        btnLogin.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String phone = edtPhone.getText().toString().trim();
                String pwd = edtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !Common.isPhoneValid(phone)){
                    showAnimation(phoneError);
                    edtPhone.setTextColor(Color.parseColor("#D31D00"));
                    if (shakeAnimation == null){
                        initEditTextShake(mContext);
                    }
                    shake(edtPhone);
                    return;
                }

                if (TextUtils.isEmpty(pwd) || pwd.length() < 6){
                    showAnimation(pwdError);
                    edtPwd.setTextColor(Color.parseColor("#D31D00"));
                    if (shakeAnimation == null){
                        initEditTextShake(mContext);
                    }
                    shake(edtPwd);
                    return;
                }

                login();

                break;
            case R.id.registerLayout:
                break;
            case R.id.tvForgetPwd:
                break;
            case R.id.tvSmsLogin:
                break;
        }
    }

    void login(){
        DialogUtils.showLoadingDialog(mContext,"登录中...");
        HttpTask httpTask = HttpUtils.getTask(Common.DOMAIN);
        Map<String,String> map = new HashMap<>();
        map.put("username",edtPhone.getText().toString().trim());
        map.put("password",edtPwd.getText().toString().trim());
        Call<BasicResponse> call = httpTask.appLogin(map);
        call.enqueue(new HttpCookieCallBack<BasicResponse>(mContext){
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                super.onResponse(call, response);
                DialogUtils.dismissLoadDialog();
                BasicResponse b = response.body();
                if (b != null){
                    if ("-1".equals(b.getCode())){
                        if (!TextUtils.isEmpty(b.getMsg())) {
                            DialogUtils.showTitleMultiDialog(mContext, "", b.getMsg(), "", "确定", 0, 0, false, null, null);
                        }else {
                            DialogUtils.showTitleMultiDialog(mContext, "", "数据异常", "", "确定", 0, 0, false, null, null);
                        }
                    }else if ("0".equals(b.getCode())){

                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                super.onFailure(call, t);
                DialogUtils.dismissLoadDialog();
            }
        });
    }

}
