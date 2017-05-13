package com.cashbus.android.bamboo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.activitys.WebviewActivity;
import com.cashbus.android.bamboo.http.HttpUtils;
import com.cashbus.android.bamboo.interfaces.HandleDialogListener;
import com.cashbus.android.bamboo.interfaces.HandleObjectListener;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zenghui on 2017/5/8.
 */

public class DialogUtils {

    private static boolean hasTake = false;
    private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private static ExecutorService executorService = Executors.newCachedThreadPool();
//    public static synchronized void showTitleMultiDialog(final Context con, String content, String confirmBtnPrompt,
//                                           String cancelBtnPrompt, int confirmColor, int cancelColor, final boolean isCancelable, final HandleDialogListener handleConfirm,
//                                           final HandleDialogListener handleCancel) {
//        final Dialog mDialog = new Dialog(con, R.style.BambooDialog);
//        mDialog.setContentView(R.layout.common_multi_dialog);
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setCancelable(isCancelable);
//        TextView tvTitle = (TextView) mDialog.findViewById(R.id.title);
//        tvTitle.setText(content);
//        TextView tvContent = (TextView) mDialog.findViewById(R.id.content);
//        tvContent.setVisibility(View.GONE);
//
//        Button mDialogCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
//        if (cancelColor != 0) {
//            mDialogCancel.setTextColor(con.getResources().getColor(cancelColor));
//        }
//        if (!TextUtils.isEmpty(cancelBtnPrompt)) {
//            mDialogCancel.setText(cancelBtnPrompt);
//        } else {
//            mDialog.findViewById(R.id.mutiLayout).setVisibility(View.GONE);
//            Button btnOk = (Button) mDialog.findViewById(R.id.btnOk);
//            btnOk.setVisibility(View.VISIBLE);
//            if (confirmColor != 0) {
//                btnOk.setTextColor(con.getResources().getColor(confirmColor));
//            }
//            btnOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (handleConfirm != null) {
//                        handleConfirm.handle();
//                    }
//                    mDialog.dismiss();
//                }
//            });
//        }
//        mDialogCancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                if (handleCancel != null) {
//                    handleCancel.handle();
//                }
//            }
//        });
//        Button mDialogOK = (Button) mDialog.findViewById(R.id.btn_ok);
//        if (confirmColor != 0) {
//            mDialogOK.setTextColor(con.getResources().getColor(confirmColor));
//        }
//        if (!TextUtils.isEmpty(confirmBtnPrompt)) {
//            mDialogOK.setText(confirmBtnPrompt);
//        }
//        mDialogOK.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                if (handleConfirm != null) {
//                    handleConfirm.handle();
//                }
//
//            }
//        });
//        mDialog.show();
//    }

