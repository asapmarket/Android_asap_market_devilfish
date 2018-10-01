package com.joey.devilfish.application;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.joey.devilfish.config.AppConfig;

import java.util.ArrayList;

/**
 * 文件描述
 * Date: 2017/5/14
 *
 * @author xusheng
 */

public class BaseApplication extends Application {

    private ArrayList<Activity> mActivities = new ArrayList<Activity>();

    private static BaseApplication sInstance;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        // 图片加载初始化
        Fresco.initialize(getApplicationContext());

        // 获取屏幕尺寸
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (dm.widthPixels <= dm.heightPixels) {
            AppConfig.setScreenWidth(dm.widthPixels);
            AppConfig.setScreenHeight(dm.heightPixels);
        } else { // 确保screen width取实际屏幕宽度
            AppConfig.setScreenWidth(dm.heightPixels);
            AppConfig.setScreenHeight(dm.widthPixels);
        }
        AppConfig.setScreenSize((float) (Math.sqrt(dm.widthPixels * dm.widthPixels +
                dm.heightPixels * dm.heightPixels) / (dm.density * 160)));

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (mActivities != null && mActivities.size() > 0) {
            if (!mActivities.contains(activity)) {
                mActivities.add(activity);
            }
        } else {
            mActivities.add(activity);
        }

    }

    public void removeActivity(Activity activity) {
        if (mActivities != null && mActivities.size() > 0) {
            if (mActivities.contains(activity)) {
                mActivities.remove(activity);
            }
        }
    }

    // 遍历所有Activity并finish
    public void exit() {
        if (mActivities != null && mActivities.size() > 0) {
            for (Activity activity : mActivities) {
                if (null != activity) {
                    try {
                        activity.finish();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}