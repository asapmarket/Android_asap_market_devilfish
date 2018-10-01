package com.joey.devilfish.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Base64;

import com.joey.devilfish.fusion.FusionCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 外部存储目录
     */
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static CacheFile getStringCache(String fileName, Context context) {

        if (context == null) {
            return null;
        }
        String folder = context.getFilesDir() + "/" + FusionCode.FILE_CONSTANT.FILE_CONSTANT_FILE_FOLDER;
        if (!checkAndCreateFolder(folder)) {
            return null;
        }
        String filePath = folder + "/" + fileName + FusionCode.FILE_CONSTANT.CACHE_FILE_SUFFIX;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream inStream = null;
        ByteArrayOutputStream stream = null;
        try {
            inStream = new FileInputStream(filePath);
            stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            String streamStr = new String(Base64.decode(stream.toByteArray(), Base64.DEFAULT));
            LogUtils.getInstance().d(TAG, "get file from disk folderName = {} fileName = {}",
                    FusionCode.FILE_CONSTANT.FILE_CONSTANT_FILE_FOLDER, fileName);
            CacheFile cacheFile = new CacheFile();
            if (streamStr != null && streamStr.length() > 14) {
                cacheFile.updateTime = streamStr.substring(0, 13);
                cacheFile.fileContent = streamStr.substring(14);
            }
            return cacheFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
        } finally {
            try {
                stream.close();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveStringToCache(Context context, String fileName,
                                         long time, String jsonString) {
        LogUtils.getInstance().d(TAG, "write file to disk folderName = {} fileName = {}",
                FusionCode.FILE_CONSTANT.FILE_CONSTANT_FILE_FOLDER, fileName);
        if (context == null) {
            return;
        }
        String folder = context.getFilesDir() + "/" + FusionCode.FILE_CONSTANT.FILE_CONSTANT_FILE_FOLDER;
        if (!checkAndCreateFolder(folder)) {
            return;
        }
        String filePath = folder + "/" + fileName + FusionCode.FILE_CONSTANT.CACHE_FILE_SUFFIX;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        String saveStr = Base64.encodeToString((time + "," + jsonString).getBytes(), Base64.DEFAULT);
        try {
            LogUtils.getInstance().d(TAG, "save file to {} jsonString = {}", filePath, jsonString);
            FileOutputStream outStream = new FileOutputStream(filePath);
            outStream.write(saveStr.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    public static boolean checkAndCreateFolder(String folder) {
        try {
            File dirFile = new File(folder);
            if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
                return dirFile.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void deleteFolder(Context context, File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFolder(context, childFiles[i]);
            }
            file.delete();
        }
    }

    public static void deleteAllFileCache(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteAllFileCache(childFiles[i]);
            }
            file.delete();
        }
    }

    public static void deleteAllSharedCache(Context context, String preferenceName) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void deleteCacheFile(Context context, String folderName, String fileName) {
        File dirFile = new File(context.getFilesDir() + "/" + folderName);
        if (!dirFile.exists()) {
            return;
        }
        if (!dirFile.isDirectory()) {
            dirFile.delete();
            return;
        }
        File file = new File(dirFile + "/" + fileName + FusionCode.FILE_CONSTANT.CACHE_FILE_SUFFIX);
        try {
            file.delete();
        } catch (Exception e) {

        }
    }

    /**
     * @return float 单位为M
     * @description 获取文件夹大小
     * @date 2013-12-03
     * @author huyongsheng
     */
    public static float getFolderSize(File folder) {
        float size = 0;
        try {
            File[] fileList = folder.listFiles();
            if (null == fileList) {
                return 0;
            }
            for (File file : fileList) {
                if (file.isDirectory()) {
                    size = size + getFolderSize(file);
                } else {
                    size = size + file.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size / 1048576;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
}