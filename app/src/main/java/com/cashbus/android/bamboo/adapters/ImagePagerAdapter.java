package com.cashbus.android.bamboo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.activitys.WebviewActivity;
import com.cashbus.android.bamboo.modle.ImgInfo;
import com.cashbus.android.bamboo.utils.Common;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ImagePagerAdapter extends PagerAdapter {
    private List<Map<String,String>> images;
    private Context mContext;

    public ImagePagerAdapter(List<Map<String,String>> images, Context context) {
        this.images = images;
        this.mContext = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return images.size()==1?1: Integer.MAX_VALUE;
        //return images.size();
    }
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = LayoutInflater.from(mContext).inflate(R.layout.item_pager_image, view, false);
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

//        imageView.setImageResource(images.get(position%images.size()).getResId());
//        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
        if(images.size()>0){

            if (mImageLoader == null){

                File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils.getOwnCacheDirectory(mContext, "imageloader/Cache");

                DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                        .cacheInMemory(true).cacheOnDisc(true).build();

                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).defaultDisplayImageOptions(defaultOptions)
                        .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                        .memoryCacheSize(12 * 1024 * 1024)
                        .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                        .discCache(new UnlimitedDiscCache(cacheDir))
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .tasksProcessingOrder(QueueProcessingType.LIFO).build();

                ImageLoader.getInstance().init(config);


                // 获取图片加载实例
                mImageLoader = ImageLoader.getInstance();
                options = new DisplayImageOptions.Builder()
                        .showStubImage(R.mipmap.top_banner_android)
                        .showImageForEmptyUri(R.mipmap.top_banner_android)
                        .showImageOnFail(R.mipmap.top_banner_android)
                        .cacheInMemory(true).cacheOnDisc(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .imageScaleType(ImageScaleType.EXACTLY).build();


            }

            // 异步加载图片
            mImageLoader.displayImage(images.get(position%images.size()).get("imageUrl"), imageView, options);

        }

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,WebviewActivity.class);
                intent.putExtra(Common.WEB_LINK,images.get(position%images.size()).get("url"));
                intent.putExtra(Common.WEB_TITLE,images.get(position%images.size()).get("title"));
                mContext.startActivity(intent);
            }
        });
        view.addView(imageLayout, 0);
        return imageLayout;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}