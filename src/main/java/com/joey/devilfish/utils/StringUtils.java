package com.joey.devilfish.utils;

import android.widget.TextView;

import com.joey.devilfish.fusion.FusionCode;

/**
 * 文件描述
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class StringUtils {
    private static StringUtils sInstance;

    /**
     * 私有构造方法，禁止外部直接调用
     */
    private StringUtils() {

    }

    public static StringUtils getInstance() {
        if (null == sInstance) {
            sInstance = new StringUtils();
        }

        return sInstance;
    }

    /**
     * 检测字符串是否为空
     */
    public boolean isNullOrEmpty(String aInput) {
        if (null == aInput) {
            return true;
        }

        if (aInput.equals(FusionCode.sEmptyString)) {
            return true;
        }

        return false;
    }

    /**
     * 检测所有字符串是否为空
     */
    public boolean isAllNullOrEmpty(String... aInputs) {
        if (null == aInputs) {
            return true;
        }

        for (String string : aInputs) {
            if (!isNullOrEmpty(string)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取字符串的字符数目
     */
    public int getCharacterNum(final String aContent) {
        if (null == aContent || FusionCode.sEmptyString.equals(aContent)) {
            return 0;
        } else {
            return (aContent.length() + getChineseNum(aContent));
        }
    }

    /**
     * 获取字符串non-ascii字符数目
     */
    public int getChineseNum(String aContent) {
        int num = 0;
        char[] myChar = aContent.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 如果字符串为空，就返回""，否则返回字符串本身
     */
    public String getRealOrEmpty(String aInput) {
        return isNullOrEmpty(aInput) ?
                FusionCode.sEmptyString : aInput;
    }

    public void setText(String input, TextView textView) {
        if (null == textView) {
            return;
        }

        textView.setText(StringUtils.getInstance().isNullOrEmpty(input)
                ? "" : input);
    }
}