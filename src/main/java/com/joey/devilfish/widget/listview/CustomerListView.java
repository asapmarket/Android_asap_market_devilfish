package com.joey.devilfish.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 文件描述
 * Date: 2017/8/8
 *
 * @author xusheng
 */

public class CustomerListView extends ListView {

    public CustomerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setFocusable(false);
    }

    public CustomerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFocusable(false);
    }

    public CustomerListView(Context context) {
        super(context);
        this.setFocusable(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}
