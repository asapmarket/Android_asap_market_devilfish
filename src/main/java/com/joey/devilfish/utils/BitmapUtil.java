package com.joey.devilfish.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.joey.devilfish.config.AppConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 图片处理方法库
 * Date: 15/7/4
 *
 * @author xusheng2
 */
public class BitmapUtil {

    private static final String LOG_TAG = BitmapUtil.class.getSimpleName();

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int DEFAULT_MEGA_SIZE = 1024;
    private static final int DEFAULT_IMAGE_LIMIT = 32;
    private static final int REQUIRED_MAX_BITMAP_WIDTH;
    private static final int REQUIRED_MAX_BITMAP_HEIGHT;

    /**
     * 外部存储目录
     */
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 缓存目录
     */
    private static final String BASE_CACHE_PATH = BASE_PATH + "/.devilfish/.devilfishPic/";

    private static String sdState = null;

    static {
        REQUIRED_MAX_BITMAP_WIDTH = AppConfig.getScreenWidth();
        REQUIRED_MAX_BITMAP_HEIGHT = AppConfig.getScreenHeight();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap
    {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

    /**
     * 从http的url地址获取图片
     *
     * @param imageUrl 图片地址
     * @return 图像的Bitmap对象
     */
    public static Bitmap getBitmapFromUrl(String imageUrl) {
        if (StringUtils.getInstance().isNullOrEmpty(imageUrl)) {
            return null;
        }

        String cachedImageFileName = ExtendUtils.getInstance().MD5(imageUrl);
        Bitmap bitmap = getBitmap(cachedImageFileName);
        if (bitmap != null) {
            return bitmap;
        }

        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            URL url = new URL(imageUrl);
            inputStream = url.openConnection().getInputStream();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            outputStream = new ByteArrayOutputStream();
            int n;
            while (EOF != (n = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, n);
            }
            byte[] imageBytes = outputStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
            bitmap = resetOptionSampleSizeIfNeed(imageBytes, options);
            if (null != bitmap) {
                saveBitmap(imageBytes, cachedImageFileName);
            }
        } catch (Exception e) {
            LogUtils.getInstance().w(LOG_TAG, "Fail to load image from url.", e);
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LogUtils.getInstance().w(LOG_TAG, "Fail to close stream after load image.", e);
            }
        }
        return bitmap;
    }

    /**
     * 保存bitmap到指定文件
     */
    public static File saveBitmap(Bitmap bitmap, String folder, String fileName) {
        folder = new StringBuilder(BASE_CACHE_PATH).append(folder).toString();
        File file = new File(folder);
        if (!file.exists() && !file.mkdirs()) {
            LogUtils.getInstance().w(LOG_TAG, "Fail to create image cache directory: {}", folder);
            return null;
        }
        File cachedImage = new File(file, fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(cachedImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            LogUtils.getInstance().d(LOG_TAG, "save bitmap in ：{}/{}", folder, fileName);
        } catch (IOException e) {
            LogUtils.getInstance().e(LOG_TAG, "IO error", e);
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LogUtils.getInstance().w(LOG_TAG, "Fail to close stream after save bitmap to file.", e);
            }
        }

        return cachedImage;
    }

    /**
     * 保存图像到缓存目录
     *
     * @param imageBytes 图像的字节
     * @param imageName  图像名
     */
    public static void saveBitmap(byte[] imageBytes, String imageName) {
        String sdState = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(sdState)) {
            return;
        }