    public static synchronized void showTitleMultiDialog(final Context con, String title, String content, String confirmBtnPrompt,
                                                         String cancelBtnPrompt, int confirmColor, int cancelColor, final boolean isCancelable, final HandleDialogListener handleConfirm,
                                                         final HandleDialogListener handleCancel) {
        final Dialog mDialog = new Dialog(con, R.style.BambooDialog);
        mDialog.setContentView(R.layout.common_multi_dialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(isCancelable);
        TextView tvTitle = (TextView) mDialog.findViewById(R.id.title);
        TextView tvContent = (TextView) mDialog.findViewById(R.id.content);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvContent.setText(content);
        } else {
            tvTitle.setText(content);
            tvContent.setVisibility(View.GONE);
        }

        Button mDialogCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        if (cancelColor != 0) {
            mDialogCancel.setTextColor(con.getResources().getColor(cancelColor));
        }
        if (!TextUtils.isEmpty(cancelBtnPrompt)) {
            mDialogCancel.setText(cancelBtnPrompt);
        } else {
            mDialog.findViewById(R.id.mutiLayout).setVisibility(View.GONE);
            Button btnOk = (Button) mDialog.findViewById(R.id.btnOk);
            btnOk.setVisibility(View.VISIBLE);
            if (confirmColor != 0) {
                btnOk.setTextColor(con.getResources().getColor(confirmColor));
            }
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if (handleConfirm != null) {
                        handleConfirm.handle();
                    }
                }
            });
        }
        mDialogCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (handleCancel != null) {
                    handleCancel.handle();
                }
            }
        });
        Button mDialogOK = (Button) mDialog.findViewById(R.id.btn_ok);
        if (confirmColor != 0) {
            mDialogOK.setTextColor(con.getResources().getColor(confirmColor));
        }
        if (!TextUtils.isEmpty(confirmBtnPrompt)) {
            mDialogOK.setText(confirmBtnPrompt);
        }
        mDialogOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (handleConfirm != null) {
                    handleConfirm.handle();
                }

            }
        });

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.show();
            }
        };
        try {
            if(con != null  && !((Activity)con).isFinishing()) {
                queue.add(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                });
                if (!hasTake) {
                    hasTake = true;
                    executorService.execute(queue.take());
                }
            }

        }catch (Exception e){
            if(con != null  && !((Activity)con).isFinishing()) {
                queue.add(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                });
                try {
                    if (!hasTake) {
                        hasTake = true;
                        executorService.execute(queue.take());
                    }
                } catch (InterruptedException e1) {
                    hasTake = false;
                    e1.printStackTrace();
                }
            }
        }

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (queue.size() > 0){
                    try {
                        executorService.execute(queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        hasTake = false;
                    }
                }else {
                    hasTake = false;
                }
            }
        });
    }

    public static void showPromotion(final Context con, final Map<String, Object> target) {
        final Dialog mDialog = new Dialog(con, R.style.BambooDialog);
        mDialog.setContentView(R.layout.advertisement_layout);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);

        View root = mDialog.findViewById(R.id.root);
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        layoutParams.width = Common.screem_width;
        layoutParams.height = Common.screem_height;
        root.setLayoutParams(layoutParams);

        final ImageView image = (ImageView) mDialog.findViewById(R.id.image);
        ImageView close = (ImageView) mDialog.findViewById(R.id.close);
        image.setImageDrawable(new BitmapDrawable(Common.getRoundedCornerBitmap(BitmapFactory.decodeResource(con.getResources(), R.mipmap.img_ad_loading_n)
                , con.getResources().getDimensionPixelSize(R.dimen.public_space_value_6))));
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bitmap source = (Bitmap) msg.obj;
                if (source == null) {
                    Toast.makeText(con, "加载失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Paint paint = new Paint();
                paint.setAntiAlias(true);
                int min = con.getResources().getDimensionPixelSize(R.dimen.public_space_value_6);
                Bitmap target = Bitmap.createBitmap(con.getResources().getDimensionPixelSize(R.dimen.public_space_value_300), con.getResources().getDimensionPixelSize(R.dimen.public_space_value_400), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(target);
                RectF rect = new RectF(0, 0, con.getResources().getDimensionPixelSize(R.dimen.public_space_value_300), con.getResources().getDimensionPixelSize(R.dimen.public_space_value_400));
                canvas.drawRoundRect(rect, min, min, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                final Rect src = new Rect(0, 0, source.getWidth(), source.getHeight());
                canvas.drawBitmap(source, src, rect, paint);
                image.setImageDrawable(new BitmapDrawable(target));
                if (source != null && !source.isRecycled()){
                    source.recycle();
                    source = null;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.obj = HttpUtils.getHttpBitmap((String) target.get("activityImage"), false);
                handler.sendMessage(message);

            }
        }).start();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(con, WebviewActivity.class);
                Bundle bundle = new Bundle();
                String url = (String) target.get("activityUrl");
                if (!url.startsWith("http")) {
                    url = Common.DOMAIN + url;
                }
                bundle.putString(Common.WEB_LINK, url);
                bundle.putString(Common.WEB_TITLE, (String) target.get("title"));
                intent.putExtras(bundle);
                con.startActivity(intent);
            }
        });

        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.umeng_socialize_dialog_animations);  //添加动画

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();

//        HttpTask iTask = HttpUtils.getTask(Common.DOMAIN,SettingUtils.get(con, Common.HTTPREQUEST_TOKEN, ""));
//        Map<String,String> map = new HashMap<>();
//        map.put("type","activity");
//        map.put("targetNo",(String)target.get("refId"));
//        Call<BasicResponse> call = iTask.postLog(map);
//        call.enqueue(new HttpCookieCallBack<BasicResponse>(con){
//            @Override
//            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
//                super.onResponse(call, response);
//                Log.d("","response ===>"+response.code());
//            }
//
//            @Override
//            public void onFailure(Call<BasicResponse> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });

    }

    private static Dialog mDialog;
    public static void showLoadingDialog(Context con, String msg) {
        mDialog = new Dialog(con, R.style.BambooDialog);
        mDialog.setContentView(R.layout.dialog_loading);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);

        ImageView img = (ImageView) mDialog.findViewById(R.id.img);
        TextView describeTv = (TextView) mDialog.findViewById(R.id.describeTv);

        describeTv.setText(msg);
        RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(1000); // 设置动画时间
        anim.setRepeatCount(Integer.MAX_VALUE);
        img.startAnimation(anim);

        mDialog.show();

    }

    public static void dismissLoadDialog(){
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

}
