package com.cashbus.android.bamboo.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.activitys.MainActivity;
import com.cashbus.android.bamboo.activitys.NewerCourseActivity;
import com.cashbus.android.bamboo.activitys.WebviewActivity;
import com.cashbus.android.bamboo.http.HttpCookieCallBack;
import com.cashbus.android.bamboo.http.HttpTask;
import com.cashbus.android.bamboo.http.HttpUtils;
import com.cashbus.android.bamboo.interfaces.HandleDialogListener;
import com.cashbus.android.bamboo.interfaces.ScrollViewListener;
import com.cashbus.android.bamboo.modle.ImgInfo;
import com.cashbus.android.bamboo.pullrefresh.OnRefreshListener;
import com.cashbus.android.bamboo.pullrefresh.PullToRefreshLayout;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.utils.DialogUtils;
import com.cashbus.android.bamboo.utils.SettingUtils;
import com.cashbus.android.bamboo.views.BannerViewpagerView;
import com.cashbus.android.bamboo.views.CircleProgressView;
import com.cashbus.android.bamboo.views.LinearGradientLayout;
import com.cashbus.android.bamboo.views.ObservableScrollView;
import com.cashbus.android.bamboo.views.TouchLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zenghui on 2017/5/5.
 */

public class FinancialFragment extends BasicFragment implements View.OnClickListener{

    View rootView;
    BannerViewpagerView bannerViewpagerView;
    LinearGradientLayout zzGradient,newUserGradient;
    TouchLinearLayout zzztLayout,newUserLayout,newerLayout;
    ObservableScrollView scrollView;
    PullToRefreshLayout pullToRefreshLayout;

