package com.joey.devilfish.utils;

/**
 * 文件描述
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class FormattingTuple {

    private String message;
    private Throwable throwable;

    public FormattingTuple(String message) {
        this(message, null);
    }

    public FormattingTuple(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}