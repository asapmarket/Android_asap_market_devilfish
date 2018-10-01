package com.joey.devilfish.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 文件描述
 * Date: 15/9/4
 *
 * @author xusheng2
 */
public class SDCardFileUtils {

    public static final String APP_DIR_NAME = "devilfish";
    public static final String IMAGE_DIR_NAME = "image";
    public static final String USER_AVATAR_DIR_NAME = "avatar";
    public static final String DRAFT_DIR_NAME = "draft";

    private static String rootDir;
    private static String appRootDir;
    private static String userAvatarDir;
    private static String imageDir;
    private static String draftDir;

    public static void init() {
        rootDir = getRootPath();
        if (!StringUtils.getInstance().isNullOrEmpty(rootDir)) {
            appRootDir = rootDir + "/" + APP_DIR_NAME;
            userAvatarDir = appRootDir + "/" + USER_AVATAR_DIR_NAME;
            imageDir = appRootDir + "/" + IMAGE_DIR_NAME;
            draftDir = appRootDir + "/" + DRAFT_DIR_NAME;
            File appDir = new File(appRootDir);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }

            File userAvatarDirFile = new File(userAvatarDir);
            if (!userAvatarDirFile.exists()) {
                userAvatarDirFile.mkdirs();
            }

            File imageDirFile = new File(imageDir);
            if (!imageDirFile.exists()) {
                imageDirFile.mkdirs();
            }

            File draftDirFile = new File(draftDir);
            if (!draftDirFile.exists()) {
                draftDirFile.mkdir();
            }

        } else {
            rootDir = "";
            appRootDir = "";
            userAvatarDir = "";
            imageDir = "";
            draftDir = "";
        }
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static String getRootPath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();// filePath:  /sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:  /data/data/
        }
    }

    public static String getAppRootDir() {
        return appRootDir;
    }

    public static String getImagePath() {
        return imageDir;
    }

    public static String getDraftDir() {
        return draftDir;
    }

    public static boolean makeFile(String path, String name) {
        File file = new File(path + "/" + name);
        File dir = new File(path);
        if (dir.exists() && file.exists()) {
            return true;
        } else if (dir.exists() && !file.exists()) {
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else if (!dir.exists()) {
            if (dir.mkdirs()) {
                try {
                    file.createNewFile();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
