package com.joey.devilfish.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SharedPreference工具类
 * Date: 2017/5/14
 *
 * @author xusheng
 */

public class SharedPreferenceUtils {

    public static String getSharedPreferences(String preferenceName, String propertyName, Context context, String defaultValue) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getString(propertyName, defaultValue);
    }

    public static int getSharedPreferences(String preferenceName, String propertyName, Context context, int defaultValue) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getInt(propertyName, defaultValue);
    }

    public static long getSharedPreferences(String preferenceName, String propertyName, Context context, long defaultValue) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getLong(propertyName, defaultValue);
    }

    public static boolean getSharedPreferences(String preferenceName, String propertyName, Context context, boolean defaultValue) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getBoolean(propertyName, defaultValue);
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, boolean propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit().putBoolean(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, String propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit()
                .putString(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, int propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit()
                .putInt(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, long propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit()
                .putLong(propertyName, propertyValue).commit();
    }

    public static ArrayList<String> getSharedPreferenceList(String preferenceName, String propertyName, Context context) {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = context.getSharedPreferences(preferenceName,
                Context.MODE_PRIVATE);
        String values;
        values = sp.getString(propertyName, "");
        if ("".equals(values)) {
            return null;
        }
        str = values.split(regularEx);
        return new ArrayList<String>(Arrays.asList(str));
    }

    public static void setSharedPreferenceList(String preferenceName, String propertyName, List<String> values, Context context) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = context.getSharedPreferences(preferenceName,
                Context.MODE_PRIVATE);
        if (values != null && values.size() > 0) {
            for (String value : values) {
                if ("".equals(value)) {
                    str += " ";
                } else {
                    str += value;
                }
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(propertyName, str);
            et.commit();
        }
    }


    public static List<Integer> getSharedPreferenceIntList(String preferenceName, String propertyName, Context context) {
        ArrayList<String> stringList = getSharedPreferenceList(preferenceName, propertyName, context);
        List<Integer> intList = new ArrayList<Integer>();
        if (stringList == null) {
            stringList = new ArrayList<String>();
        }
        for (String string : stringList) {
            intList.add(Integer.valueOf(string));
        }
        return intList;
    }

    public static void setSharedPreferenceIntList(String preferenceName, String propertyName, List<Integer> values, Context context) {
        List<String> stringList = new ArrayList<String>();
        if (values != null) {
            for (Integer integer : values) {
                stringList.add(String.valueOf(integer));
            }
        }
        setSharedPreferenceList(preferenceName, propertyName, stringList, context);
    }
}