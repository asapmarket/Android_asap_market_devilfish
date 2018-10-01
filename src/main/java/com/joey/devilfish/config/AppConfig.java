package com.joey.devilfish.config;

/**
 * 应用配置
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class AppConfig {

    private static int screenWidth;

    private static int screenHeight;

    private static float screenSize;

    private static int statusBarHeight;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static float getScreenSize() {
        return screenSize;
    }

    public static void setScreenHeight(int screenHeight) {
        AppConfig.screenHeight = screenHeight;
    }

    public static void setScreenWidth(int screenWidth) {
        AppConfig.screenWidth = screenWidth;
    }

    public static void setScreenSize(float screenSize) {
        AppConfig.screenSize = screenSize;
    }

    public static void setStatusBarHeight(int height) {
        AppConfig.statusBarHeight = height;
    }

    public static int getStatusBarHeight() {
        return statusBarHeight;
    }
}
