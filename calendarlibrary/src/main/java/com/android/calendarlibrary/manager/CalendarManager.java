package com.android.calendarlibrary.manager;

import org.joda.time.LocalDate;

import java.util.Calendar;

/**
 * 日历管理
 * @author MaJian
 *
 */
public class CalendarManager {

	private State mState;
	private RangeUnit mUnit;
	private LocalDate mSelected;
	private LocalDate mToday;
	private LocalDate mMinDate;
	private LocalDate mMaxDate;
	private Formatter formatter;
	private LocalDate mActiveMonth;
	private String monthStr = "";				//月份字符串
	private OnMonthChangeListener monthChangeListener;
	
	public interface OnMonthChangeListener{
		/**
		 * 月份切换监听器
		 * @param month	当前月份
		 * @param mSelected	选中日期
		 */
		public void monthChange(String month, LocalDate mSelected);
	}
	
	/**
	 * 设置月份切换监听器
	 * @param monthChangeListener
	 */
	public void setMonthChangeListener(OnMonthChangeListener monthChangeListener){
		this.monthChangeListener = monthChangeListener;
	}

	/**
	 * 构造器
	 * @param selected 	选中的日期
	 * @param state		状态：月份、周
	 * @param minDate	最小日期
	 * @param maxDate	最大日期
	 */
	public CalendarManager(  LocalDate selected,   State state,   LocalDate minDate,
			LocalDate maxDate) {
		this(selected, state, minDate, maxDate, null);
	}
	/**
	 * 设置今天
	 */
	public void setToday(){
		mToday = LocalDate.now();
	}

	public CalendarManager(  LocalDate selected,   State state,   LocalDate minDate,
			LocalDate maxDate,   Formatter formatter) {
		mToday = LocalDate.now();
		mState = state;
		if (formatter == null) {
			this.formatter = new DefaultFormatter();
		} else {
			this.formatter = formatter;
		}
		init(selected, minDate, maxDate);
	}
	
	/**
	 * 返回今天日期对象
	 * @return
	 */
	public LocalDate getToday(){
		if (mToday == null) {
			return LocalDate.now();
		}
		return mToday;
	}

	public boolean selectDay(  LocalDate date) {
		if (!mSelected.isEqual(date)) {
			mUnit.deselect(mSelected);
			mSelected = date;
			mUnit.select(mSelected);

			if (mState == State.WEEK) {
				setActiveMonth(date);
			}
			return true;
		} else {
			return false;
		}
	}


	public LocalDate getSelectedDay() {
		return mSelected;
	}

	/**
	 * 获取头部日期显示文本
	 * @return 根据日期单位返回月份或者周数
	 */

	public String getHeaderText() {
		if (!monthStr.equals(formatter.getHeaderText(mUnit.getType(), mUnit.getFrom(), mUnit.getTo() , mSelected))) {
			monthStr = formatter.getHeaderText(mUnit.getType(), mUnit.getFrom(), mUnit.getTo() , mSelected);
			if (monthChangeListener != null) {
				monthChangeListener.monthChange(monthStr,mSelected);
			}
		}
		return monthStr;
	}

	/**
	 * 得到日历月份的一个日期
	 * @return
	 */
	public LocalDate getCurrentMonthDate(){
		if (mUnit == null || mUnit.getFrom() == null) {
			return LocalDate.now();
		}
		return mUnit.getFrom();
	}

	/**
	 * 是否有下一项
	 * @return
	 */
	public boolean hasNext() {
		return mUnit.hasNext();
	}

	/**
	 * 是否有前一项
	 * @return
	 */
	public boolean hasPrev() {
		return mUnit.hasPrev();
	}

	/**
	 * 下一项
	 * @return
	 */
	public boolean next() {
		boolean next = mUnit.next();
		Calendar cal = Calendar.getInstance();
		cal.setTime(mSelected.toDate());
		if (mState == State.MONTH) {
			cal.add(Calendar.MONTH, 1);
			mSelected = LocalDate.fromCalendarFields(cal);
			selectDay(mSelected);
		} else if (mState == State.WEEK) {
			cal.add(Calendar.WEEK_OF_MONTH, 1);
			mSelected = LocalDate.fromCalendarFields(cal);
			selectDay(mSelected);
		}
		mUnit.select(mSelected);
		setActiveMonth(mUnit.getFrom());
		return next;
	}

