package com.cashbus.android.bamboo.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cashbus.android.bamboo.modle.BasicResponse;
import com.cashbus.android.bamboo.utils.DialogUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zenghui on 2017/5/8.
 */

public class HttpCookieCallBack<T> implements Callback<T>{

    Context context;
    public BasicResponse basicResponse;
    public HttpCookieCallBack(Context context){
        this.context = context;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.errorBody() != null && response.errorBody().byteStream() != null ) {
            basicResponse = convertStreamToCodeInfo(response.errorBody().byteStream());
        }
        setCookies(context,response,basicResponse);
    }

    public void setCookies(Context mContext,retrofit2.Response response,BasicResponse basicResponse){

        //保存cookies
        List<String> headers = response.raw().headers().values("Set-Cookie");
        for (String string:headers){
            Log.d("Set-Cookie ===>",string);
            String split = string.split(";")[0];
            if (string.contains("JSESSIONID") && !HttpUtils.JSESSIONID.equals(split)){
                HttpUtils.JSESSIONID = split;
            }else if (string.contains("SERVERID") && !HttpUtils.SERVERID.equals(split)){
                HttpUtils.SERVERID = split;
            }else if (string.contains("SESSION") && !HttpUtils.SESSION.equals(split)){
                HttpUtils.SESSION = split;
            }
        }
        if (response.code() != 200 && !TextUtils.isEmpty(response.raw().headers().get("CB_REQID"))) {
            String CB_REQID = response.raw().headers().get("CB_REQID");
            if (response.errorBody() != null) {
                if (basicResponse != null) {
                    if (TextUtils.isEmpty(basicResponse.getMsg())) {
                        if (CB_REQID.length() >= 8) {
                            DialogUtils.showTitleMultiDialog(mContext,"", "错误ID:"+CB_REQID.substring(0, 8),"确定","",0,0,false,null,null);
                        } else {
                            DialogUtils.showTitleMultiDialog(mContext,"","错误ID:"+CB_REQID,"确定","",0,0,false,null,null);
                        }
                    } else {
                        if (CB_REQID.length() >= 8) {
                            if ("OK不一致".equalsIgnoreCase(basicResponse.getMsg())){
                                DialogUtils.showTitleMultiDialog(mContext,"银行卡姓名和本人姓名不一致" , "错误ID:"+CB_REQID.substring(0, 8),"确定","",0,0,false,null,null);
                            }else {
                                DialogUtils.showTitleMultiDialog(mContext, basicResponse.getMsg(), "错误ID:" + CB_REQID.substring(0, 8), "确定", "", 0, 0, false, null, null);
                            }
                        } else {
                            DialogUtils.showTitleMultiDialog(mContext, basicResponse.getMsg(),"错误ID:"+ CB_REQID,"确定","",0,0,false,null,null);
                        }
                    }
                } else {
                    if (CB_REQID.length() >= 8) {
                        DialogUtils.showTitleMultiDialog(mContext,"", "错误ID:"+CB_REQID.substring(0, 8),"确定","",0,0,false,null,null);
                    } else {
                        DialogUtils.showTitleMultiDialog(mContext,"","错误ID:"+CB_REQID,"确定","",0,0,false,null,null);
                    }
                }
            }else {
                if (CB_REQID.length() >= 8) {
                    DialogUtils.showTitleMultiDialog(mContext,"", "错误ID:"+CB_REQID.substring(0, 8),"确定","",0,0,false,null,null);
                } else {
                    DialogUtils.showTitleMultiDialog(mContext,"","错误ID:"+CB_REQID,"确定","",0,0,false,null,null);
                }
            }
        }else if (basicResponse != null){
            if (!TextUtils.isEmpty(basicResponse.getMsg())) {
                DialogUtils.showTitleMultiDialog(mContext, "", basicResponse.getMsg(), "确定", "", 0, 0, false, null, null);
            }else {
                DialogUtils.showTitleMultiDialog(mContext, "", "内部错误", "确定", "", 0, 0, false, null, null);
            }
        }
    }

    public BasicResponse convertStreamToCodeInfo(InputStream is){
        if (is == null){
            return null;
        }
        try {
            BasicResponse basicResponse = new Gson().fromJson(convertStreamToString(is),BasicResponse.class);
            return basicResponse;
        }catch (Exception e){
        }
        return null;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
