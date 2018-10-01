package com.joey.devilfish.ui.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 适配器基类
 * Date: 2017/7/1
 *
 * @author xusheng
 */

public abstract class ListDataAdapter<ModuleType> extends BaseAdapter {

    protected Context mContext;

    protected List<ModuleType> mDataList = new ArrayList<>();

    public ListDataAdapter(Context context) {
        this.mContext = context;
    }

    public ListDataAdapter(Context context, List<ModuleType> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ModuleType getItem(int arg0) {
        return mDataList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public int insertItems(Collection<ModuleType> items) {
        if (items == null || items.size() < 1) {
            return 0;
        }

        if (mDataList == null) {
            mDataList = new ArrayList<>(items);
        }

        mDataList.addAll(0, items);
        this.notifyDataSetChanged();
        return items.size();
    }

    public int addItems(Collection<ModuleType> items) {
        if (items == null || items.size() < 1) {
            return 0;
        }

        if (mDataList == null) {
            mDataList = new ArrayList<>(items);
        }

        mDataList.addAll(items);

        this.notifyDataSetChanged();
        return items.size();
    }

    public int setItems(Collection<ModuleType> items) {
        if (mDataList != null) {
            mDataList.clear();
        }

        if (items == null || items.size() < 1) {
            this.notifyDataSetChanged();
            return 0;
        }

        mDataList.addAll(items);
        this.notifyDataSetChanged();
        return items.size();
    }
}