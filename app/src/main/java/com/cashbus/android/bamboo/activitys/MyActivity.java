package com.cashbus.android.bamboo.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.interfaces.HostJsInterface;
import com.cashbus.android.bamboo.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zenghui on 2017/5/6.
 */

public class MyActivity extends BasicActivity implements HostJsInterface {
    WebView webView;
    Toolbar toolbar;

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("我的");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webView);

        webView.addJavascriptInterface(MyActivity.this,Common.JS_ALIAS);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("","onPageStarted ====>"+url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("","shouldOverrideUrlLoading ====>"+url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("","onPageFinished ====>"+url);
                if (url.contains("/user/login")){
                    startActivity(new Intent(mContext,SmsLoginActivity.class));
                    finish();
                }
            }
        });

        Common.webSetting(webView);

        webView.loadUrl(Common.DOMAIN+"/#!/my");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView != null && webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @JavascriptInterface
    @Override
    public void setToolbarStatus(String jsonString) {
        try {
            final JSONObject jsonObject = new JSONObject(jsonString);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toolbar != null && !isFinishing()){
                        TextView tvTitle = (TextView) toolbar.findViewById(R.id.title);
                        if (!TextUtils.isEmpty(jsonObject.optString("title"))) {
                            tvTitle.setText(jsonObject.optString("title"));
                        }else {
                            tvTitle.setText("竹子理财");
                        }
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    @Override
    public void alert(String s) {
        if (!TextUtils.isEmpty(s)) {
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        }
    }

    @JavascriptInterface
    @Override
    public void credentialError() {
        showToast("登录");
        startActivity(new Intent(mContext,SmsLoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}

