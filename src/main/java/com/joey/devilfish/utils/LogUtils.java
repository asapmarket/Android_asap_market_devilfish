package com.joey.devilfish.utils;

import android.util.Log;

/**
 * log统一入口
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class LogUtils {
    private static boolean IS_PRINTABLE = false;

    private static LogUtils sInstance;

    /**
     * 私有构造方法，禁止外部直接调用
     */
    private LogUtils() {

    }

    public static LogUtils getInstance() {
        if (null == sInstance) {
            sInstance = new LogUtils();
        }

        return sInstance;
    }

    public void init(boolean isPrintable) {
        IS_PRINTABLE = isPrintable;
    }

    /**
     * Send a verbose log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public void v(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.v(tag, msg);
    }

    /**
     * Send a verbose log message and log the exception.
     *
     * @param tag  Used to identify the source of a log message.  It usually identifies
     *             the class or activity where the log call occurs.
     * @param msg  The formatted message you would like logged.
     * @param args An array of arguments to be substituted in place of formatting
     *             anchors. The last argument can be an instance of Throwable, and stack trace
     *             will be logged.
     */
    public void v(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.v(tag, format(msg, args));
    }

    public void d(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.d(tag, msg);
    }

    public void d(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.d(tag, format(msg, args));
    }

    public void i(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.i(tag, msg);
    }

    public void i(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.i(tag, format(msg, args));
    }

    public void w(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.w(tag, msg);
    }

    public void w(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.w(tag, format(msg, args));
    }

    public void e(String tag, String msg) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.e(tag, msg);
    }

    public void e(String tag, String msg, Object... args) {
        if (!IS_PRINTABLE) {
            return;
        }
        Log.e(tag, format(msg, args));
    }

    /**
     * For formatted messages substitute arguments.
     *
     * @param format 需要解析的字符串，{}会被args对应的字符串替换
     * @param args   待替换的字符串，最后一位可以是异常
     */
    private String format(final String format, final Object... args) {
        FormattingTuple formattingTuple = MessageFormatter.format(format, args);
        String message = formattingTuple.getMessage();
        Throwable throwable = formattingTuple.getThrowable();
        if (throwable != null) {
            return message + '\n' + Log.getStackTraceString(throwable);
        } else {
            return message;
        }
    }
}