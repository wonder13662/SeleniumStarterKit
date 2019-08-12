package com.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.base.data.time.CalendarDate;

public class TimeUtil {
	public static enum TimeFormat {
		YMDHMS("yyyyMMdd_HHmmss"),
		YMD_HMS("yyyy-MM-dd HH:mm:ss"), 
		HMS("HHmmss"), 
		YMD("yyyy-MM-dd"), 
		DMY("dd/MM/yyyy"), 
		M_Y("MMM yyyy"), 
		D_M_Y("d MMM yyyy"), 
		CALENDAR_DATE("MMM yyyy");
		
		private String value;
		TimeFormat(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	private TimeUtil() {}

	public static Calendar getCalendar(int year, int month, int date) {
		return TimeUtil.getCalendar(year, month, date, 0);
	}

	public static Calendar getCalendar(CalendarDate calDate, int dayDiff) {
		return TimeUtil.getCalendar(calDate.getYear(), calDate.getMonth(), calDate.getDate(), dayDiff);
	}

	public static Calendar getCalendar(int year, int month, int date, int dayDiff) {
		String dt = String.format("%d-%d-%d", year, month, date);  // Start date
		
		SimpleDateFormat sdf = TimeUtil.getSimpleDateFormat("yyyy-M-d");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(dayDiff != 0) {
			c.add(Calendar.DATE, dayDiff);
		}
		
		return c;
	}

	public static CalendarDate getCalendarDate(TimeFormat tf, String value) {
		Calendar cal = TimeUtil.getCalendar(tf, value);
		return new CalendarDate(TimeUtil.getYear(cal), TimeUtil.getMonth(cal), TimeUtil.getDate(cal));
	}

	public static CalendarDate getCalendarDate(int year, int month, int date) {
		Calendar cal = getCalendar(year, month, date);
		return new CalendarDate(TimeUtil.getYear(cal), TimeUtil.getMonth(cal), TimeUtil.getDate(cal));
	}

	public static int getMaxDaysInCurrentMonth() {
		CalendarDate calDate = getCalendarDate(TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDate());
		return TimeUtil.getMaxDaysInMonth(calDate.getCalendar());
	}

	public static int getMaxDaysInMonth(Calendar calendar) {
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static CalendarDate getRandomCalendarDate(int year, int month, int date, int dayDiffMax) {
		Calendar cal = TimeUtil.getRandomCalendar(year, month, date, dayDiffMax);
		return new CalendarDate(TimeUtil.getYear(cal), TimeUtil.getMonth(cal), TimeUtil.getDate(cal));
	}

	public static Calendar getRandomCalendar(int year, int month, int date, int dayDiffMax) {
		int dayDiff = NumberUtil.getRandom(0, dayDiffMax);
		return getCalendar(year, month, date, dayDiff);
	}

	public static Calendar getCalendarByMonthDiff(CalendarDate calDate, int monthDiff) {
		return TimeUtil.getCalendarByMonthDiff(calDate.getYear(), calDate.getMonth(), calDate.getDate(), monthDiff);
	}

	public static Calendar getCalendarByMonthDiff(int year, int month, int date, int monthDiff) {
		String dt = String.format("%d-%d-%d", year, month, date);  // Start date
		
		SimpleDateFormat sdf = TimeUtil.getSimpleDateFormat("yyyy-M-d");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(monthDiff != 0) {
			c.add(Calendar.MONTH, monthDiff);
		}
		
		return c;
	}

	public static Calendar getCalendar(TimeFormat tf, String value) {
		return TimeUtil.getCalendar(tf.getValue(), value);
	}

	public static Calendar getCalendar(String format, String value) {
		
		SimpleDateFormat sdf = TimeUtil.getSimpleDateFormat(format);
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(value));
			return c;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static Date getDate(int year, int month, int date) {
		String dt = String.format("%d-%d-%d", year, month, date);  // Start date
		SimpleDateFormat sdf = TimeUtil.getSimpleDateFormat("yyyy-M-d");
		try {
			return sdf.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static String getFormattedDate(int year, int month, int date, TimeFormat tf) {		
		Date parsedDate = getDate(year, month, date);
		return TimeUtil.getFormattedDate(parsedDate, tf.getValue()); // "yyyy-MM-dd hh:mm:ss"
	}

	public static String getFormattedDate(Calendar cal, TimeFormat tf) {
		return TimeUtil.getFormattedDate(cal, tf.getValue());
	}

	public static String getFormattedDate(Calendar cal, String format) {
		Date parsedDate = cal.getTime();
		return TimeUtil.getFormattedDate(parsedDate, format); // "yyyy-MM-dd hh:mm:ss"
	}

	public static SimpleDateFormat getSimpleDateFormat(TimeFormat timeFormat) {
		return TimeUtil.getSimpleDateFormat(timeFormat.getValue());
	}

	public static SimpleDateFormat getSimpleDateFormat(String dateFormat) {
		return new SimpleDateFormat(dateFormat, Locale.US);
	}

	public static String getFormattedDate(Date date, String dateFormat) {
		SimpleDateFormat format = getSimpleDateFormat(dateFormat);
	    return format.format(date);
	}

	public static CalendarDate getCalendarDateFromToday(int daysDiff) {
		return TimeUtil.getNowCalendarDate().clone(daysDiff);
	}

	public static CalendarDate getNowCalendarDate() {
		return new CalendarDate(TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDate());
	}

	public static String getNow(String format) {
		return getFormattedDate(new Date(), format);
	}

	public static String getNow() {
		return getNow(TimeFormat.YMD_HMS.getValue());
	}

	public static String getNow(TimeFormat tf) {
		return getNow(tf.getValue());
	}

	public static int getYear(Calendar cal) {
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth(Calendar cal) {
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getDate(Calendar cal) {
		return cal.get(Calendar.DATE);
	}

	public static int getMonthIdx(String monthName) {
		if(StringUtil.contains("JANUARY", monthName)) {
			return Calendar.JANUARY;
		} else if(StringUtil.contains("FEBRUARY", monthName)) {
			return Calendar.FEBRUARY;
		} else if(StringUtil.contains("MARCH", monthName)) {
			return Calendar.MARCH;
		} else if(StringUtil.contains("APRIL", monthName)) {
			return Calendar.APRIL;
		} else if(StringUtil.contains("MAY", monthName)) {
			return Calendar.MAY;
		} else if(StringUtil.contains("JUNE", monthName)) {
			return Calendar.JUNE;
		} else if(StringUtil.contains("JULY", monthName)) {
			return Calendar.JULY;
		} else if(StringUtil.contains("AUGUST", monthName)) {
			return Calendar.AUGUST;			
		} else if(StringUtil.contains("SEPTEMBER", monthName)) {
			return Calendar.SEPTEMBER;			
		} else if(StringUtil.contains("OCTOBER", monthName)) {
			return Calendar.OCTOBER;			
		} else if(StringUtil.contains("NOVEMBER", monthName)) {
			return Calendar.NOVEMBER;			
		} else if(StringUtil.contains("DECEMBER", monthName)) {
			return Calendar.DECEMBER;
		}
		
		return -1;
		
	}

	public static int getDayIdx(String day) {
		if(StringUtil.contains("SUNDAY", day)) {
			return Calendar.SUNDAY;
		} else if(StringUtil.contains("MONDAY", day)) {
			return Calendar.MONDAY;
		} else if(StringUtil.contains("TUESDAY", day)) {
			return Calendar.TUESDAY;
		} else if(StringUtil.contains("WEDNESDAY", day)) {
			return Calendar.WEDNESDAY;
		} else if(StringUtil.contains("THURSDAY", day)) {
			return Calendar.THURSDAY;
		} else if(StringUtil.contains("FRIDAY", day)) {
			return Calendar.FRIDAY;
		} else if(StringUtil.contains("SATURDAY", day)) {
			return Calendar.SATURDAY;			
		}
		
		return -1;
	}

	public static Calendar getDayDiffFromToday(int dayDiff) {
		int year = TimeUtil.getCurrentYear();
		int month = TimeUtil.getCurrentMonth();
		int date = TimeUtil.getCurrentDate();
		return getCalendar(year, month, date, dayDiff);
	}

	public static int getCurrentYear() {
		return Integer.parseInt(getFormattedDate(new Date(), "YYYY"));
	}

	public static int getCurrentMonth() {
		return Integer.parseInt(getFormattedDate(new Date(), "M"));
	}

	public static int getCurrentDate() {
		return Integer.parseInt(getFormattedDate(new Date(), "d"));
	}

	public static int getLastDayOfCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DATE);
	}

	public static int getDaysDiff(CalendarDate a, CalendarDate b) {
		return TimeUtil.getDaysDiff(a.getCalendar(), b.getCalendar());
	}

	public static int getDaysDiff(Calendar a, Calendar b) {
		Long daysDiff = ChronoUnit.DAYS.between(a.toInstant(), b.toInstant());
		return daysDiff.intValue();
	}

}
