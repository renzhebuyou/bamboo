package com.cashbus.android.bamboo.views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.cashbus.android.bamboo.R;
import com.cashbus.android.bamboo.adapters.ImagePagerAdapter;
import com.cashbus.android.bamboo.modle.ImgInfo;
import com.cashbus.android.bamboo.utils.Common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 工具箱首页轮播图布局
 */
public class BannerViewpagerView extends LinearLayout implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    /**
     * 请求更新显示的View。
     */
    protected static final int MSG_UPDATE_IMAGE  = 1;
    /**
     * 请求暂停轮播。
     */
    protected static final int MSG_KEEP_SILENT   = 2;
    /**
     * 请求恢复轮播。
     */
    protected static final int MSG_BREAK_SILENT  = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    protected static final int MSG_PAGE_CHANGED  = 4;

    private static final long MSG_DELAY = 5000;

    public ViewPager mViewPager;
    protected LinearLayout mTabLy;
    protected Context mContext;
    private ImagePagerAdapter mAdapter;

    public BannerViewpagerView(Context context) {
        super(context);
        initView(context);
    }

    public BannerViewpagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public void initView(Context context) {
        mContext = context;
        int height= Common.screem_height;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams
                .MATCH_PARENT, height);
        setLayoutParams(params);
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_viewapger, null);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLy = (LinearLayout) view.findViewById(R.id.ad_small_tab_ly);
        mViewPager.setOnPageChangeListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(view, layoutParams);
        setViewPagerScrollSpeed();
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);

    }
    /**
     * 设置ViewPager的滑动速度
     * */
    private void setViewPagerScrollSpeed( ){
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller( mViewPager.getContext( ),new DecelerateInterpolator());
            scroller.setmDuration(500);
            mScroller.set( mViewPager, scroller);
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }
    }
    private int positionViewPager= 0;
    private int size;
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity activity = (Activity) mContext;
            if (activity==null){
                //Activity已经回收，无需再处理UI了
                return ;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (mHandler.hasMessages(MSG_UPDATE_IMAGE)){
                mHandler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what){
                case MSG_UPDATE_IMAGE:
                    positionViewPager++;
                    mViewPager.setCurrentItem(positionViewPager);
                    //准备下次播放
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    positionViewPager = msg.arg1;
                    break;
                default:
                    break;
            }
        }

    };

    List<Map<String,String>> resIds = new ArrayList<>();

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        if(size>=2){
            mHandler.sendMessage(Message.obtain(mHandler, MSG_PAGE_CHANGED, position, 0));
        }
//        if(size>0){
//            selected(position%size);
//        }
    }

    public int SCROLL_STATE = 0;

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                SCROLL_STATE = 1;
                if(size>=2){
                    mHandler.sendEmptyMessage(MSG_KEEP_SILENT);
                }
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                SCROLL_STATE = 0;
                if(size>=2){
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                }
                break;
            default:
                break;
        }
    }

    public void startLoop(){
        mHandler.sendEmptyMessage(MSG_BREAK_SILENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHandler.removeCallbacks(null);

        return super.onTouchEvent(event);
    }

    private void selected(int position) {
        for (int i = 0; i < mTabLy.getChildCount(); i++) {
            View imageView = mTabLy.getChildAt(i);
            if (i == position) {
                imageView.setBackgroundResource(R.drawable.dot_focused);
            } else {
                imageView.setBackgroundResource(R.drawable.dot_normal);
            }
        }
    }

    public void setData(List<Map<String,String>> resIds) {
        this.resIds = resIds;
        size = resIds.size();
        //添加第六项
        mTabLy.removeAllViews();

        if (resIds.size() > 1) {
            mTabLy.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
        } else {
            mTabLy.setVisibility(View.GONE);
        }
        mAdapter = new ImagePagerAdapter(resIds, mContext);
        mViewPager.setAdapter(mAdapter);
        addDynamicView();
        selected(0);
        mTabLy.setVisibility(GONE);
    }


        private void addDynamicView() {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.public_space_value_5),getResources().getDimensionPixelSize(R.dimen.public_space_value_5));
        layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.public_space_value_5);
        layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.public_space_value_5);
        for (int i = 0; i < resIds.size(); i++) {
            View dot = LayoutInflater.from(getContext()).inflate(R.layout.dot_layout, null);
            dot.setLayoutParams(layoutParams);
            dot.requestLayout();
            mTabLy.addView(dot);
        }
    }

}
