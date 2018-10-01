package com.joey.devilfish.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.joey.devilfish.fusion.FusionCode;

/**
 * 提示
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class PromptUtils {
    private static PromptUtils sInstance;

    /**
     * 私有构造方法，禁止外部直接调用
     */
    private PromptUtils() {

    }

    public static PromptUtils getInstance() {
        if (null == sInstance) {
            sInstance = new PromptUtils();
        }

        return sInstance;
    }

    /**
     * 生成符合系统主题的AlertDialog.Builder
     *
     * @param context
     * @return
     */
    public AlertDialog.Builder getAlertDialog(Context context, boolean isLight) {
        return new AlertDialog.Builder(
                new ContextThemeWrapper(context, getDialogTheme(isLight)));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private int getDialogTheme(boolean isLight) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                (isLight ? android.R.style.Theme_Holo_Light_Dialog
                        : android.R.style.Theme_Holo_Dialog)
                : android.R.style.Theme_Dialog;
    }

    public void showShortPromptToast(Context aContext, int aResId) {
        Toast shortToast = Toast.makeText(aContext, aResId, Toast.LENGTH_SHORT);
        shortToast.setText(aResId);
        shortToast.show();
    }

    public void showShortPromptToast(Context aContext, String aResString) {
        if (StringUtils.getInstance().isNullOrEmpty(aResString)) {
            aResString = FusionCode.sEmptyString;
        }

        Toast shortToast = Toast.makeText(aContext, aResString, Toast.LENGTH_SHORT);
        shortToast.setText(aResString);
        shortToast.show();
    }

    public void showLongPromptToast(Context aContext, int aResId) {
        Toast shortToast = Toast.makeText(aContext, aResId, Toast.LENGTH_LONG);
        shortToast.setText(aResId);
        shortToast.show();
    }

    public void showLongPromptToast(Context aContext, String aResString) {
        if (StringUtils.getInstance().isNullOrEmpty(aResString)) {
            aResString = FusionCode.sEmptyString;
        }

        Toast shortToast = Toast.makeText(aContext, aResString, Toast.LENGTH_LONG);
        shortToast.setText(aResString);
        shortToast.show();
    }

}