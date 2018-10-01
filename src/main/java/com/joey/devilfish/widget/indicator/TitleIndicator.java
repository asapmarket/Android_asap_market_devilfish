/*
 * @author http://blog.csdn.net/singwhatiwanna
 */
package com.joey.devilfish.widget.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joey.devilfish.R;
import com.joey.devilfish.model.tab.TabInfo;

import java.util.List;


/**
 * 这是个选项卡式的控件，会随着viewpager的滑动而滑动
 */
public class TitleIndicator extends LinearLayout implements View.OnClickListener,
        OnFocusChangeListener {
    private int mCurrentScroll = 0;

    //选项卡列表
    private List<TabInfo> mTabs;

    //选项卡所依赖的viewpager
    private ViewPager mViewPager;

    private Paint mPaintFooterTriangle;

    //当前选项卡的下标，从0开始
    private int mSelectedTab = 0;

    private Context mContext;

    //单个选项卡的宽度
    private int mPerItemWidth = 0;

    //表示选项卡总共有几个
    private int mTotal = 0;

    private LayoutInflater mInflater;


    /**
     * Default constructor
     */
    public TitleIndicator(Context context) {
        super(context);
        initDraw(context.getResources().getDimensionPixelSize(R.dimen.dimen_2),
                getResources().getColor(R.color.app_color));
    }

    /**
     * The contructor used with an inflater
     *
     * @param context
     * @param attrs
     */
    public TitleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setOnFocusChangeListener(this);
        mContext = context;

        initDraw(context.getResources().getDimensionPixelSize(R.dimen.dimen_2),
                getResources().getColor(R.color.app_color));
    }

    /**
     * Initialize draw objects
     */
    private void initDraw(float footerLineHeight, int footerColor) {
        mPaintFooterTriangle = new Paint();
        mPaintFooterTriangle.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintFooterTriangle.setColor(footerColor);
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * @这个是核心函数，选项卡是用canvas画出来的。所有的invalidate方法均会触发onDraw
     * 大意是这样的：当页面滚动的时候，会有一个滚动距离，然后onDraw被触发后，
     * 就会在新位置重新画上滚动条（其实就是画线）
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //下面是计算本次滑动的距离
        float scroll_x = 0;
        if (mTotal != 0) {
            mPerItemWidth = getWidth() / mTotal;
            int tabId = mSelectedTab;
            scroll_x = (mCurrentScroll - ((tabId) * (getWidth() + mViewPager.getPageMargin()))) / mTotal;
        } else {
            mPerItemWidth = getWidth();
            scroll_x = mCurrentScroll;
        }
        //下面就是如何画线了
        Path path = new Path();
        path.rewind();
        float offset = 0;
        float left_x = mSelectedTab * mPerItemWidth + offset + scroll_x;
        float right_x = (mSelectedTab + 1) * mPerItemWidth - offset + scroll_x;
        float top_y = getHeight() - getContext().getResources().getDimensionPixelSize(R.dimen.dimen_2);
        float bottom_y = getHeight();

        path.moveTo(left_x, top_y + 1f);
        path.lineTo(right_x, top_y + 1f);
        path.lineTo(right_x, bottom_y + 1f);
        path.lineTo(left_x, bottom_y + 1f);
        path.close();
        canvas.drawPath(path, mPaintFooterTriangle);
    }

    /**
     * 获取指定下标的选项卡的标题
     */
    private String getTitle(int pos) {
        // Set the default title
        String title = "title " + pos;
        // If the TitleProvider exist
        if (mTabs != null && mTabs.size() > pos) {
            title = mTabs.get(pos).getName();
        }
        return title;
    }

    //当页面滚动的时候，重新绘制滚动条
    public void onScrolled(int h) {
        mCurrentScroll = h;
        invalidate();
    }

    //当页面切换的时候，重新绘制滚动条
    public synchronized void onSwitched(int position) {
        if (mSelectedTab == position) {
            return;
        }
        setCurrentTab(position);
        invalidate();
    }

    public void init(int startPos, List<TabInfo> tabs, ViewPager mViewPager) {
        init(startPos, tabs, mViewPager, false);
    }

    //初始化选项卡
    public void init(int startPos, List<TabInfo> tabs, ViewPager mViewPager, boolean isShowDivider) {
        removeAllViews();
        this.mViewPager = mViewPager;
        this.mTabs = tabs;
        this.mTotal = tabs.size();
        for (int i = 0; i < mTotal; i++) {
            add(i, getTitle(i), mTotal, isShowDivider);
        }
        setCurrentTab(startPos);
        invalidate();
    }

    private void add(int index, String label, int total, boolean isShowDivider) {
        View tabIndicator;
        tabIndicator = mInflater.inflate(R.layout.layout_indicator, this, false);
        View dividerView = tabIndicator.findViewById(R.id.v_divider);
        if (isShowDivider) {
            dividerView.setVisibility(VISIBLE);
        } else {
            dividerView.setVisibility(GONE);
        }
        if (index == total - 1) {
            dividerView.setVisibility(GONE);
        }
        final TextView tv = (TextView) tabIndicator.findViewById(R.id.tv_title);
        tv.setText(label);
        tabIndicator.setId(index);
        tabIndicator.setOnClickListener(this);
        addView(tabIndicator);
    }

    @Override
    public void onClick(View v) {
        int position = v.getId();
        setCurrentTab(position);
    }

    public int getTabCount() {
        return getChildCount();
    }

    //设置当前选项卡
    public synchronized void setCurrentTab(int index) {
        if (index < 0 || index >= getTabCount()) {
            return;
        }
        View oldTab = getChildAt(mSelectedTab);
        oldTab.setSelected(false);
        TextView tv = (TextView) oldTab.findViewById(R.id.tv_title);
        tv.setTextColor(getResources().getColor(R.color.title_color));
        mSelectedTab = index;
        View newTab = getChildAt(mSelectedTab);
        newTab.setSelected(true);
        tv = (TextView) newTab.findViewById(R.id.tv_title);
        tv.setTextColor(getResources().getColor(R.color.app_color));
        mViewPager.setCurrentItem(mSelectedTab);
        invalidate();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus && getTabCount() > 0) {
            getChildAt(mSelectedTab).requestFocus();
            return;
        }

        if (hasFocus) {
            int i = 0;
            int numTabs = getTabCount();
            while (i < numTabs) {
                if (getChildAt(i) == v) {
                    setCurrentTab(i);
                    break;
                }
                i++;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mCurrentScroll == 0 && mSelectedTab != 0) {
            mCurrentScroll = (getWidth() + mViewPager.getPageMargin()) * mSelectedTab;
        }
    }
}
