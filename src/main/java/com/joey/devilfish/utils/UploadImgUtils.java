package com.joey.devilfish.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件描述
 * Date: 2017/7/27
 *
 * @author xusheng
 */

public class UploadImgUtils {


    /**
     * 上传文件路径
     */
    private String mUploadPath = null;
    /**
     * 文件原始路径
     */
    private String mFilePath = null;
    /**
     * 文件拓展名
     */
    private String extensionName = null;
    /**
     * 上传文件名
     */
    private String uploadFileName = null;
    /**
     * 上传文件Uri
     */
    private Uri OutputHeaderFileUri = null;

    private Bitmap mUploadBitmap = null;

    public UploadImgUtils(String mFilePath) {
        this.mFilePath = mFilePath;
        initOutputHeaderFileUri();
    }

    public UploadImgUtils(String mUploadPath, String mFilePath) {
        this.mUploadPath = mUploadPath;
        this.mFilePath = mFilePath;
        extensionName = FileUtils.getExtensionName(mFilePath);
        uploadFileName = "avatar_" + System.currentTimeMillis() + "." + extensionName;
    }

    public UploadImgUtils(String mFilePath, Bitmap uploadBitmap) {
        this.mFilePath = mFilePath;
        this.mUploadBitmap = uploadBitmap;
    }

    /**
     * public UploadImgUtils(Context ctx,String mUploadPath, String mFilePath) {
     * this.context=ctx;
     * this.mUploadPath = mUploadPath;
     * this.mFilePath = mFilePath;
     * }
     **/

    protected Uri initOutputHeaderFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Header");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mUploadPath = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        File mediaFile = new File(mUploadPath);
        OutputHeaderFileUri = Uri.fromFile(mediaFile);
        return OutputHeaderFileUri;
    }



    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getmUploadPath() {
        return mUploadPath;
    }

    public void setmUploadPath(String mUploadPath) {
        this.mUploadPath = mUploadPath;
    }

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public Uri getOutputHeaderFileUri() {
        return OutputHeaderFileUri;
    }

    public void setOutputHeaderFileUri(Uri outputHeaderFileUri) {
        OutputHeaderFileUri = outputHeaderFileUri;
    }

    public Bitmap getmUploadBitmap() {
        return mUploadBitmap;
    }

    public void setmUploadBitmap(Bitmap mUploadBitmap) {
        this.mUploadBitmap = mUploadBitmap;
    }
}