    private TextView yearProfitTvZZZT,yearProfitTvNew,dayTvNew,dayTvZzzt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_financial, null);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pullToRefreshLayout);
        scrollView = (ObservableScrollView) rootView.findViewById(R.id.scrollView);
        pullToRefreshLayout.setScrollView(scrollView);
        bannerViewpagerView = (BannerViewpagerView) rootView.findViewById(R.id.bannerViewpagerView);
        zzztLayout = (TouchLinearLayout) rootView.findViewById(R.id.zzztLayout);
        newUserLayout = (TouchLinearLayout) rootView.findViewById(R.id.newUserLayout);
        newerLayout = (TouchLinearLayout) rootView.findViewById(R.id.newerLayout);

        yearProfitTvNew = (TextView) rootView.findViewById(R.id.yearProfitTvNew);
        yearProfitTvZZZT = (TextView) rootView.findViewById(R.id.yearProfitTvZZZT);
        dayTvNew = (TextView) rootView.findViewById(R.id.dayTvNew);
        dayTvZzzt = (TextView) rootView.findViewById(R.id.dayTvZzzt);

        newUserGradient = (LinearGradientLayout) rootView.findViewById(R.id.newUserGradient);
        newUserGradient.setLinearGradientColor(new int[] { 0XFFF86F19,0XFFFE8E03 });

        zzGradient = (LinearGradientLayout) rootView.findViewById(R.id.zzGradient);
        zzGradient.setLinearGradientColor(new int[] { 0XFF429321,0XFFB4ED50 });


        initListener();
        getHomePage(true);

    }

    void getHomePage(final boolean needDialog){
        if (needDialog){
            DialogUtils.showLoadingDialog(getActivity(),"加载中...");
        }
        HttpTask task = HttpUtils.getTask(Common.DOMAIN, SettingUtils.get(getActivity(),Common.HTTPREQUEST_TOKEN,""));
        Call<Map<String,Object>> call = task.getHomePage();
        call.enqueue(new HttpCookieCallBack<Map<String,Object>>(getActivity()){
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                super.onResponse(call, response);
                final Map<String,Object> map = response.body();
                if (map != null){
                    List<Map<String,String>> adList = (List<Map<String, String>>) map.get("banner");
                    bannerViewpagerView.setData(adList);
                    //预先设置100个轮播缓存
                    for (int i = 0 ; i < 100 ; i ++){
                        bannerViewpagerView.mViewPager.setCurrentItem(i);
                    }
                    bannerViewpagerView.startLoop();
                    final List<Map<String,Object>> detail = (List<Map<String, Object>>) map.get("detail");
                    ((TextView)rootView.findViewById(R.id.newerTvRight)).setText((String)detail.get(0).get("detail"));
                    String minRate = ""+(detail.get(0).get("minRate"));
                    if (minRate.contains(".")){
                        minRate = minRate.substring(0,minRate.indexOf("."));
                    }
                    yearProfitTvNew.setText(minRate);

                    ((TextView)rootView.findViewById(R.id.zzztTvRight)).setText((String)detail.get(1).get("detail"));
                    String maxRate = ""+(detail.get(1).get("maxRate"));
                    minRate = ""+(detail.get(1).get("minRate"));

                        if (minRate.contains(".")){
                            minRate = minRate.substring(0,minRate.indexOf("."));
                        }
                    if (!TextUtils.isEmpty(maxRate)) {

                        if (maxRate.contains(".")){
                            maxRate = maxRate.substring(0,maxRate.indexOf("."));
                        }

                        yearProfitTvZZZT.setText(minRate);
                        ((TextView)rootView.findViewById(R.id.yearProfitTvZZZT1)).setText(maxRate);
                    }else {
                        yearProfitTvZZZT.setText(minRate);
                    }

                    CircleProgressView newerCircleProgressView = (CircleProgressView) rootView.findViewById(R.id.newerCircleProgressView);
                    newerCircleProgressView.setProgress((int) ((Double) detail.get(0).get("progress") * 100));

                    CircleProgressView zzztCircleProgressView = (CircleProgressView) rootView.findViewById(R.id.zzztCircleProgressView);
                    zzztCircleProgressView.setProgress((int) ((Double) detail.get(1).get("progress") * 100));

                    TextView unitTvZzzt = (TextView) rootView.findViewById(R.id.unitTvZzzt);
                    unitTvZzzt.setText((String)detail.get(1).get("durationUnit"));

                    TextView unitTvNew = (TextView) rootView.findViewById(R.id.unitTvNew);
                    unitTvNew.setText((String)detail.get(0).get("durationUnit"));


                    TextView dayTvNew = (TextView) rootView.findViewById(R.id.dayTvNew);
                    dayTvNew.setText((String)detail.get(0).get("duration"));

                    TextView dayTvZzzt = (TextView) rootView.findViewById(R.id.dayTvZzzt);
                    dayTvZzzt.setText((String)detail.get(0).get("duration"));


                    zzztLayout.setHandleDialogListener(new HandleDialogListener() {
                        @Override
                        public void handle() {
                            Intent intentz = new Intent(getActivity(),WebviewActivity.class);
                            intentz.putExtra(Common.WEB_LINK,(String) detail.get(1).get("url"));
                            intentz.putExtra(Common.WEB_TITLE,"周周智投");
                            startActivity(intentz);
                        }
                    });

                    newUserLayout.setHandleDialogListener(new HandleDialogListener() {
                        @Override
                        public void handle() {
                            Intent intent = new Intent(getActivity(),WebviewActivity.class);
                            intent.putExtra(Common.WEB_LINK,(String) detail.get(0).get("url"));
                            intent.putExtra(Common.WEB_TITLE,"新手福利");
                            startActivity(intent);
                        }
                    });
                    ((MainActivity)getActivity()).rightImgUrl = (String) map.get("notiUrl");

                }
                if (needDialog){
                    DialogUtils.dismissLoadDialog();
                }else {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
                }
            }

            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                super.onFailure(call, t);
                if (needDialog){
                    DialogUtils.dismissLoadDialog();
                }else {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
                }

            }
        });
    }


    void initListener(){
        pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomePage(false);
            }
        });
        rootView.findViewById(R.id.zzztLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebviewActivity.class);
                intent.putExtra(Common.WEB_LINK,Common.DOMAIN+"/#!/product/weekly");
                intent.putExtra(Common.WEB_TITLE,"周周智投");
                startActivity(intent);
            }
        });

        yearProfitTvNew.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                yearProfitTvNew.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                View newerLayout1 = rootView.findViewById(R.id.newerLayout1);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) newerLayout1.getLayoutParams();
                layoutParams.height = yearProfitTvNew.getMeasuredHeight();
                newerLayout1.requestLayout();

                Paint.FontMetrics fontMetrics = yearProfitTvNew.getPaint().getFontMetrics();
                LinearLayout.LayoutParams linearLp = (LinearLayout.LayoutParams) yearProfitTvNew.getLayoutParams();
                linearLp.height = yearProfitTvNew.getMeasuredHeight();
                linearLp.topMargin = (int) fontMetrics.bottom/2;
                yearProfitTvNew.requestLayout();


                View dayNewLayout = rootView.findViewById(R.id.dayNewLayout);
                layoutParams = (RelativeLayout.LayoutParams) dayNewLayout.getLayoutParams();
                layoutParams.height = yearProfitTvNew.getMeasuredHeight();
                dayNewLayout.requestLayout();

                int height = (rootView.findViewById(R.id.relativeLayout1)).getMeasuredHeight();
                int bottomMargin = height /2 - yearProfitTvNew.getMeasuredHeight() / 2-getResources().getDimensionPixelSize(R.dimen.public_space_value_5) - rootView.findViewById(R.id.newerTv1).getMeasuredHeight();

                TextView dayNewerRight = (TextView) rootView.findViewById(R.id.dayNewerRight);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) dayNewerRight.getLayoutParams();
                layoutParams1.bottomMargin = bottomMargin;
                dayNewerRight.requestLayout();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
                    }
                },50);

            }
        });

        rootView.findViewById(R.id.yearProfitTvZZZT1).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.findViewById(R.id.yearProfitTvZZZT1).getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

