package com.joey.devilfish.widget.indicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 文件描述
 * Date: 2018/1/23
 *
 * @author xusheng
 */

public class ChildViewPager extends ViewPager {

    // 父ViewPager的引用
    private ViewPagerCompat mViewPager;

    private boolean mFlag = true;

    private float mLastMotionX;

    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerCompat getViewPager() {
        return mViewPager;
    }

    /**
     * 处理前必须调用此方法初始化冲突ViewPager
     *
     * @param viewPager
     */
    public void setViewPager(ViewPagerCompat viewPager) {
        this.mViewPager = viewPager;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 使父控件不处理任何触摸事件
                mViewPager.requestDisallowInterceptTouchEvent(true);
                mFlag = true;
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mFlag) {
                    if (x - mLastMotionX > 5 && getCurrentItem() == 0) {
                        mFlag = false;

                        // 将事件交由父控件处理
                        mViewPager.requestDisallowInterceptTouchEvent(false);
                    }
                    if (x - mLastMotionX < -5 && getCurrentItem() == getAdapter().getCount() - 1) {
                        mFlag = false;
                        mViewPager.requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mViewPager.requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                mViewPager.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}