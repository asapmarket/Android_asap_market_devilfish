package com.joey.devilfish.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.joey.devilfish.fusion.FusionCode;
import com.joey.devilfish.fusion.FusionField;
import com.joey.devilfish.fusion.SharedPreferenceConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 文件描述
 * Date: 2017/7/26
 *
 * @author xusheng
 */

public class Utils {

    public static void changeLanguage(Context context) {
        if (null == context) {
            return;
        }

        int language = SharedPreferenceUtils.getSharedPreferences(
                SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_LANGUAGE,
                context, FusionCode.LocalState.LOCAL_STATE_CHINESE);

        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (language == FusionCode.LocalState.LOCAL_STATE_ENGLISH) {
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }

        context.getResources().updateConfiguration(config, dm);
    }

    public static boolean isChinese(Context context) {
        if (null == context) {
            return true;
        }

        if (FusionField.mCurrentLanguage < 0) {
            int language = SharedPreferenceUtils.getSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                    SharedPreferenceConstant.PROPERTY_LANGUAGE, context, FusionCode.LocalState.LOCAL_STATE_CHINESE);

            FusionField.mCurrentLanguage = language;
        }

        return FusionField.mCurrentLanguage == FusionCode.LocalState.LOCAL_STATE_CHINESE;

    }

    public static void setToken(Context context, String token) {
        SharedPreferenceUtils.setSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_TOKEN, token, context);
    }

    public static String getToken(Context context) {
        return SharedPreferenceUtils.getSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_TOKEN, context, "");
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferenceUtils.setSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_USERID, userId, context);
    }

    public static String getUserId(Context context) {
        return SharedPreferenceUtils.getSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_USERID, context, "");
    }

    public static void setNickName(Context context, String nickName) {
        SharedPreferenceUtils.setSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_NICK_NAME, nickName, context);
    }

    public static String getNickName(Context context) {
        return SharedPreferenceUtils.getSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_NICK_NAME, context, "");
    }

    public static void setHeadImage(Context context, String headImage) {
        SharedPreferenceUtils.setSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_HEAD_IMAGE, headImage, context);
    }

    public static String getHeadImage(Context context) {
        return SharedPreferenceUtils.getSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_HEAD_IMAGE, context, "");
    }

    public static boolean getIsFirst(Context context) {
        return SharedPreferenceUtils.getSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_IS_FIRST_IN, context, true);
    }

    public static void setIsFirst(Context context, boolean isFirstIn) {
        SharedPreferenceUtils.setSharedPreferences(SharedPreferenceConstant.PREFERENCE_NAME,
                SharedPreferenceConstant.PROPERTY_IS_FIRST_IN, isFirstIn, context);
    }

    /**
     * @param strs
     * @param i
     * @param j
     * @param num
     */
    public static void compare(String[] strs, int i, int j, int num) {
        //判断2个字符串谁的长度最小，则以当前长度作为num+1的最大标准
        if (strs[i].length() >= strs[j].length()) {
            if (num + 1 <= strs[j].length()) {
                if (strs[j].charAt(num) > strs[i].charAt(num)) {
                    String temp = strs[i];
                    strs[i] = strs[j];
                    strs[j] = temp;
                    //若相等，则判断第二个
                } else if (strs[j].charAt(num) == strs[i].charAt(num)) {
                    num++;
                    compare(strs, i, j, num);
                }
            }
        } else {
            if (num + 1 <= strs[i].length()) {
                if (strs[j].charAt(num) > strs[i].charAt(num)) {
                    String temp = strs[i];
                    strs[i] = strs[j];
                    strs[j] = temp;
                    //若相等，则判断第二个
                } else if (strs[j].charAt(num) == strs[i].charAt(num)) {
                    num++;
                    compare(strs, i, j, num);
                }
            } else {
                //表示当前字符串内容都一致，strs[j]的长度大。 则放前面。
                String temp = strs[i];
                strs[i] = strs[j];
                strs[j] = temp;
            }
        }
    }

    public static void logout(Context context) {
        setToken(context, "");
        setUserId(context, "");
        setHeadImage(context, "");
        setNickName(context, "");
    }

    public static boolean isAppAvailable(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
