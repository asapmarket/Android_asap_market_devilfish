package com.joey.devilfish.utils;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 文件描述
 * Date: 2017/7/22
 *
 * @author xusheng
 */

public class ImageUtils {

    private static ImageUtils sInstance;

    /**
     * 私有构造方法，禁止外部直接调用
     */
    private ImageUtils() {

    }

    public static ImageUtils getInstance() {
        if (null == sInstance) {
            sInstance = new ImageUtils();
        }

        return sInstance;
    }

    /**
     * Displays an image given by the uri.
     *
     * @param uri uri of the image
     */
    public void setImageURI(Uri uri, SimpleDraweeView simpleDraweeView) {
        if (null == uri || null == simpleDraweeView) {
            return;
        }
        simpleDraweeView.setImageURI(uri, null);
    }

    public void setImageURL(String url, SimpleDraweeView simpleDraweeView) {
        if (url == null) {
            url = "";
        }

        setImageURI(Uri.parse(url), simpleDraweeView);
    }

    /**
     * Displays an image by resource id
     *
     * @param resId resource id
     */
    public void setImageResId(int resId, SimpleDraweeView simpleDraweeView) {
        if (resId <= 0 || null == simpleDraweeView) {
            return;
        }

        setImageURI(Uri.parse("res:///" + resId), simpleDraweeView);
    }

    /**
     * Displays an image by local path
     *
     * @param path local path of the image
     */
    public void setImageLocalPath(String path, SimpleDraweeView simpleDraweeView) {
        if (path == null) {
            return;
        }

        setImageURI(Uri.parse("file://" + path), simpleDraweeView);
    }
}
