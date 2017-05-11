package com.cashbus.android.bamboo.activitys;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.http.HttpCookieCallBack;
import com.cashbus.android.bamboo.http.HttpTask;
import com.cashbus.android.bamboo.http.HttpUtils;
import com.cashbus.android.bamboo.modle.BasicResponse;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.utils.DialogUtils;
import com.cashbus.android.bamboo.utils.ExpandAnimation;
import com.cashbus.android.bamboo.utils.SettingUtils;
import com.cashbus.android.bamboo.views.ExtendClearEditText;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zenghui on 2017/5/9.
 */

public class SmsLoginActivity extends BasicActivity implements View.OnClickListener,TextWatcher{
    private ExtendClearEditText edtPhone;
    private ExtendClearEditText edtSms;
    private Button btnLogin,btnGetSms;
    private TextView tvAccountLogin;
    private LinearLayout phoneError,smsError;
    private ImageView imgBack;
    private String pictureCaptcha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_login);

        initView();
        initListener();

        long time = SettingUtils.get(mContext, Common.SENDSMS_TIME, 0l);
        long currentTime = System.currentTimeMillis();
        if ((currentTime - time)/1000 < 60){
            countDownTime(60 - (int) ((currentTime - time) / 1000));
        }
    }

    private void initListener() {

        edtPhone.addTextChangedListener(this);
        edtSms.addTextChangedListener(this);

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
//                    if (!Common.isPhoneValid(edtPhone.getText().toString().trim())){
//                        showAnimation(phoneError);
//                        edtPhone.setTextColor(Color.parseColor("#D31D00"));
//                        if (shakeAnimation == null){
//                            initEditTextShake(mContext);
//                        }
//                        shake(edtPhone);
//                    }else {
//                        if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() > 0){
//                            hiddenAnimation(phoneError);
//                        }
//                    }
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
//                    if (edtSms.getText().length() < 4){
//                        showAnimation(smsError);
//                        edtSms.setTextColor(Color.parseColor("#D31D00"));
//                        if (shakeAnimation == null){
//                            initEditTextShake(mContext);
//                        }
//                        shake(edtSms);
//                    }else {
//                        if (smsError.getVisibility() == View.VISIBLE && smsError.getHeight() > 0){
//                            hiddenAnimation(smsError);
//                        }
//                    }
                }
            }
        });


        btnLogin.setOnClickListener(this);
        btnGetSms.setOnClickListener(this);
        tvAccountLogin.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        edtSms.setOnClickListener(this);
        edtPhone.setOnClickListener(this);
    }

    private void initView() {

        edtPhone = (ExtendClearEditText) findViewById(R.id.edtPhone);
        edtSms = (ExtendClearEditText) findViewById(R.id.edtSms);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGetSms = (Button) findViewById(R.id.btnGetSms);
        tvAccountLogin = (TextView) findViewById(R.id.tvAccountLogin);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        phoneError = (LinearLayout) findViewById(R.id.phoneError);
        smsError = (LinearLayout) findViewById(R.id.smsError);

        smsError.setVisibility(View.GONE);
        phoneError.setVisibility(View.GONE);
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
            if (smsError.getVisibility() == View.VISIBLE && smsError.getHeight() == height){
                hiddenAnimation(smsError);
            }
            edtSms.setTextColor(Color.parseColor("#333333"));
        }else if (s == edtPhone.getEditableText()){
            if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() == height){
                hiddenAnimation(phoneError);
            }
            edtPhone.setTextColor(Color.parseColor("#333333"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAccountLogin:
                break;
            case R.id.btnGetSms:
                String phoneStr = edtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneStr) || !Common.isPhoneValid(phoneStr)){
                    showAnimation(phoneError);
                    edtPhone.setTextColor(Color.parseColor("#D31D00"));
                    if (shakeAnimation == null){
                        initEditTextShake(mContext);
                    }
                    shake(edtPhone);
                    return;
                }
                getSms();

                break;
            case R.id.btnLogin:

                String phone = edtPhone.getText().toString().trim();
                String sms = edtSms.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !Common.isPhoneValid(phone)){
                    showAnimation(phoneError);
                    edtPhone.setTextColor(Color.parseColor("#D31D00"));
                    if (shakeAnimation == null){
                        initEditTextShake(mContext);
                    }
                    shake(edtPhone);
                    return;
                }

                if (TextUtils.isEmpty(sms) || sms.length() < 4){
                    showAnimation(smsError);
                    edtSms.setTextColor(Color.parseColor("#D31D00"));
                    if (shakeAnimation == null){
                        initEditTextShake(mContext);
                    }
                    shake(edtSms);
                    return;
                }
                login();

                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.edtPhone:
                if (phoneError.getVisibility() == View.VISIBLE && phoneError.getHeight() == height){
                    hiddenAnimation(phoneError);
                }
                break;
            case R.id.edtSms:
                if (smsError.getVisibility() == View.VISIBLE && smsError.getHeight() == height){
                    hiddenAnimation(smsError);
                }
                break;
        }
    }
    RelativeLayout btnTest;

    void testS(){
        final TextView tv = (TextView) findViewById(R.id.tvLogin);
        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btnTest.getLayoutParams();
        ValueAnimator oa = ValueAnimator.ofInt(btnTest.getWidth(),btnTest.getHeight());
        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                lp.width = value;
                btnTest.requestLayout();
            }
        });
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tv.setText("");
                tv.setBackgroundResource(R.mipmap.loading);
                RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(true); // 设置保持动画最后的状态
                anim.setDuration(1000); // 设置动画时间
                anim.setRepeatCount(Integer.MAX_VALUE);
                tv.startAnimation(anim);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        oa.setDuration(500);
        oa.setInterpolator(new DecelerateInterpolator());
        oa.start();
    }

    void testL(){
        final TextView tv = (TextView) findViewById(R.id.tvLogin);
        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btnTest.getLayoutParams();
        ValueAnimator oa = ValueAnimator.ofInt(btnTest.getWidth(),Common.screem_width - getResources().getDimensionPixelSize(R.dimen.public_space_value_50));
        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                lp.width = value;
                btnTest.requestLayout();
            }
        });

        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tv.setText("登录");
                tv.getAnimation().cancel();
                tv.setBackground(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        oa.setDuration(500);
        oa.setInterpolator(new DecelerateInterpolator());
        oa.start();
    }

    private CountDownTimer timer;
    private void countDownTime(int count) {
        btnGetSms.setEnabled(false);
        timer = new CountDownTimer(count * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetSms.setText("重新获取" + (millisUntilFinished/1000) + "秒");
            }

            @Override
            public void onFinish() {
                btnGetSms.setEnabled(true);
                btnGetSms.setText("获取验证码");
            }
        };
        timer.start();
    }

    void getSms(){
        DialogUtils.showLoadingDialog(mContext,"获取验证码...");
        HttpTask httpTask = HttpUtils.getTask(Common.DOMAIN);
        Map<String,String> map = new HashMap<>();
        map.put("phone",edtPhone.getText().toString());
        if (!TextUtils.isEmpty(pictureCaptcha)) {
            map.put("pictureCaptcha", pictureCaptcha);
        }
        Call<BasicResponse> call = httpTask.getSms(map);
        call.enqueue(new HttpCookieCallBack<BasicResponse>(mContext){
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                super.onResponse(call, response);
                DialogUtils.dismissLoadDialog();
                BasicResponse b = response.body();
                if (b != null){
                    if ("0".equals(b.getCode())){
                        SettingUtils.set(mContext, Common.SENDSMS_TIME, System.currentTimeMillis());
                        countDownTime(60);
                        return;
                    }else if ("-1001".equals(b.getCode())){
                        showCaptchDialog(mContext,"");
                    }else if ("-1".equals(b.getCode())){
                        if (!TextUtils.isEmpty(b.getMsg())) {
                            DialogUtils.showTitleMultiDialog(mContext, "", b.getMsg(), "确定", "", 0, 0, false, null, null);
                        }else {
                            DialogUtils.showTitleMultiDialog(mContext, "", "数据异常", "确定", "", 0, 0, false, null, null);
                        }
                    }
                    pictureCaptcha = "";
                }else {
                    pictureCaptcha = "";
                }

            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                super.onFailure(call, t);
                DialogUtils.dismissLoadDialog();
            }
        });

    }

    public void showCaptchDialog(final Context con, String tip) {
        final Dialog mDialog = new Dialog(con, R.style.BambooDialog);
        mDialog.setContentView(R.layout.dialog_pic_captcha);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        View root = mDialog.findViewById(R.id.root);
        //宽为5/6
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        layoutParams.width = Common.screem_width * 5 / 6;
        root.setLayoutParams(layoutParams);

        final EditText captcha = (EditText) mDialog.findViewById(R.id.captcha);
        final ImageView imageCaptcha = (ImageView) mDialog.findViewById(R.id.imageCaptcha);
        if (!TextUtils.isEmpty(tip)){
            TextView tipTv = (TextView) mDialog.findViewById(R.id.tipTv);
            tipTv.setText(tip);
        }
        TextView captchaHint = (TextView) mDialog.findViewById(R.id.captchaHint);

        getPicCaptcha(imageCaptcha);

        Button btnOk = (Button) mDialog.findViewById(R.id.btnOk);
        btnOk.setText("确定");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = captcha.getText().toString().trim();
                if (TextUtils.isEmpty(st) || st.length() < 4){
                    showToast("请填写正确的验证码");
                    return;
                }
                mDialog.dismiss();
                pictureCaptcha = captcha.getText().toString().trim();
                getSms();
            }
        });

        captchaHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicCaptcha(imageCaptcha);
            }
        });

        imageCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicCaptcha(imageCaptcha);
            }
        });

        mDialog.show();

    }


    void getPicCaptcha(final ImageView imageCaptcha){
        DialogUtils.showLoadingDialog(mContext,"获取中...");
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj == null){
                    DialogUtils.showTitleMultiDialog(mContext, "", "图片验证码获取失败,请重试", "", "确定", 0, 0, false, null, null);
                }else {
                    imageCaptcha.setBackground(new BitmapDrawable((Bitmap) msg.obj));
                }
                DialogUtils.dismissLoadDialog();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = HttpUtils.getHttpBitmap(Common.DOMAIN+"/rest/root/user/createImage",true);
                Message message = handler.obtainMessage();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();
    }

    void login(){
        DialogUtils.showLoadingDialog(mContext,"登录中...");
        HttpTask httpTask = HttpUtils.getTask(Common.DOMAIN);
        Map<String,String> map = new HashMap<>();
        map.put("username",edtPhone.getText().toString().trim());
        map.put("phoneCaptcha",edtSms.getText().toString().trim());
        if (!TextUtils.isEmpty(pictureCaptcha)) {
            map.put("pictureCaptcha", pictureCaptcha);
        }
        Call<Map<String,String>> call = httpTask.appLogin(map);
        call.enqueue(new HttpCookieCallBack<Map<String,String>>(mContext){
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                super.onResponse(call, response);
                DialogUtils.dismissLoadDialog();
                Map<String,String> b = response.body();
                if (b != null){
                    if ("-1".equals(b.get("code"))){
                        if (b.get("msg") != null) {
                            DialogUtils.showTitleMultiDialog(mContext, "", (String) b.get("msg"), "", "确定", 0, 0, false, null, null);
                        }else {
                            DialogUtils.showTitleMultiDialog(mContext, "", "数据异常", "", "确定", 0, 0, false, null, null);
                        }
                    }else if ("0".equals(b.get("code"))){
                        SettingUtils.set(mContext,Common.HTTPREQUEST_USERPHONE,edtPhone.getText().toString().trim());
                        SettingUtils.set(mContext,Common.HTTPREQUEST_TOKEN,b.get("loginToken"));
                        Common.syncCookie(mContext);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                super.onFailure(call, t);
                DialogUtils.dismissLoadDialog();
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null ){
            timer.cancel();
        }
    }
}
