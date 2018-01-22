package com.android.calendarlibrary.manager;


import com.android.calendarlibrary.utils.ChinaDate;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Created by Blaz Solar on 24/02/14.
 */
public class Day {

    private static final DateTimeFormatter mFormatter = DateTimeFormat.forPattern("d");

     private final LocalDate mDate;
    private boolean mToday;
    private boolean mSelected;
    private boolean mEnabled;
    private boolean mCurrent;

    public Day( LocalDate date, boolean today) {
        mDate = date;
        mToday = today;
        mEnabled = true;
        mCurrent = true;
    }

    
    public LocalDate getDate() {
        return mDate;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isCurrent() {
        return mCurrent;
    }

    public void setCurrent(boolean current) {
        mCurrent = current;
    }

    public boolean isToday() {
        return mToday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        if (mEnabled != day.mEnabled) return false;
        if (mSelected != day.mSelected) return false;
        if (mToday != day.mToday) return false;
        if (!mDate.isEqual(day.mDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mDate.hashCode();
        result = 31 * result + (mToday ? 1 : 0);
        result = 31 * result + (mSelected ? 1 : 0);
        result = 31 * result + (mEnabled ? 1 : 0);
        return result;
    }

    
    public String getText() {
        return mDate.toString(mFormatter);
    }
    
    public final static String[] nStr1 = new String[] { "", "正", "二", "三", "四",
  		"五", "六", "七", "八", "九", "十", "十一", "十二" };
     
      /**
       * 获取农历时间
       */
      public String getChinaDate() {
      	long[] l = ChinaDate.calElement(mDate.getYear(), mDate.getMonthOfYear(), mDate.getDayOfMonth());
  		String chinadate= ChinaDate.getChinaDate((int) (l[2]));
  		if(chinadate.equals("初一")){
  			chinadate=nStr1[(int) l[1]]+"月";
  		}
  		return chinadate;
  	}
}
