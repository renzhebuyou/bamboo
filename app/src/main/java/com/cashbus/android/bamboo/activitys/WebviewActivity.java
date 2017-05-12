package com.cashbus.android.bamboo.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
 * Created by zenghui on 2017/5/8.
 */

public class WebviewActivity extends BasicActivity implements HostJsInterface{
    WebView webView;
    Toolbar toolbar;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.title);
        tvTitle.setText(getIntent().getStringExtra(Common.WEB_TITLE));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(this,Common.JS_ALIAS);
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
                Log.d("","onPageStarted ====>"+url);

            }
        });

        Common.webSetting(webView);

        webView.loadUrl(getIntent().getStringExtra(Common.WEB_LINK));
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

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @JavascriptInterface
    @Override
    public void setToolbarStatus(String jsonString) {
        try {
            final JSONObject jsonObject = new JSONObject(jsonString);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toolbar != null &&  !isFinishing()){
                        TextView tvTitle = (TextView) toolbar.findViewById(R.id.title);
                        tvTitle.setText(jsonObject.optString("title"));
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
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void credentialError() {
        startActivity(new Intent(mContext,SmsLoginActivity.class));
        finish();
    }
}
