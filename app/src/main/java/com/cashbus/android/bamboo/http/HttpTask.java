package com.cashbus.android.bamboo.http;

import com.cashbus.android.bamboo.modle.BasicResponse;

import java.io.InputStream;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by zenghui on 2017/5/8.
 */

public interface HttpTask {

    @GET("/rest/root/user/createImage")
    Call<InputStream>  createImage();

    /**
     * code = ”0“ : 发送正常
     code = “-1001” :需要图片验证码
     code = “-1”， 短信发送失败，具体看msg
     */
    @POST("/rest/captcha/user/sms")
    Call<BasicResponse>  getSms(@Body Map<String,String> map);

    @POST("/rest/root/user/appRegister")
    Call<BasicResponse>  appRegister(@Body Map<String,String> map);

    @POST("/rest/root/user/appLogin")
    Call<Map<String,String>> appLogin(@Body Map<String,String> map);
}
