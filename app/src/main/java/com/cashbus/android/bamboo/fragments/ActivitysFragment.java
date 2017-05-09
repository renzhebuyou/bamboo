package com.cashbus.android.bamboo.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.activitys.MainActivity;
import com.cashbus.android.bamboo.activitys.MyActivity;
import com.cashbus.android.bamboo.interfaces.BackInterface;
import com.cashbus.android.bamboo.utils.Common;

/**
 * Created by zenghui on 2017/5/5.
 */

public class ActivitysFragment extends BasicFragment{

    View rootView;
    WebView webView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_activitys, null);
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (WebView) rootView.findViewById(R.id.webView);

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
        ((MainActivity)getActivity()).setBackInterface(new BackInterface() {
            @Override
            public boolean goBack() {
                if (webView != null && webView.canGoBack()){
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });

        Common.webSetting(webView);

        webView.loadUrl(Common.DOMAIN+"/#!/product/newer");
    }

}
