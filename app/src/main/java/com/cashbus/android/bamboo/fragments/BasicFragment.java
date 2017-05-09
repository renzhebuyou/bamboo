package com.cashbus.android.bamboo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.webkit.JavascriptInterface;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.activitys.MainActivity;
import com.cashbus.android.bamboo.interfaces.HostJsInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zenghui on 2017/5/5.
 */

public class BasicFragment extends Fragment implements HostJsInterface {
    Toolbar toolbar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @JavascriptInterface
    @Override
    public void setToolbarStatus(String jsonString) {
        try {
            final JSONObject jsonObject = new JSONObject(jsonString);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toolbar != null && getActivity() != null && !getActivity().isFinishing()){
                        TextView tvTitle = (TextView) toolbar.findViewById(R.id.title);
                        tvTitle.setText(jsonObject.optString("title"));
                        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(jsonObject.optBoolean("isNeedBack"));
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
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void credentialError() {

    }
}
