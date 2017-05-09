package com.cashbus.android.bamboo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cashbus.android.bamboo.activitys.WebviewActivity;
import com.cashbus.android.bamboo.utils.Common;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.utils.DialogUtils;

/**
 * Created by zenghui on 2017/5/5.
 */

public class FinancialFragment extends BasicFragment {

    View rootView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_financial, null);
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        initListener();
    }

    void initListener(){
        rootView.findViewById(R.id.zzztLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebviewActivity.class);
                intent.putExtra(Common.WEB_LINK,Common.DOMAIN+"/#!/product/weekly");
                intent.putExtra(Common.WEB_TITLE,"周周智投");
                startActivity(intent);
            }
        });
    }


}
