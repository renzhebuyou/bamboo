package com.cashbus.android.bamboo.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.cashbus.android.bamboo.BuildConfig;
import com.cashbus.android.bamboo.utils.Common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zenghui on 2017/5/8.
 */

public class HttpUtils {

    public static String JSESSIONID = "";
    public static String SERVERID = "";
    public static String SESSION = "";

    // okhttp 请求类 默认选择这个请求类
    public static HttpTask getTask(final String apiUrl) {
        //设置网络报文log打印
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = null;

        try {
            //绕过证书认证
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] x509Certificates = new X509Certificate[0];
                            return x509Certificates;
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            httpClient = new OkHttpClient().newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request;
                            if (!TextUtils.isEmpty(JSESSIONID) || !TextUtils.isEmpty(SERVERID)) {
                                StringBuilder value = new StringBuilder();
                                if (!TextUtils.isEmpty(JSESSIONID)) {
                                    value.append(JSESSIONID);
                                    if (!TextUtils.isEmpty(SERVERID)) {
                                        value.append(";" + SERVERID);
                                    }
                                } else {
                                    value.append(SERVERID);
                                    if (!TextUtils.isEmpty(JSESSIONID)) {
                                        value.append(";" + JSESSIONID);
                                    }
                                }
                                if (!TextUtils.isEmpty(SESSION)) {
                                    value.append(";" + SESSION);
                                }

                                request = chain.request()
                                        .newBuilder()
                                        .addHeader(Common.HTTPREQUEST_COOKIE, value.toString())
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            } else {
                                request = chain.request()
                                        .newBuilder()
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            }

                            Response originalResponse = chain.proceed(request);
                            return originalResponse;
                        }
                    })
                    .connectTimeout(120, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        } catch (Exception e) {
            httpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request;
                            if (!TextUtils.isEmpty(JSESSIONID) || !TextUtils.isEmpty(SERVERID)) {
                                StringBuilder value = new StringBuilder();
                                if (!TextUtils.isEmpty(JSESSIONID)) {
                                    value.append(JSESSIONID);
                                    if (!TextUtils.isEmpty(SERVERID)) {
                                        value.append(";" + SERVERID);
                                    }
                                } else {
                                    value.append(SERVERID);
                                    if (!TextUtils.isEmpty(JSESSIONID)) {
                                        value.append(";" + JSESSIONID);
                                    }
                                }
                                if (!TextUtils.isEmpty(SESSION)) {
                                    value.append(";" + SESSION);
                                }

                                request = chain.request()
                                        .newBuilder()
                                        .addHeader(Common.HTTPREQUEST_COOKIE, value.toString())
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            } else {
                                request = chain.request()
                                        .newBuilder()
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            }

                            Response originalResponse = chain.proceed(request);
                            return originalResponse;
                        }
                    })
                    .connectTimeout(120, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();


        HttpTask task = retrofit.create(HttpTask.class);

        return task;
    }



    // okhttp 请求类 默认选择这个请求类
    public static HttpTask getTask(String apiUrl, final String token) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(Common.HTTPREQUEST_TOKEN + "=%s", token));
        stringBuilder.append(String.format("; domain=%s", Common.DOMAIN_));
        stringBuilder.append(String.format("; path=%s", "/"));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
        OkHttpClient httpClient = null;
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] x509Certificates = new X509Certificate[0];
                            return x509Certificates;
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request;

                            if (!TextUtils.isEmpty( JSESSIONID) || !TextUtils.isEmpty( SERVERID)) {

                                StringBuilder value = stringBuilder;
                                if (!TextUtils.isEmpty( JSESSIONID)) {
                                    value.append(";" + JSESSIONID);
                                    if (!TextUtils.isEmpty(SERVERID)) {
                                        value.append(";" + SERVERID);
                                    }
                                } else {
                                    value.append(";" + SERVERID);
                                    if (!TextUtils.isEmpty(JSESSIONID)) {
                                        value.append(";" + JSESSIONID);
                                    }
                                }

                                request = chain.request()
                                        .newBuilder()
                                        .addHeader(Common.HTTPREQUEST_COOKIE, value.toString())
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            } else {
                                request = chain.request()
                                        .newBuilder()
                                        .addHeader(Common.HTTPREQUEST_COOKIE, stringBuilder.toString())
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            }

                            Response originalResponse = chain.proceed(request);
                            return originalResponse;
                        }

                    })
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
                    .addInterceptor(interceptor)
                    .build();
        }catch (Exception e){
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request;

                            if (!TextUtils.isEmpty( JSESSIONID) || !TextUtils.isEmpty( SERVERID)) {

                                StringBuilder value = stringBuilder;
                                if (!TextUtils.isEmpty( JSESSIONID)) {
                                    value.append(";" + JSESSIONID);
                                    if (!TextUtils.isEmpty(SERVERID)) {
                                        value.append(";" + SERVERID);
                                    }
                                } else {
                                    value.append(";" + SERVERID);
                                    if (!TextUtils.isEmpty(JSESSIONID)) {
                                        value.append(";" + JSESSIONID);
                                    }
                                }

                                request = chain.request()
                                        .newBuilder()
                                        .addHeader(Common.HTTPREQUEST_COOKIE, value.toString())
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            } else {
                                request = chain.request()
                                        .newBuilder()
                                        .addHeader(Common.HTTPREQUEST_COOKIE, stringBuilder.toString())
                                        .addHeader("User-Agent", Common.USER_AGENT)
                                        .addHeader("content-type", "application/json;charset=UTF-8")
                                        .build();
                            }

                            Response originalResponse = chain.proceed(request);
                            return originalResponse;
                        }

                    })
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
                    .addInterceptor(interceptor)
                    .build();
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build();
        HttpTask task = retrofit.create(HttpTask.class);
        return task;
    }


    public static Bitmap getHttpBitmap(String url, boolean needSession){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            if (!TextUtils.isEmpty(JSESSIONID)) {
                conn.setRequestProperty("Cookie", JSESSIONID);
                conn.setRequestProperty("Cookie", SERVERID);
            }
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            List<String> responseCookie = conn.getHeaderFields().get("Set-Cookie");
            if (needSession & responseCookie != null ){

                for (int i = 0; i < responseCookie.size();i++){
                    if (responseCookie.get(i).contains("JSESSIONID")){
                        JSESSIONID = responseCookie.get(i);
                    }else if (responseCookie.get(i).contains("SERVERID")){
                        SERVERID = responseCookie.get(i);
                    }
                }

            }
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
    
}
