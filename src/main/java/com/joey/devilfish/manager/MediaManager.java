package com.joey.devilfish.manager;

import android.os.Handler;

import com.joey.devilfish.utils.SDCardFileUtils;
import com.joey.devilfish.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件描述
 * Date: 2017/5/14
 *
 * @author xusheng
 */

public class MediaManager {

    public static final int SAVE_SUCCESS = 0;

    public static final int SAVE_FAIL = 1;

    private String mLocalPath;

    private long mMediaLength = 0;

    private long mReadSize = 0;

    private String mMediaPath;

    public void setMediaPath(String mediaPath) {
        this.mMediaPath = mediaPath;
        if (!StringUtils.getInstance().isNullOrEmpty(mMediaPath)) {
            SDCardFileUtils.init();
            int index = mMediaPath.lastIndexOf("/");
            mLocalPath = SDCardFileUtils.getImagePath()
                    + mMediaPath.substring(index + 1);
        }
    }

    public String getLocalPath() {

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(mLocalPath);

            if (file.exists()) {
                return mLocalPath;
            }
        }

        return null;
    }

    public void saveMedia(final Handler handler) {

        if (StringUtils.getInstance().isNullOrEmpty(mMediaPath)) {
            handler.sendEmptyMessage(SAVE_FAIL);
            return;
        }

        final String path = mMediaPath;
        final File cacheFile = new File(mLocalPath);

        new Thread(new Runnable() {

            @Override
            public void run() {
                FileOutputStream out = null;
                InputStream is = null;

                try {
                    URL url = new URL(path);
                    HttpURLConnection httpConnection = (HttpURLConnection) url
                            .openConnection();
                    httpConnection.setConnectTimeout(5000);
                    httpConnection.setRequestMethod("GET");

                    if (null != cacheFile && cacheFile.exists()) {
                        cacheFile.delete();
                    }

                    if (!cacheFile.exists()) {
                        cacheFile.getParentFile().mkdirs();
                        cacheFile.createNewFile();
                    }

                    mReadSize = cacheFile.length();
                    out = new FileOutputStream(cacheFile, true);

                    httpConnection.setRequestProperty("User-Agent", "NetFox");
                    httpConnection.setRequestProperty("RANGE", "bytes="
                            + mReadSize + "-");

                    is = httpConnection.getInputStream();

                    mMediaLength = httpConnection.getContentLength();
                    if (mMediaLength == -1) {
                        handler.sendEmptyMessage(SAVE_FAIL);
                        return;
                    }

                    mMediaLength += mReadSize;

                    byte buf[] = new byte[4 * 1024];
                    int size = 0;

                    while ((size = is.read(buf)) != -1) {
                        try {
                            out.write(buf, 0, size);
                            mReadSize += size;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    handler.sendEmptyMessage(SAVE_SUCCESS);

                } catch (Exception e) {
                    handler.sendEmptyMessage(SAVE_FAIL);
                    e.printStackTrace();
                    if (null != cacheFile && cacheFile.exists()) {
                        cacheFile.delete();
                    }
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            //
                        }
                    }

                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            //
                        }
                    }
                }

            }
        }).start();
    }
}
