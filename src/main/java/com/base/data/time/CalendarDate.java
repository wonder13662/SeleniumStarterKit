package com.base.data.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.Assert;

import com.base.util.TimeUtil;

public class CalendarDate {
	
	private Calendar calendar;
	
	public CalendarDate() {
		this.calendar = TimeUtil.getCalendar(TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDate());
	}
	
	public CalendarDate(String dateStr) {
		this.calendar = TimeUtil.getCalendar(TimeUtil.TimeFormat.YMD, dateStr);
	}
	
	public CalendarDate(String dateStr, TimeUtil.TimeFormat timeFormat) {
		this.calendar = TimeUtil.getCalendar(timeFormat, dateStr);
	}

	public CalendarDate(int year, int month, int date) {
		this.calendar = TimeUtil.getCalendar(year, month, date);
	}
	
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}

	public int getDate() {
		return calendar.get(Calendar.DATE);
	}

	public Calendar getCalendar() {
		return calendar;
	}
	
	public String getDateStr(TimeUtil.TimeFormat tf) {
		SimpleDateFormat sdf = TimeUtil.getSimpleDateFormat(tf.getValue());
		return sdf.format(calendar.getTime());
	}

	public void check() {
		Assert.assertTrue(getYear() > 0);
		Assert.assertTrue(getMonth() > 0 && getMonth() < 13);
		Assert.assertTrue(getDate() > 0 && getDate() < 32);
	}
	
	public CalendarDate cloneCycleEnd() {
		// CalendarDate 인스턴스를 1년 cycle의 시작 시점으로 cycle의 종료 시점인 CalendarDate를 돌려줍니다. 
		Calendar cal = TimeUtil.getCalendarByMonthDiff(this, 11);
		
		int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal = TimeUtil.getCalendar(TimeUtil.getYear(cal), TimeUtil.getMonth(cal), lastDayOfMonth);
		
		return new CalendarDate(TimeUtil.getFormattedDate(cal, TimeUtil.TimeFormat.YMD));
	}
	
	public CalendarDate clone(int daysDiff) {
		Calendar cal = TimeUtil.getCalendar(this, daysDiff);
		return new CalendarDate(TimeUtil.getFormattedDate(cal, TimeUtil.TimeFormat.YMD));
	}
	
	public int getDaysDiff(CalendarDate calDate) {
		return TimeUtil.getDaysDiff(this, calDate);
	}
	
	public String toString() {
		return TimeUtil.getFormattedDate(calendar, TimeUtil.TimeFormat.YMD);
	}
	
	public void expect(CalendarDate expected) {
		CalendarDate actual = this;
		
		Assert.assertEquals(actual.getYear(), expected.getYear());
		Assert.assertEquals(actual.getMonth(), expected.getMonth());
		Assert.assertEquals(actual.getDate(), expected.getDate());
	}
	
	public boolean equals(CalendarDate expected) {
		CalendarDate actual = this;
		
		if(actual.getYear() != expected.getYear()) return false;
		
		if(actual.getMonth() != expected.getMonth()) return false;

		if(actual.getDate() != expected.getDate()) return false;
		
		return true;
	}
	
	public boolean isAfter(CalendarDate expected) {
		return TimeUtil.getDaysDiff(expected, this) > 0;
	}
	
	public boolean isBefore(CalendarDate expected) {
		return TimeUtil.getDaysDiff(expected, this) < 0;
	}
}
