package com.cashbus.android.bamboo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.activitys.WebviewActivity;
import com.cashbus.android.bamboo.interfaces.HandleDialogListener;
import com.cashbus.android.bamboo.utils.Common;
import com.cashbus.android.bamboo.views.TouchLinearLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghui on 2017/5/12.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.PictureViewHolder>  {

    private Context context;
    private List<Map<String,String>> mDatas;

    public MessageAdapter(Context context, List<Map<String,String>> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public MessageAdapter.PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PictureViewHolder viewHolder ;
        View view = LayoutInflater.from(context).inflate(R.layout.item_activitys, parent, false);
        viewHolder = new PictureViewHolder(view);
        return viewHolder;
    }

    public void setImageUrl(final Context context, final String imageUrl, final ImageView imageView){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                if (imageUrl.equals((String) imageView.getTag())) {
                    imageView.setImageDrawable((Drawable) msg.obj);
                }
            }
        };


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Picasso picasso = Picasso.with(context);
                    picasso.setLoggingEnabled(true);

                    RequestCreator requestCreator = picasso.load(imageUrl);
                    Bitmap bitmap = null;
                    try {
                        bitmap = requestCreator.get();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    android.os.Message message = handler.obtainMessage();
                    message.obj = bitmapDrawable;
                    handler.sendMessage(message);
                }
            }).start();
    }

    @Override
    public void onBindViewHolder(MessageAdapter.PictureViewHolder holder, final int position) {

        holder.layout.setHandleDialogListener(new HandleDialogListener() {
            @Override
            public void handle() {
                Toast.makeText(context,"position =>"+position,Toast.LENGTH_SHORT).show();
            }
        });
        final Map<String,String> map = mDatas.get(position);
        holder.img.setTag(map.get("imageUrl"));
        setImageUrl(context,map.get("imageUrl"),holder.img);
        holder.leftTv.setText(map.get("title"));
        holder.rightTv.setText(map.get("detail"));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.img.getLayoutParams();
        layoutParams.height = (int) ((Common.screem_width - context.getResources().getDimensionPixelSize(R.dimen.public_space_value_20))*0.4);
        holder.img.requestLayout();

        holder.layout.setHandleDialogListener(new HandleDialogListener() {
            @Override
            public void handle() {
                Intent intent = new Intent(context,WebviewActivity.class);
                intent.putExtra(Common.WEB_LINK,map.get("url"));
                intent.putExtra(Common.WEB_TITLE,map.get("title"));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {

        TouchLinearLayout layout;
        TextView leftTv,rightTv;
        ImageView img;

        public PictureViewHolder(View itemView) {
            super(itemView);
            this.layout = (TouchLinearLayout) itemView.findViewById(R.id.layout);
            this.leftTv = (TextView) itemView.findViewById(R.id.leftTv);
            this.rightTv = (TextView) itemView.findViewById(R.id.rightTv);
            this.img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
