package com.cashbus.android.bamboo.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.fragments.ActivitysFragment;
import com.cashbus.android.bamboo.fragments.FinancialFragment;
import com.cashbus.android.bamboo.interfaces.BackInterface;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.utils.DialogUtils;

public class MainActivity extends BasicActivity implements View.OnClickListener {

    RadioButton imgOne,imgTwo,imgThree;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RadioGroup radioGroup;
    private TextView tvTitle;
    BackInterface backInterface;
    private int checkIndex = 0;
    public ImageView rightImg;
    public String  rightImgUrl;

    public void setBackInterface(BackInterface backInterface) {
        this.backInterface = backInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListener();
    }

    private void initListener() {
        imgOne.setOnClickListener(this);
        imgTwo.setOnClickListener(this);
        imgThree.setOnClickListener(this);
    }

    private void initViews() {


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("竹子理财");

        rightImg = (ImageView) findViewById(R.id.rightImg);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.mipmap.ic_product_news_n);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        imgOne = (RadioButton) findViewById(R.id.imgOne);
        imgTwo = (RadioButton) findViewById(R.id.imgTwo);
        imgThree = (RadioButton) findViewById(R.id.imgThree);

        checkIndex = R.id.imgOne;
        imgOne.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment mainFragment = new FinancialFragment();
        transaction.replace(R.id.contentView, mainFragment);
        transaction.commit();

        rightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(rightImgUrl)) {
                    Intent intent = new Intent(mContext, WebviewActivity.class);
                    intent.putExtra(Common.WEB_LINK, rightImgUrl);
                    intent.putExtra(Common.WEB_TITLE, "消息中心");
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
                case R.id.imgOne:
                    if (checkIndex == R.id.imgOne){
                        return;
                    }
                    imgOne.setChecked(true);
                    checkIndex = R.id.imgOne;
                    tvTitle.setText("竹子理财");
                    transaction = fragmentManager.beginTransaction();
                    Fragment financialFragment = new FinancialFragment();
                    transaction.replace(R.id.contentView, financialFragment);
                    transaction.commit();
                    break;
                case R.id.imgTwo:
                    if (checkIndex == R.id.imgTwo){
                        return;
                    }
                    imgTwo.setChecked(true);
                    checkIndex = R.id.imgTwo;
                    tvTitle.setText("活动");
                    transaction = fragmentManager.beginTransaction();
                    Fragment activitysFragment = new ActivitysFragment();
                    transaction.replace(R.id.contentView, activitysFragment);
                    transaction.commit();
                    break;
                case R.id.imgThree:
                    if (checkIndex == R.id.imgTwo){
                        imgTwo.setChecked(true);
                    }else {
                        imgOne.setChecked(true);
                    }
                    startActivity(new Intent(mContext,MyActivity.class));
                    break;

        }
    }

    //双击退出
    public void doubleClickExitApp() {
        if (Common.downTime == 0) {
            Common.downTime = System.currentTimeMillis();
            showToast("再点一次 退出应用");
            return;
        }
        long lastDownTime = System.currentTimeMillis();
        if ((lastDownTime - Common.downTime) > 1000) {
            Common.downTime = lastDownTime;
            showToast("再点一次 退出应用");
        } else {
            System.exit(0);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (backInterface != null){
                    backInterface.goBack();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (backInterface != null){
            if (!backInterface.goBack()){
                doubleClickExitApp();
            }
        }else {
            doubleClickExitApp();
        }
    }

}
