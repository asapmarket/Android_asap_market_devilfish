package com.joey.devilfish.widget.datetimepicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;

import com.joey.devilfish.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期选择
 * Date: 2017/7/22
 *
 * @author xusheng
 */

public class DateTimePickerWidget implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String DATEPICKER_TAG = "datepicker";

    public static final String TIMEPICKER_TAG = "timepicker";

    private Context mContext;

    private DatePickerDialog mDatePickerDialog;

    private TimePickerDialog mTimePickerDialog;

    private OnDateChangedListener mListener;

    private FragmentManager mFragmentMgr;

    private Date mCurrentDate;

    private boolean mIsNeedSetTime = false;

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        this.mListener = listener;
    }

    public DateTimePickerWidget(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentMgr = fragmentManager;
    }

    public void setNeedSetTime(boolean isNeedSetTime) {
        this.mIsNeedSetTime = isNeedSetTime;
    }

    public void pickDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            mCurrentDate = date;
            calendar.setTime(date);
        }
        mDatePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.setStyle(R.style.TransparentDialog, R.style.TransparentDialog);
        mDatePickerDialog.setYearRange(calendar.get(Calendar.YEAR) - 30, calendar.get(Calendar.YEAR) + 20);

        mDatePickerDialog.setThemeDark(false);
        mDatePickerDialog.vibrate(true);
        mDatePickerDialog.dismissOnPause(false);
        mDatePickerDialog.showYearPickerFirst(false);
        mDatePickerDialog.show(((Activity) mContext).getFragmentManager(), DATEPICKER_TAG);
        mDatePickerDialog.setAccentColor(Color.parseColor("#2196f3"));
    }

    public void pickTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            mCurrentDate = date;
            calendar.setTime(date);
        }
        mTimePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), true);
        mTimePickerDialog.setStyle(R.style.TransparentDialog, R.style.TransparentDialog);

        mTimePickerDialog.setThemeDark(false);
        mTimePickerDialog.vibrate(true);
        mTimePickerDialog.dismissOnPause(false);
        mTimePickerDialog.show(((Activity) mContext).getFragmentManager(), DATEPICKER_TAG);
        mTimePickerDialog.setAccentColor(Color.parseColor("#2196f3"));
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        if (null != mCurrentDate) {
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(mCurrentDate);
            calendar.set(Calendar.HOUR_OF_DAY, currentCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, currentCalendar.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, currentCalendar.get(Calendar.SECOND));
        }
        Date date = calendar.getTime();

        if (mIsNeedSetTime) {
            pickTime(date);
        } else {
            if (null != mListener) {
                mListener.onDateChanged(date);
            }
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        if (null != mCurrentDate) {
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(mCurrentDate);
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH));
        }

        Date date = calendar.getTime();

        if (null != date && null != mListener) {
            mListener.onDateChanged(date);
        }
    }
}
