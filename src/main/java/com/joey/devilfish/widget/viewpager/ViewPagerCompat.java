package com.joey.devilfish.widget.viewpager;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class ViewPagerCompat extends ViewPager {

    public ViewPagerCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 在mViewTouchMode为true或者滑动方向不是左右的时候，ViewPager将放弃控制点击事件，
     * 这样做有利于在ViewPager中加入ListView等可以滑动的控件，否则两者之间的滑动将会有冲突
     */
    @Override
    public boolean arrowScroll(int direction) {
        if (direction != FOCUS_LEFT && direction != FOCUS_RIGHT) {
            return false;
        }
        return super.arrowScroll(direction);
    }

}
