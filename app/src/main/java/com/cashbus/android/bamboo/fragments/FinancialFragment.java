package com.cashbus.android.bamboo.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cashbus.android.bamboo.activitys.WebviewActivity;
import com.cashbus.android.bamboo.modle.ImgInfo;
import com.cashbus.android.bamboo.utils.Common;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.utils.DialogUtils;
import com.cashbus.android.bamboo.views.BannerViewpagerView;
import com.cashbus.android.bamboo.views.LinearGradientLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zenghui on 2017/5/5.
 */

public class FinancialFragment extends BasicFragment implements View.OnClickListener{

    View rootView;
    public SwipeRefreshLayout swipeRefreshLayout;
    BannerViewpagerView bannerViewpagerView;
    LinearGradientLayout zzGradient,newUserGradient;
    LinearLayout zzztLayout,newUserLayout;

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
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        bannerViewpagerView = (BannerViewpagerView) rootView.findViewById(R.id.bannerViewpagerView);
        zzztLayout = (LinearLayout) rootView.findViewById(R.id.zzztLayout);
        newUserLayout = (LinearLayout) rootView.findViewById(R.id.newUserLayout);

        yearProfitTvNew = (TextView) rootView.findViewById(R.id.yearProfitTvNew);
        yearProfitTvZZZT = (TextView) rootView.findViewById(R.id.yearProfitTvZZZT);
        dayTvNew = (TextView) rootView.findViewById(R.id.dayTvNew);
        dayTvZzzt = (TextView) rootView.findViewById(R.id.dayTvZzzt);

        newUserGradient = (LinearGradientLayout) rootView.findViewById(R.id.newUserGradient);
        newUserGradient.setLinearGradientColor(new int[] { 0XFFFE8E03,0XFFF86F19 });

        zzGradient = (LinearGradientLayout) rootView.findViewById(R.id.zzGradient);
        zzGradient.setLinearGradientColor(new int[] { 0XFF429321,0XFFB4ED50 });

        List<ImgInfo> adList = new ArrayList<>();
        ImgInfo imgInfo = new ImgInfo();
        imgInfo.setResId(R.mipmap.img_banner_688_n);
        adList.add(imgInfo);

        imgInfo = new ImgInfo();
        imgInfo.setResId(R.mipmap.img_banner_friend_n);
        adList.add(imgInfo);

        imgInfo = new ImgInfo();
        imgInfo.setResId(R.mipmap.img_banner_safe_n);
        adList.add(imgInfo);

        bannerViewpagerView.setData(adList);
        //预先设置100个轮播缓存
        for (int i = 0 ; i < 100 ; i ++){
            bannerViewpagerView.mViewPager.setCurrentItem(i);
        }

        initListener();
    }

    void initListener(){

        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#e7a123"), Color.parseColor("#171123"),
                Color.parseColor("#671aa1"), Color.parseColor("#aaa333"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },500);
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

        zzztLayout.setOnClickListener(this);
        newUserLayout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newUserLayout:
                Intent intent = new Intent(getActivity(),WebviewActivity.class);
                intent.putExtra(Common.WEB_LINK,Common.DOMAIN+Common.LINK_NEWER);
                intent.putExtra(Common.WEB_TITLE,"新手福利");
                startActivity(intent);
                break;
            case R.id.zzztLayout:
                Intent intentz = new Intent(getActivity(),WebviewActivity.class);
                intentz.putExtra(Common.WEB_LINK,Common.DOMAIN+Common.LINK_WEEKLY);
                intentz.putExtra(Common.WEB_TITLE,"周周智投");
                startActivity(intentz);
                break;
        }
    }
}
