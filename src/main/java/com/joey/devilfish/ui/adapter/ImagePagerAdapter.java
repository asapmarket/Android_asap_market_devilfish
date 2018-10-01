package com.joey.devilfish.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joey.devilfish.R;
import com.joey.devilfish.widget.photoview.LoadablePhotoView;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagePagerAdapter extends PagerAdapter {


    private Context mContext;
    private List<String> mPictureList;

    private int mLayoutId = -1;

    public ImagePagerAdapter(Context context) {
        this.mContext = context;
    }

    public ImagePagerAdapter(Context context, int resLayout) {
        this.mLayoutId = resLayout;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mPictureList == null ? 0 : mPictureList.size();
    }

    public void setData(List<String> list) {
        mPictureList = list;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View view;
        if (mLayoutId == -1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_pictures, null);
        } else {
            view = LayoutInflater.from(mContext).inflate(mLayoutId, null);
        }

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (LoadablePhotoView) view.findViewById(R.id.lpv_picture);
        viewHolder.titleView = (TextView) view.findViewById(R.id.tv_title);
        viewHolder.countView = (TextView) view.findViewById(R.id.tv_picture_conut);
        viewHolder.saveView = (TextView) view.findViewById(R.id.tv_save);
        viewHolder.imageView.loadImage(mPictureList.get(position), position);
        viewHolder.titleView.setText("");

        viewHolder.countView.setText(mContext.getResources().getString(R.string.number_part, position + 1, getCount()));
        viewHolder.imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v2) {
                if (null != mOperateListener) {
                    mOperateListener.onImageTap();
                }
            }
        });

        viewHolder.saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOperateListener && null != mPictureList && mPictureList.size() > position) {
                    mOperateListener.onDownload(mPictureList.get(position));
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class ViewHolder {
        LoadablePhotoView imageView;
        TextView titleView;
        TextView countView;
        TextView saveView;
    }

    private OperateListener mOperateListener;

    public void setOperateListener(OperateListener listener) {
        mOperateListener = listener;
    }

    public interface OperateListener {

        // 单击图片关闭activity
        void onImageTap();

        void onDownload(String url);
    }
}
