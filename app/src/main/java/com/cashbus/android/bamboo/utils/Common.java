package com.cashbus.android.bamboo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cashbus.android.bamboo.BuildConfig;
import com.cashbus.android.bamboo.http.HttpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zenghui on 2017/5/5.
 */

public class Common {
    public static String DOMAIN = "http://test.zhuzilc.com";//"http://192.168.1.33:9000";//"http://test.zhuzilc.com";//https://www.zhuzilc.com/#!/product/weekly
    public static String DOMAIN_ = "test.zhuzilc.com";//"test.zhuzilc.com";
    public static int screem_width;
    public static int screem_height;
    public static String WEBVIEW_PARAM_USER_AGENT = "com.cashbus.android.smallwallet.v"+ BuildConfig.VERSION_NAME+".micromessenger";
    public static String USER_AGENT = "com.cashbus.android.smallwallet.v"+ BuildConfig.VERSION_NAME;
    public static long downTime;
    public static String WEB_LINK = "webview_link";
    public static String WEB_TITLE = "webview_title";
    public static String HTTPREQUEST_TOKEN = "token";
    public static String HTTPREQUEST_COOKIE = "Cookie";
    public static String HTTPREQUEST_USERPHONE = "userPhone";
    private static String PHONE_PATTERN ="^[1][3,4,7,5,8][0-9]{9}$";
    public static String SENDSMS_TIME = "sendsms_time";
    public static String JS_ALIAS = "zzApp";


    public static void webSetting(final WebView webView){
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString(Common.WEBVIEW_PARAM_USER_AGENT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        webView.getSettings().setBlockNetworkImage(false);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setFocusable(true);
        webView.setClickable(true);
        webView.setHapticFeedbackEnabled(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!webView.hasFocus()) {
                            v.requestFocusFromTouch();
                        }
                        break;
                }
                return false;
            }
        });
    }

    //生成圆角图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int round) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, round, round, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 校验电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneValid(String phone) {

        if (TextUtils.isEmpty(phone)){
            return false;
        }

        Pattern pattern = Pattern.compile(Common.PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    //将cookie同步到webview 以便H5能取到手机号跟cbtk
    public static void syncCookie(Context context){

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(Common.DOMAIN, "username="+SettingUtils.get(context, Common.HTTPREQUEST_USERPHONE, ""));
        cookieManager.setCookie(Common.DOMAIN, "jsessionid="+ HttpUtils.JSESSIONID);//cookies是在HttpClient中获得的cookie
        cookieManager.setCookie(Common.DOMAIN, "serverid="+HttpUtils.SERVERID);
        CookieSyncManager.getInstance().sync();

    }
    
}
