package com.cashbus.android.bamboo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.adapters.MessageAdapter;
import com.cashbus.android.bamboo.http.HttpCookieCallBack;
import com.cashbus.android.bamboo.http.HttpTask;
import com.cashbus.android.bamboo.http.HttpUtils;
import com.cashbus.android.bamboo.interfaces.HorizontalDividerItemDecoration;
import com.cashbus.android.bamboo.pullrefresh.OnRefreshListener;
import com.cashbus.android.bamboo.pullrefresh.PullToRefreshLayout;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.utils.DialogUtils;
import com.cashbus.android.bamboo.utils.SettingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zenghui on 2017/5/5.
 */

public class ActivitysFragment extends BasicFragment{

    View rootView;
    private RecyclerView mRecyclerView;
    private  MessageAdapter messageAdapter;
    private List<Map<String ,String>> mDatas;
    private PullToRefreshLayout pullToRefreshLayout;
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

        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pullToRefreshLayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        pullToRefreshLayout.setRecyclerView(mRecyclerView);

        mDatas = new ArrayList<>();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(android.R.color.transparent).build());


        pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivitys(false);
            }
        });
        getActivitys(true);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgNewer.getLayoutParams();
//        layoutParams.height = (int) ((Common.screem_width - getResources().getDimensionPixelSize(R.dimen.public_space_value_20))*0.4);
//        imgNewer.requestLayout();
//
//        layoutParams = (LinearLayout.LayoutParams) imgInvite.getLayoutParams();
//        layoutParams.height = (int) ((Common.screem_width - getResources().getDimensionPixelSize(R.dimen.public_space_value_20))*0.4);
//        imgInvite.requestLayout();

//        webView = (WebView) rootView.findViewById(R.id.webView);
//
//        webView.addJavascriptInterface(this,Common.JS_ALIAS);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                Log.d("","onPageStarted ====>"+url);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.d("","shouldOverrideUrlLoading ====>"+url);
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Log.d("","onPageStarted ====>"+url);
//            }
//        });
//        ((MainActivity)getActivity()).setBackInterface(new BackInterface() {
//            @Override
//            public boolean goBack() {
//                if (webView != null && webView.canGoBack()){
//                    webView.goBack();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        Common.webSetting(webView);
//
//        webView.loadUrl(Common.DOMAIN+"/#!/product/newer");
    }

    void getActivitys(final boolean needDialog){
        if (needDialog){
            DialogUtils.showLoadingDialog(getActivity(),"获取中...");
        }
        HttpTask httpTask = HttpUtils.getTask(Common.DOMAIN, SettingUtils.get(getActivity(),Common.HTTPREQUEST_TOKEN,""));
        Call<Map<String, List<Map<String,String>>>> call = httpTask.getActivityList();
        call.enqueue(new HttpCookieCallBack<Map<String, List<Map<String,String>>>>(getActivity()){
            @Override
            public void onResponse(Call<Map<String, List<Map<String,String>>>> call, Response<Map<String, List<Map<String,String>>>> response) {
                super.onResponse(call, response);
                if (needDialog){
                    DialogUtils.dismissLoadDialog();
                }else {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
                }

                if (response.body() != null) {
                    mDatas = response.body().get("activityList");
                    messageAdapter = new MessageAdapter(getActivity(), mDatas);
                    mRecyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Map<String,String>>>> call, Throwable t) {
                super.onFailure(call, t);
                if (needDialog){
                    DialogUtils.dismissLoadDialog();
                }else {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
                }
            }
        });
    }

}