        File cacheDir = new File(BASE_CACHE_PATH);
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            LogUtils.getInstance().w(LOG_TAG, "Fail to create image cache directory: {}", BASE_CACHE_PATH);
            return;
        }

        File cachedImage = new File(cacheDir, imageName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(cachedImage);
            outputStream.write(imageBytes, 0, imageBytes.length);
        } catch (IOException e) {
            LogUtils.getInstance().w(LOG_TAG, "Fail to save bitmap to file.", e);
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LogUtils.getInstance().w(LOG_TAG, "Fail to close stream after save bitmap to file.", e);
            }
        }
    }

    /**
     * 从缓存目录获取缓存的图片
     *
     * @param imageName 缓存的图片文件名
     * @return 缓存的bitmap对象，null表示未缓存不存在
     */
    public static Bitmap getBitmap(String imageName) {
        String sdState = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(sdState)) {
            return null;
        }
        File cachedImage = new File(BASE_CACHE_PATH, imageName);
        if (!cachedImage.isFile() || !cachedImage.exists()) {
            return null;
        }
        Bitmap bitmap = null;
        FileInputStream inputStream = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true;
            options.inInputShareable = true;
            options.inPurgeable = true;
            inputStream = new FileInputStream(cachedImage);
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream = new FileInputStream(cachedImage);
            bitmap = resetOptionSampleSizeIfNeed(inputStream, options);
        } catch (OutOfMemoryError e) {
            LogUtils.getInstance().w(LOG_TAG, "OOM", e);
            System.gc();
        } catch (Exception e) {
            LogUtils.getInstance().w(LOG_TAG, "Fail to load image from disk.", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // do nothing here
                }
            }
        }
        return bitmap;
    }

    private static Bitmap resetOptionSampleSizeIfNeed(FileInputStream inputStream, BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        int pow = 0;
        while (options.outHeight >> pow > REQUIRED_MAX_BITMAP_HEIGHT || options.outWidth >> pow > REQUIRED_MAX_BITMAP_WIDTH) {
            pow += 1;
        }
        options.inSampleSize = 1 << pow;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    private static Bitmap resetOptionSampleSizeIfNeed(byte[] imageBytes, BitmapFactory.Options options) {
        int pow = 0;
        while (options.outHeight >> pow > REQUIRED_MAX_BITMAP_HEIGHT || options.outWidth >> pow > REQUIRED_MAX_BITMAP_WIDTH) {
            pow += 1;
        }
        options.inSampleSize = 1 << pow;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
    }

    public static void deleteFile(File file) {
        sdState = Environment.getExternalStorageState();
        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
                file.delete();
            }
        }
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 转换成黑白图片
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     * @param bitmap   The picture intent to add round corner
     * @param radius   The radius of the corner
     * @param location Which corner to add round corner:
     *                 1-left_top;2-left_bottom;3-right_top;4-right_bottom
     * @return The picture with round corner or null if native create bitmap return null
     * @author huyongsheng
     */
    public static Bitmap setRoundCorner(Bitmap bitmap, int radius, int... location) {

        if (bitmap == null) {
            return bitmap;
        }

        final int LEFT_TOP = 1;
        final int LEFT_BOTTOM = 2;
        final int RIGHT_TOP = 3;
        final int RIGHT_BOTTOM = 4;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Rect completeRect = new Rect(0, 0, width, height);
        Rect leftTopRect = new Rect(0, 0, radius, radius);
        Rect leftBottomRect = new Rect(0, height - radius, radius, height);
        Rect rightTopRect = new Rect(width - radius, 0, width, radius);
        Rect rightBottomRect = new Rect(width - radius, height - radius, width, height);
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // [fix] 线上crash 1300263 NullPointerException Bitmap.create返回null, 上层调用需判断bitmap是否为空
        if (output == null) {
            return null;
        }
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.drawRect(completeRect, paint);
        if (location != null) {
            for (int corner : location) {
                switch (corner) {
                    case LEFT_TOP:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(leftTopRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(radius, radius, radius, paint);
                        break;
                    case LEFT_BOTTOM:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(leftBottomRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(radius, height - radius, radius, paint);
                        break;
                    case RIGHT_TOP:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(rightTopRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(width - radius, radius, radius, paint);
                        break;
                    case RIGHT_BOTTOM:
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawRect(rightBottomRect, paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        canvas.drawCircle(width - radius, height - radius, radius, paint);
                        break;
                    default:
                        break;
                }
            }
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return output;
    }

    /**
     * 压缩图片小于size kb
     *
     * @param bitmap:传入的位图对象
     * @param size:对象大小阀值
     */
    public static Bitmap compressBitmap(Bitmap bitmap, float size) {
        double originSize = getBitmapSize(bitmap);//原图大小
        if (size > originSize)
            return bitmap;
        float zoom = (float) Math.sqrt(size / originSize);//首次缩放的比例,因为是边的缩放所以要开方
        Bitmap compressBitmap = zoomImage(bitmap, zoom);//图像开始进行压缩
        return compressBitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param bgimage ：源图片资源
     * @param zoom    ：缩放比例
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, float zoom) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        matrix.postScale(zoom, zoom);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        //首次进行宽高缩放,得到第一次压缩的bitmap
        double compress = getBitmapSize(bitmap);
        double lastCompress = compress;
        //由于图像第一次压缩过后会有不准的情况，所以进行循环判断每次压缩90%
        //因为第一次的压缩是有保证的，所以循环一般不会超过2次
        while (compress > DEFAULT_IMAGE_LIMIT) {
            matrix.setScale(0.9f, 0.9f);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            lastCompress = compress;
            compress = getBitmapSize(bitmap);
            if (compress == 0 || lastCompress == compress)
                return bitmap;
        }
        return bitmap;
    }

    /**
     * 获取传入图片的字节大小
     *
     * @param bitmap:传入的位图对象
     */
    public static int getBitmapSize(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bitmap == null)
            return 0;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//将bitmap写入流
        } catch (Exception e) {
            return 0;
        }
        byte[] b = baos.toByteArray();//将字节换成KB
        return b.length / DEFAULT_MEGA_SIZE;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = null;
        try {
            //获取资源图片
            is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, opt);
        } catch (Resources.NotFoundException e) {
            LogUtils.getInstance().d(LOG_TAG, "ResourceId Can not found");
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 按照scale将图片从正中心剪裁成目标比例
     *
     * @param resource
     * @param desScale 图片应有的宽高比 宽/高
     * @return
     */
    public static Bitmap getScaledMap(Bitmap resource, float desScale) {
        if (resource == null) {
            return resource;
        }
        int width = resource.getWidth();
        int height = resource.getHeight();
        // modified by xusheng on 2015-05-19
        if (width <= 0 || height <= 0) {
            return null;
        }
        int widthByHeight = (int) (height * desScale);
        int heightByWidth = (int) (width / desScale);
        int scaledWidth = 0;
        int scaledHeight = 0;
        if (widthByHeight <= width) {
            scaledWidth = widthByHeight;
            scaledHeight = height;
        } else if (heightByWidth <= height) {
            scaledHeight = heightByWidth;
            scaledWidth = width;
        }
        int x = (width - scaledWidth) / 2;
        int y = (height - scaledHeight) / 2;
        Bitmap bitmap = Bitmap.createBitmap(resource, x, y, scaledWidth, scaledHeight);
        return bitmap;
    }

}
