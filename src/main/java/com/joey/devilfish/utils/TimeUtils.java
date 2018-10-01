package com.joey.devilfish.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理类
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class TimeUtils {
    private static final String LOG_TAG = TimeUtils.class.getSimpleName();

    // yyyy-MM-dd
    public SimpleDateFormat YEARMONTHDAY = new SimpleDateFormat("yyyy-MM-dd");

    public SimpleDateFormat YEARMONTHDAY1 = new SimpleDateFormat("yyyy年MM月dd日");

    //  MM月dd日
    public SimpleDateFormat MONTHDAY = new SimpleDateFormat("MM月dd日");

    // yyyy-MM
    public SimpleDateFormat YEARMONTH = new SimpleDateFormat("yyyy-MM");

    // yyyy-MM-dd hh:mm:ss
    public SimpleDateFormat ALLFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // yyyy-MM-dd hh:mm:ss
    public SimpleDateFormat YEARMONTHDAYHourMinuteSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // HH:mm
    public SimpleDateFormat HourMinute = new SimpleDateFormat("HH:mm");

    // yyyy年MM月dd日  hh:mm
    public SimpleDateFormat ALLFORMATWITHCHINESE = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");

    // yyyy年MM月dd日  hh:mm:ss
    public SimpleDateFormat ALLFORMATWITHCHINESE2 = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");

    // 申请车辆或会议室提前6个小时
    public int APPLY_BETWEEN_IN_HOUR = 6;

    private static TimeUtils sInstance;

    /**
     * 私有构造方法，禁止外部直接调用
     */
    private TimeUtils() {

    }

    public static TimeUtils getInstance() {
        if (null == sInstance) {
            sInstance = new TimeUtils();
        }

        return sInstance;
    }

    /**
     * get the year of a special date
     *
     * @param date yyyy-MM-dd format date string
     * @return 0 if parsing error.
     */
    public int getYear(String date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.setTime(YEARMONTHDAY.parse(date));
            return calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return 0;
        }
    }

    /**
     * get the month of a special date
     *
     * @param date yyyy-MM-dd format date string
     * @return 0 if parsing error.
     */
    public int getMonth(String date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.setTime(YEARMONTHDAY.parse(date));
            return calendar.get(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return 0;
        }
    }

    /**
     * get the day of a special date
     *
     * @param date yyyy-MM-dd format date string
     * @return 0 if parsing error.
     */
    public int getDay(String date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.setTime(YEARMONTHDAY.parse(date));
            return calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return 0;
        }
    }

    /**
     * get the day of week of a special date
     *
     * @param date yyyy-MM-dd format date string
     * @return 0 if parsing error.
     */
    public int getWeekDay(String date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.setTime(YEARMONTHDAY.parse(date));
            return calendar.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return 0;
        }
    }

    /**
     * get the week of date by a special date
     */
    public int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * get the last day of a special month
     *
     * @param date yyyy-MM format date string
     * @return 0 if parsing error.
     */
    public int getEdgeDay(String date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.setTime(YEARMONTH.parse(date));
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return 0;
        }
    }

    /**
     * check if parameter is yyyy-MM-dd format date string.
     */
    public boolean isDateString(String date) {
        try {
            YEARMONTHDAY.parse(date);
            return true;
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return false;
        }
    }

    /**
     * get time interval between day.
     */
    public int daysBetween(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            return -1;
        }
        //1000是毫秒的修正
        long intervalMilli = toDate.getTime() + 1000 - fromDate.getTime();
        return (int) (intervalMilli / (24 * 3600000));
    }

    /**
     * get yyyy-MM-dd hh:mm:ss format time string.
     *
     * @param millis current time millis
     * @return 0 if parsing error.
     */
    public String getAllFormatStr(long millis, SimpleDateFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return format.format(calendar.getTime());
    }

    /**
     * get yyyy-MM-dd format time string.
     *
     * @param millis current time millis
     */
    public String getYearMonthDayFormatStr(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return YEARMONTHDAY.format(calendar.getTime());
    }

    /**
     * get yyyy-MM-dd format time string.
     */
    public String getYearMonthDayFormatStr(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return YEARMONTHDAY.format(calendar.getTime());
    }

    /**
     * get time millis.
     */
    public long getMillis(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return calendar.getTimeInMillis();
    }

    /**
     * get time millis.
     *
     * @param time yyyy-MM-dd hh:mm:ss format time string
     * @return 0 if parsing error.
     */
    public long getTime(String time) {
        try {
            Date date = ALLFORMAT.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return 0;
        }
    }

    /**
     * get day of time.
     *
     * @param time current time millis
     */
    public int getDayofTime(long time) {
        return (int) time % (24 * 60 * 60 * 1000);
    }

    /**
     * get hour of time.
     *
     * @param time current time millis
     */
    public int getHourofTime(long time) {
        long hourTime = time % (24 * 60 * 60 * 1000);
        return (int) hourTime / (60 * 60 * 1000);
    }

    /**
     * get minute of time.
     *
     * @param time current time millis
     */
    public int getMinuteofTime(long time) {
        long minTime = time % (60 * 60 * 1000);
        return (int) minTime / (60 * 1000);
    }

    /**
     * get second of time.
     *
     * @param time current time millis
     */
    public int getSecondofTime(long time) {
        long secTime = time % (60 * 1000);
        return (int) secTime / (1000);
    }

    /**
     * add 1 day.
     */
    public Date addOneDay(Date date) {
        return addDay(date, 1);
    }

    /**
     * add days of a special date.
     */
    public Date addDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    /**
     * add days of a special date.
     *
     * @param beforeDate yyyy-MM-dd format time string
     * @return empty string if parsing error.
     */
    public String addDay(String beforeDate, int days) {
        try {
            Date date = addDay(YEARMONTHDAY.parse(beforeDate), days);
            return YEARMONTHDAY.format(date);
        } catch (ParseException e) {
            LogUtils.getInstance().e(LOG_TAG, "parsing error: ", e);
            return "";
        }
    }

    /**
     * compare two dates.
     *
     * @return 1 if date1 is bigger; -1 if date1 is smaller; 0 if equal
     */
    public int compareDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return 0;
        } else if (date1 == null) {
            return -1;
        } else if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    public String getAllFormatByDate(Date date, SimpleDateFormat format) {
        if (null == date) {
            return "";
        }
        return getAllFormatStr(date.getTime(), format);
    }

    public String getAllFormat(int year, int month, int day, int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        long time = calendar.getTimeInMillis();
        return formatTime(ALLFORMAT, time);

    }

    /**
     * 根据固定时间格式format时间
     *
     * @param pattern 固定格式
     * @param time    时间
     * @return
     */
    public static String formatTime(SimpleDateFormat pattern, long time) {

        String formatTime = "";
        Date curDate = new Date(time);
        formatTime = pattern.format(curDate);
        return formatTime;
    }

    /**
     * 是否是闰年
     */
    public boolean isLeapYear(int year) {
        return ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) ? true : false;
    }

    /**
     * 得到当前年份
     */
    public int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     */
    public int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 得到当前日期
     */
    public int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public int getYear(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public int getDay(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public Date getCurrentDate() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        return date;
    }

    /**
     * 时间显示规则：今天的显示到分钟，如13：30，今天以前的显示月日，如06-07 15：30；今年以前的显示年份，如2010-09-08
     * 13：30; <BR>
     * yyyy.MM.dd HH:mm : 2011.06.02 10:10
     */
    public String formatTime(final Date date) {
        if (null == date) {
            return "";
        }
        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(date.getTime());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        String pattern;

        if (currentYear != year) {
            pattern = "yyyy-MM-dd HH:mm";
        } else if ((currentMonth != month) || (currentDay != day)) {
            pattern = "MM-dd HH:mm";
        } else {
            pattern = "HH:mm";
        }

        String time = new SimpleDateFormat(pattern).format(date.getTime());

        return time;
    }

    public String formatDate(Date date, String pattern) {
        if (null == date || StringUtils.getInstance().isNullOrEmpty(pattern)) {
            return "";
        }

        String time = new SimpleDateFormat(pattern).format(date.getTime());

        return time;
    }

    /**
     * 获取开始时间和结束时间的样式
     * 如果是同一天，就显示年月日 时分～时分
     *
     * @param startDate
     * @param endDate
     * @param hasHourAndMinute
     * @return
     */
    public String formatPeriodTime(Date startDate, Date endDate, boolean hasHourAndMinute) {
        if (null == startDate || null == endDate) {
            return "";
        }

        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(startDate.getTime());
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(endDate.getTime());
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (startYear == endYear && startMonth == endMonth && startDay == endDay) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            String startTime = new SimpleDateFormat("HH:mm").format(startDate);
            String endTime = new SimpleDateFormat("HH:mm").format(endDate);

            return date + " " + startTime + "~" + endTime;
        } else {
            if (hasHourAndMinute) {
                String startDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startDate);
                String endDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endDate);

                return startDateString + "~" + endDateString;
            } else {
                String startDateString = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
                String endDateString = new SimpleDateFormat("yyyy-MM-dd").format(endDate);

                return startDateString + "~" + endDateString;
            }
        }
    }

    public Calendar getTargetCalendar(Date date, boolean needAddDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (needAddDay) {
            calendar.set(Calendar.DAY_OF_MONTH, day + 1);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }

        return calendar;
    }

    /**
     * 多久之后
     *
     * @return
     */
    public Date getDateAfterHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    public String leftPadTwoZero(int num) {
        java.text.DecimalFormat format = new java.text.DecimalFormat("00");
        return format.format(num);
    }

    public String getDateAndWeek(Date date) {
        if (null == date) {
            return "";
        }

        String[] strings = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String dateString = getAllFormatByDate(date, MONTHDAY);
        int year = calendar.get(Calendar.YEAR);
        if (year != getCurrentYear()) {
            dateString = getAllFormatByDate(date, YEARMONTHDAY1);
        }

        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        if (week < 0) {
            week = 0;
        }

        return dateString + "  " + strings[week];
    }

    public String getInterval(Date date1, Date date2) {
        String string = "";
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long dayInMillis = 24 * 3600 * 1000;
        long hourInMillis = 3600 * 1000;
        long minuteInMillis = 60 * 1000;

        long period = time1 - time2;
        int day = 0;
        int hour = 0;
        int minute = 0;
        if (period > dayInMillis) {
            day = (int) (period / dayInMillis);
        }

        hour = (int) ((period - day * dayInMillis) / hourInMillis);
        minute = (int) ((period - day * dayInMillis - hour * hourInMillis) / minuteInMillis);

        if (day > 0) {
            string = string + day + "天";
        }

        if (hour > 0) {
            string = string + hour + "小时";
        }

        if (minute > 0) {
            string = string + minute + "分钟";
        }

        return string;
    }
}