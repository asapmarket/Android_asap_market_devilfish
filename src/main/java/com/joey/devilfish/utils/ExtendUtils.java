package com.joey.devilfish.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.File;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件描述
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class ExtendUtils {

    private static final String LOG_TAG = ExtendUtils.class.getSimpleName();

    private static ExtendUtils sInstance;

    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 私有构造方法，禁止外部直接调用
     */
    private ExtendUtils() {

    }

    public static ExtendUtils getInstance() {
        if (null == sInstance) {
            sInstance = new ExtendUtils();
        }

        return sInstance;
    }

    /**
     * 改变TextView某段文字的颜色
     *
     * @param view  TextView
     * @param start 起始位置
     * @param end   结束位置
     * @param color 要改变成的颜色
     */
    public void setSpan(TextView view, int start, int end, int color) {
        String strTemp = view.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(strTemp);
        style.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        view.setText(style);
    }

    /**
     * 改变TextView某段文字的颜色
     *
     * @param view  TextView
     * @param text  要变化的文字
     * @param start 要变化的文字起始位置
     * @param color 要改变成的颜色
     */
    public void setSpan(TextView view, String text, int start, int color) {
        setSpan(view, text, start, color, 0);
    }

    /**
     * 改变TextView某段文字的颜色
     *
     * @param view   TextView
     * @param text   要变化的文字
     * @param start  要变化的文字起始位置
     * @param color  要改变成的颜色
     * @param offset 要变化的文字结束位置的偏移值
     */
    public void setSpan(TextView view, String text, int start, int color, int offset) {
        String strTemp = view.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(strTemp);
        style.setSpan(new ForegroundColorSpan(color), start, start + text.length() + offset,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        view.setText(style);
    }

    /**
     * 对TextView某段加粗并改变颜色
     *
     * @param view   TextView
     * @param text   要变化的文字
     * @param start  要变化的文字起始位置
     * @param color  要改变成的颜色
     * @param offset 要变化的文字结束位置的偏移值
     */
    public void setBoldSpan(TextView view, String text, int start, int color, int offset) {
        String strTemp = view.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(strTemp);
        if (start < 0 || start > strTemp.length() || start + text.length() + offset > strTemp.length())
            return;
        style.setSpan(new ForegroundColorSpan(color), start, start + text.length() + offset,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new StyleSpan(Typeface.BOLD), start, start + text.length() + offset, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        view.setText(style);
    }

    /**
     * 改变TextView 2段文字的颜色
     *
     * @param view        TextView
     * @param text        第1段要变色的文字
     * @param start       第1段文字起始位置
     * @param secondText  第2段要变色的文字
     * @param secondStart 第2段文字起始位置
     * @param color       要改变成的颜色
     */
    public void setSpan(TextView view, String text, int start, String secondText, int secondStart, int color) {
        setSpan(view, text, start, secondText, secondStart, color, 0, 0);
    }

    /**
     * 改变TextView text颜色
     *
     * @param view        TextView
     * @param text        第1段要变色的文字
     * @param start       第1段文字起始位置
     * @param secondText  第2段要变色的文字
     * @param secondStart 第2段文字起始位置
     * @param color       要改变成的颜色
     * @param offset1     第1段要变色的文字结束位置的偏移值
     * @param offset2     第2段要变色的文字结束位置的偏移值
     */
    public void setSpan(TextView view, String text, int start, String secondText, int secondStart, int color, int offset1, int offset2) {
        String strTemp = view.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(strTemp);
        style.setSpan(new ForegroundColorSpan(color), start, start + text.length() + offset1,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(color), secondStart, secondStart + secondText.length() + offset2,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        view.setText(style);
    }

    /**
     * 对SpannableString中的某段进行颜色变换，可以对同一个Spannable进行多次变色
     *
     * @param content 原始Spannable
     * @param start   变色字段的开始index
     * @param end     变色字段的越界index
     * @param color   字体颜色
     * @return 变色后的spannable
     */
    public SpannableStringBuilder getSpannableString(SpannableStringBuilder content, int start, int end, int color) {
        if (content == null) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > content.length()) {
            end = content.length();
        }
        content.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return content;
    }

    /**
     * 对SpannableString中的某段进行加粗
     *
     * @param content 原始Spannable
     * @param start   变色字段的开始index
     * @param end     变色字段的越界index
     */
    public SpannableStringBuilder getBoldSpannableString(SpannableStringBuilder content, int start, int end) {
        if (content == null) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > content.length()) {
            end = content.length();
        }
        content.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return content;
    }

    /**
     * dp -> px
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px -> dp
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断字符串是否为手机号
     */
    public boolean isPhoneNumber(String number) {
        if (null == number)
            return false;

        Pattern pattern = Pattern.compile("^1[3-9][0-9]{9}$");
        return pattern.matcher(number).matches();
    }

    /**
     * 判断字符串是否为邮箱
     */
    public boolean isEmail(String email) {
        boolean isExist = false;

        Pattern p = Pattern.compile("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if (b) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * 对字符串进行MD5加密
     */
    public String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput(Context context, View view) {
        if (null == context || null == view) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(Context context, View view) {
        if (null == context || null == view) {
            return;
        }
        InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager m = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 获取应用当前版本号
     */
    public int getCurrentVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.getInstance().e(LOG_TAG, "package info not get", e);
            return 0;
        }
    }

    /**
     * 获取应用当前版名称
     */
    public String getCurrentVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.getInstance().e(LOG_TAG, "package info not get", e);
            return "";
        }
    }

    /**
     * 创建快捷方式
     *
     * @param context
     * @param iconResId    icon resource id
     * @param appNameResId app name string id
     */
    public void createShortCut(Context context, int iconResId, int appNameResId) {
        createShortCut(context, iconResId, appNameResId, false);
    }

    /**
     * 创建快捷方式
     *
     * @param context
     * @param iconResId      icon resource id
     * @param appNameResId   app name string id
     * @param allowDuplicate 是否允许重复创建
     */
    public void createShortCut(Context context, int iconResId, int appNameResId, boolean allowDuplicate) {
        String shortcutName = context.getString(appNameResId);
        if (hasShortcut(context, shortcutName)) {
            return;
        }

        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", allowDuplicate);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), iconResId);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(context.getApplicationContext(), context.getClass()));
        // 发送广播
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 判断应用的快捷方式是否存在
     * NOTE: "duplicate" 在部分android的rom上不起作用(如：Nexus4/5)，所以用这种方式检查是否存在快捷方式
     *
     * @param context      调用该方法的页面
     * @param shortcutName 快捷方式应用的名称
     * @return 是否创建
     */
    private boolean hasShortcut(Context context, String shortcutName) {
        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        Cursor c;
        try {
            c = context.getContentResolver().query(
                    CONTENT_URI,
                    new String[]{"title"},
                    "title=?",
                    new String[]{shortcutName},
                    null);
        } catch (Exception e) {
            return false;
        }
        return c != null && c.moveToNext();
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @param packageName 应用包名
     */
    public boolean isPackageInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * @param string          the String to find out the number's position
     * @param n               the No.n number
     * @param isBeginPosition true,the begin position of the number;false,the end position of the number
     * @return the position of the number in the string,start from 1
     * @author huyongsheng
     */
    public int getNumberPositionFromString(String string, int n, boolean isBeginPosition) {
        int position = -1;
        if (StringUtils.getInstance().isNullOrEmpty(string)) {
            position = -2;
        }
        if (n < 0) {
            position = -3;
        }
        boolean currentType = true;
        if (position == -1) {
            for (int i = 0; i < string.length(); i++) {
                char x = string.charAt(i);
                if (Character.isDigit(x)) {
                    if (currentType) {
                        n--;
                        if (n < 0 && isBeginPosition) {
                            position = i;
                            break;
                        }
                    } else {
                        continue;
                    }
                    currentType = false;
                } else {
                    if (currentType) {
                        continue;
                    } else {
                        if (n < 0 && !isBeginPosition) {
                            position = i - 1;
                            break;
                        }
                    }
                    currentType = true;
                }
            }
        }
        if (n < 0 && !isBeginPosition && position == -1) {
            position = string.length() - 1;
        }
        return position + 1;
    }

    /**
     * 根据进程号获取进程名称
     */
    public String getAppProcessNameByPID(Context context, int pid) {
        ActivityManager manager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    /**
     * 跳转到应用市场
     */
    public static void startMarket(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtils.getInstance().w(LOG_TAG, "Something wrong when start the market activity.", e);
        }
    }

    //md5算法
    public static String md5(String source) {
        if (source != null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(source.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public boolean isTabletDevice(Context context) {
        //true为平板，false为手机
        return (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 调用系统拨打电话
     *
     * @param context
     * @param phone
     */
    public void doDial(Context context, String phone) {
        if (!StringUtils.getInstance().isNullOrEmpty(phone)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
        }
    }

    /**
     * 调用系统发短信
     *
     * @param context
     * @param phone
     */
    public void doMessage(Context context, String phone) {
        if (!StringUtils.getInstance().isNullOrEmpty(phone) &&
                this.isPhoneNumber(phone)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + phone));
            context.startActivity(intent);
        }
    }

    public String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return androidId;
    }

    public void updateAlbum(Context context, File file) {
        if (null == file || null == context) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public Bitmap createDrawableFromView(Context context, View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public boolean hasPermission(Context context, String permission) {
        try {
            if (PackageManager.PERMISSION_DENIED == context.checkCallingOrSelfPermission(permission)) {
                return false;
            }
        } catch (Exception e) {

        }

        return true;
    }
}