//        dayTvNew.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                dayTvNew.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                View dayNewLayout = rootView.findViewById(R.id.dayNewLayout);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dayNewLayout.getLayoutParams();
//                layoutParams.height = yearProfitTvNew.getMeasuredHeight();
//                dayNewLayout.requestLayout();
//
//                Paint.FontMetrics fontMetrics = dayTvNew.getPaint().getFontMetrics();
//                LinearLayout.LayoutParams linearLp = (LinearLayout.LayoutParams) dayTvNew.getLayoutParams();
//                linearLp.height = dayTvNew.getMeasuredHeight();
//                linearLp.bottomMargin = (int) (fontMetrics.bottom)/2;
//                dayTvNew.requestLayout();
//
//            }
//        });

        yearProfitTvZZZT.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                yearProfitTvZZZT.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                View newerLayout1 = rootView.findViewById(R.id.zzztLayout1);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) newerLayout1.getLayoutParams();
                layoutParams.height = yearProfitTvZZZT.getMeasuredHeight();
                newerLayout1.requestLayout();

                Paint.FontMetrics fontMetrics = yearProfitTvZZZT.getPaint().getFontMetrics();
                LinearLayout.LayoutParams linearLp = (LinearLayout.LayoutParams) yearProfitTvZZZT.getLayoutParams();
                linearLp.height = yearProfitTvZZZT.getMeasuredHeight();
                linearLp.topMargin = (int) fontMetrics.bottom/2;
                yearProfitTvZZZT.requestLayout();

               TextView yearProfitTvZZZT1 = (TextView) rootView.findViewById(R.id.yearProfitTvZZZT1);
                linearLp = (LinearLayout.LayoutParams) yearProfitTvZZZT1.getLayoutParams();
                linearLp.height = yearProfitTvZZZT1.getMeasuredHeight();
                linearLp.topMargin = (int) fontMetrics.bottom/2;
                yearProfitTvZZZT1.requestLayout();

                View dayZzztLayout = rootView.findViewById(R.id.dayZzztLayout);
                layoutParams = (RelativeLayout.LayoutParams) dayZzztLayout.getLayoutParams();
                layoutParams.height = yearProfitTvZZZT.getMeasuredHeight();
                dayZzztLayout.requestLayout();

                int height = (rootView.findViewById(R.id.relativeLayout2)).getMeasuredHeight();

                int bottomMargin = height /2 - yearProfitTvZZZT.getMeasuredHeight() / 2-getResources().getDimensionPixelSize(R.dimen.public_space_value_5) - rootView.findViewById(R.id.newerTv1).getMeasuredHeight();

                TextView dayZzztRight = (TextView) rootView.findViewById(R.id.dayzzztRight);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) dayZzztRight.getLayoutParams();
                layoutParams1.bottomMargin = bottomMargin;
                dayZzztRight.requestLayout();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rootView.findViewById(R.id.layout2).setVisibility(View.VISIBLE);
                    }
                },50);
            }
        });

        zzztLayout.setHandleDialogListener(new HandleDialogListener() {
            @Override
            public void handle() {
                Intent intentz = new Intent(getActivity(),WebviewActivity.class);
                intentz.putExtra(Common.WEB_LINK,Common.DOMAIN+Common.LINK_WEEKLY);
                intentz.putExtra(Common.WEB_TITLE,"周周智投");
                startActivity(intentz);
            }
        });

        newUserLayout.setHandleDialogListener(new HandleDialogListener() {
            @Override
            public void handle() {
                Intent intent = new Intent(getActivity(),WebviewActivity.class);
                intent.putExtra(Common.WEB_LINK,Common.DOMAIN+Common.LINK_NEWER);
                intent.putExtra(Common.WEB_TITLE,"新手福利");
                startActivity(intent);
            }
        });

        newerLayout.setHandleDialogListener(new HandleDialogListener() {
            @Override
            public void handle() {
                startActivity(new Intent(getActivity(), NewerCourseActivity.class));
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
