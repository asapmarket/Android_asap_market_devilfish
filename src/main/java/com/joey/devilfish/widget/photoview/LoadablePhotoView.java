package com.joey.devilfish.widget.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.joey.devilfish.R;
import com.joey.devilfish.utils.StringUtils;
import com.joey.devilfish.widget.imageloader.ImageLoader;

import uk.co.senab.photoview.PhotoView;

/**
 * 图片
 * Date: 2017/5/14
 *
 * @author xusheng
 */

public class LoadablePhotoView extends PhotoView implements ImageLoader.ImageCallBack {

    private static final String LOG_TAG = LoadablePhotoView.class.getSimpleName();

    private Context mContext;

    private int position = 0;

    private static final int REQUIRED_MAX_BITMAP_SIZE = 4096;

    private RotateAnimation mAnimation;

    public LoadablePhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        mContext = context;
    }

    public LoadablePhotoView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
    }

    public LoadablePhotoView(Context context) {
        super(context);
        mContext = context;
    }

    public void loadImage(String url, int position) {
        if (StringUtils.getInstance().isNullOrEmpty(url)) {
            return;
        }
        setPosition(position);
        ImageLoader.getInstance(mContext).loadImages(url, this, position);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, String imageUrl) {
        if (null != bitmap) {
            try {
                int pow = 0;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                while (width >> pow > REQUIRED_MAX_BITMAP_SIZE || height >> pow > REQUIRED_MAX_BITMAP_SIZE) {
                    pow += 1;
                }
                width = width >> pow;
                height = height >> pow;
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, width, height, true);
                setImageBitmap(scaled);
                setScaleType(ImageView.ScaleType.FIT_CENTER);
            } catch (IllegalStateException e) {
                setImageDrawable(null);
            }
        }
        mAnimation.cancel();
        mAnimation = null;
    }

    @Override
    public void onImageStartLoad() {
        try {
            setImageDrawable(getResources().getDrawable(R.drawable.icon_loading));

            mAnimation=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);//设置图片动画属性，各参数说明可参照api
            mAnimation.setRepeatCount(5);//设置旋转重复次数，即转几圈
            mAnimation.setDuration(1000);//设置持续时间，注意这里是每一圈的持续时间，如果上面设置的圈数为3，持续时间设置1000，则图片一共旋转3秒钟
            mAnimation.setInterpolator(new LinearInterpolator());//设置动画匀速改变。相应的还有AccelerateInterpolator、DecelerateInterpolator、CycleInterpolator等
            setAnimation(mAnimation);//设置imageview的动画，也可以myImageView.startAnimation(myAlphaAnimation)
            mAnimation.start();
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } catch (IllegalStateException e) {

        }
    }

    @Override
    public void onImageLoadFailed() {
        mAnimation.cancel();
        mAnimation = null;
        try {
            setImageDrawable(getResources().getDrawable(R.drawable.icon_loading_fail));
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public void loadDefault() {

    }

    @Override
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

