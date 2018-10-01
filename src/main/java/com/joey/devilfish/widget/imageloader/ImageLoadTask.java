package com.joey.devilfish.widget.imageloader;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

public class ImageLoadTask {

    private String url;
    private Bitmap bitmap;

    private final WeakReference<ImageLoader.ImageCallBack> imageCallBackRef;
    private int position;

    public ImageLoadTask(ImageLoader.ImageCallBack imageCallBack) {
        this.imageCallBackRef = new WeakReference<ImageLoader.ImageCallBack>(imageCallBack);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String path) {
        this.url = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageLoader.ImageCallBack getImageCallBack() {
        return imageCallBackRef.get();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
