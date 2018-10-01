package com.joey.devilfish.widget.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 文件描述
 * Date: 2017/8/4
 *
 * @author xusheng
 */

public class CustomerGridView extends GridView {

    public CustomerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerGridView(Context context) {
        super(context);
    }

    public CustomerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
