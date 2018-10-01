package com.joey.devilfish.widget.imageloader;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.joey.devilfish.utils.AndroidVersionUtils;
import com.joey.devilfish.utils.BitmapUtil;
import com.joey.devilfish.utils.LruCache;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLoader {
    private static final String TAG = ImageLoader.class.getName();
    private static ImageLoader sInstance;
    private LruCache<String, SoftReference<Bitmap>> mImageCache;
    private List<ImageLoadTask> mTaskQueue = Collections.synchronizedList(new ArrayList<ImageLoadTask>());
    private ConcurrentHashMap<ImageCallBack, ImageLoadTask> mTaskMap = new ConcurrentHashMap<ImageCallBack, ImageLoadTask>();

    private HandlerThread handlerThread;
    private Handler imageHandler;
    private Handler mHandler;

    private ImageLoader(Context ctx) {
        handlerThread = new HandlerThread("ImageLoader Main Threadd-", Process.THREAD_PRIORITY_MORE_FAVORABLE);
        handlerThread.start();
        imageHandler = new Handler(handlerThread.getLooper());

        final ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        final int memoryClassBytes = am.getMemoryClass() * 1024 * 1024;
        mImageCache = new ImageCache(memoryClassBytes / 2);

        mHandler = new MyHandler();
    }

    public static ImageLoader getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new ImageLoader(ctx);
        }
        return sInstance;
    }

    public void loadImages(final String imageUrl, final ImageCallBack callBack, final int position) {
        if (callBack == null) {
            return;
        }
        if (imageUrl == null) {
            callBack.onImageLoadFailed();
            return;
        }
        callBack.onImageStartLoad();
        if (mImageCache != null && null != mImageCache.get(imageUrl)) {
            SoftReference<Bitmap> softBmp = mImageCache.get(imageUrl);
            Bitmap bmp = softBmp.get();
            if (null != bmp && bmp.isRecycled()) {
                mImageCache.remove(imageUrl);
            } else if (null != bmp) {
                Message msg = Message.obtain();
                ImageLoadTask task = new ImageLoadTask(callBack);
                task.setUrl(imageUrl);
                task.setBitmap(bmp);
                task.setPosition(position);
                msg.obj = task;
                mHandler.sendMessage(msg);
                return;
            }
        }
        if (position == callBack.getPosition()) {
            //FIX by zhangjun: http://bug.tuniu.org/issues/23404
            //在图像load之前，先设置下默认图，避免出现图像出现被替换的效果
            callBack.loadDefault();
        }
        imageHandler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoadTask task = new ImageLoadTask(callBack);

                task.setUrl(imageUrl);
                task.setPosition(position);
                if (!mTaskQueue.contains(task)) {
                    mTaskQueue.add(task);
                }
                while (mTaskQueue.size() > 0) {
                    ImageLoadTask task1 = mTaskQueue.remove(0);
                    Bitmap bitmap = BitmapUtil.getBitmapFromUrl(task1.getUrl());
                    if (null != bitmap) {
                        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
                        mImageCache.put(task1.getUrl(), softBitmap);
                    }
                    Message msg = Message.obtain();
                    task1.setBitmap(bitmap);
                    msg.obj = task1;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    public interface ImageCallBack {
        public void onImageLoaded(Bitmap bitmap, String imageUrl);

        public void onImageStartLoad();

        public void onImageLoadFailed();

        public void loadDefault();

        public int getPosition();
    }

    public void TrimMemory(int level) {
        // Memory we can release here will help overall system performance, and
        // make us a smaller target as the system looks for memory
        try {
            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) { // 60
                // Nearing middle of list of cached background apps; evict our
                // entire thumbnail cache
                Log.v(TAG, "evicting entire thumbnail cache");
                mImageCache.evictAll();

            } else if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) { // 40
                // Entering list of cached background apps; evict oldest half of our
                // thumbnail cache
                Log.v(TAG, "evicting oldest half of thumbnail cache");
                mImageCache.trimToSize(mImageCache.size() / 2);
            }
        } catch (IllegalStateException e) {
            // TODO: handle exception
        }

    }

    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageLoadTask task = (ImageLoadTask) msg.obj;
            if (task == null) {
                return;
            }
            ImageCallBack imageCallBack = task.getImageCallBack();
            if (imageCallBack == null) { // may have been released
                return;
            }

            int viewPosition = imageCallBack.getPosition();
            if (viewPosition != task.getPosition()) {
                // FIX by zhangjun: http://bug.tuniu.org/issues/23404
                return;
            }
            if (task.getBitmap() == null) {
                imageCallBack.onImageLoadFailed();
            } else {
                imageCallBack.onImageLoaded(((ImageLoadTask) msg.obj).getBitmap(), task.getUrl());
            }
        }
    }

    public static class ImageCache extends LruCache<String, SoftReference<Bitmap>> {
        public ImageCache(int maxSizeBytes) {
            super(maxSizeBytes);
        }

        @Override
        protected int sizeOf(String key, SoftReference<Bitmap> value) {
            Bitmap bitmap = value.get();
            if (bitmap == null || bitmap.isRecycled()) {
                return 0;
            } else {
                return getSizeInBytes(bitmap);
            }
        }
    }

    @SuppressLint("NewApi")
    public static int getSizeInBytes(Bitmap bitmap) {
        if (AndroidVersionUtils.isHoneycombMr2OrHigher()) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }
}