	/**
	 * 前一项
	 * @return
	 */
	public boolean prev() {
		boolean prev = mUnit.prev();
		Calendar cal = Calendar.getInstance();
		cal.setTime(mSelected.toDate());
		if (mState == State.MONTH) {
			cal.add(Calendar.MONTH, -1);
			mSelected = LocalDate.fromCalendarFields(cal);
			selectDay(mSelected);
		} else if (mState == State.WEEK) {
			cal.add(Calendar.WEEK_OF_MONTH, -1);
			mSelected = LocalDate.fromCalendarFields(cal);
			selectDay(mSelected);
		}
		mUnit.select(mSelected);
		setActiveMonth(mUnit.getTo());
		return prev;
	}

	/**
	 *
	 * @return index of month to focus to
	 */
	public synchronized void toggleView() {
		if(mState == State.MONTH) {
			toggleFromMonth();
		} else {
			toggleFromWeek();
		}
	}

	/**
	 * 得到当前日历状态
	 * @return
	 */
	public State getState() {
		return mState;
	}

	public CalendarUnit getUnits() {
		return mUnit;
	}

	public LocalDate getActiveMonth() {
		return mActiveMonth;
	}

	private void setActiveMonth(LocalDate activeMonth) {
		mActiveMonth = activeMonth.withDayOfMonth(1);
	}

	private synchronized void toggleFromMonth() {

		// if same month as selected
		if (mUnit.isInView(mSelected)) {
			toggleFromMonth(mSelected);

			setActiveMonth(mSelected);
		} else {
			setActiveMonth(mUnit.getFrom());
			toggleFromMonth(mUnit.getFirstDateOfCurrentMonth(mActiveMonth));
		}
	}

	void toggleToWeek(int weekInMonth) {
		LocalDate date = mUnit.getFrom().plusDays(weekInMonth * 7);
		toggleFromMonth(date);
	}

	private void toggleFromMonth(LocalDate date) {
		setUnit(new Week(date, mToday, mMinDate, mMaxDate));
		mUnit.select(mSelected);
		mState = State.WEEK;
	}

	private synchronized void toggleFromWeek() {
		setUnit(new Month(mActiveMonth, mToday, mMinDate, mMaxDate));
		mUnit.select(mSelected);
		mState = State.MONTH;
	}

	private void init() {
		if (mState == State.MONTH) {
			setUnit(new Month(mSelected, mToday, mMinDate, mMaxDate));
		} else {
			setUnit(new Week(mSelected, mToday, mMinDate, mMaxDate));
		}
		mUnit.select(mSelected);
	}

	synchronized void setUnit(  RangeUnit unit) {
		if (unit != null) {
			mUnit = unit;
		}
	}

	public int getWeekOfMonth() {
		if(mUnit.isInView(mSelected)) {
			if (mUnit.isIn(mSelected)) { // TODO not pretty
				return mUnit.getWeekInMonth(mSelected);
			} else if (mUnit.getFrom().isAfter(mSelected)) {
				return mUnit.getWeekInMonth(mUnit.getFrom());
			} else {
				return mUnit.getWeekInMonth(mUnit.getTo());
			}
		} else {
			return mUnit.getFirstWeek(mUnit.getFirstDateOfCurrentMonth(mActiveMonth)); // if not in this month first week should be selected
		}
	}

	public void init(  LocalDate date){
		this.init(date, mMinDate, mMaxDate);
	}

	public void init(  LocalDate date,   LocalDate minDate,   LocalDate maxDate) {
		mSelected = date;
		setActiveMonth(date);
		mMinDate = minDate;
		mMaxDate = maxDate;
		init();
	}

	public LocalDate getMinDate() {
		return mMinDate;
	}

	public void setMinDate(  LocalDate minDate) {
		mMinDate = minDate;
	}

	public LocalDate getMaxDate() {
		return mMaxDate;
	}

	public void setMaxDate(  LocalDate maxDate) {
		mMaxDate = maxDate;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * CalendarManager State
	 * @author MaJian
	 *
	 */
	public enum State {
		MONTH,
		WEEK
	}

}
