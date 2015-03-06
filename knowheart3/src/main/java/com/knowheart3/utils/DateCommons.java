package com.knowheart3.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateCommons {

	private static long startTime;
	private static long endTime;

	/**
	 * 算法计时开始
	 */
	public static void algorithmStart() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * 返回算法消耗的时间（eg1.2秒）
	 *
	 * @return
	 */
	public static String algorithmFinish() {
		endTime = System.currentTimeMillis();
		return String.valueOf(((endTime - startTime) / 1000.0));
	}

	public static String Date2String(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
			return sdf.format(date);
		}
		return "";
	}

	public static Date String2Date(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		Date currentdate = null;
		try {
			currentdate = sdf.parse(date);
		} catch (ParseException e) {
		}
		return currentdate;
	}

	public static Date Date2Date(Date date) {
		return String2Date(Date2String(date));
	}

	public static String Date2String(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
			return sdf.format(date);
		}
		return "";
	}

	public static Date String2Date(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
		Date currentdate = null;
		if (date == null || date.trim().equals("")) {
			return null;
		}
		try {
			currentdate = sdf.parse(date);
		} catch (ParseException e) {
		}
		return currentdate;
	}

	public static Date Date2Date(Date date, String pattern) {
		return String2Date(Date2String(date, pattern), pattern);
	}

	/**
	 * 判断是否符合日期规范
	 *
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static boolean isValidDate(String s, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if (s == null || s.trim().equals("")) {
			return true;
		}
		try {
			return sdf.format(sdf.parse(s)).equals(s);
		} catch (Exception e) {
			return false;
		}
	}

	public static Long StringToLong(String s) {
		if ("".equals(s) || null == s) {
			return null;
		} else {
			return Long.parseLong(s);
		}
	}

	public static Integer StringToInt(String s) {
		if ("".equals(s) || null == s) {
			return null;
		} else {
			return Integer.parseInt(s);
		}
	}

	/**
	 * 获得两个日期之间的所有日期
	 */
	public static Date[] getDateArrays(Date start, Date end, int calendarType) {
		List<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		Date tmpDate = calendar.getTime();
		long endTime = end.getTime();
		while (tmpDate.before(end) || tmpDate.getTime() == endTime) {
			ret.add(calendar.getTime());
			calendar.add(calendarType, 1);
			tmpDate = calendar.getTime();
		}
		Date[] dates = new Date[ret.size()];
		return ret.toArray(dates);
	}

	/**
	 * 获取明天的日期
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getTomorrow(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date tomorrow = new Date(date.getTime() + 1000 * 60 * 60 * 24);
		String strDate = df.format(tomorrow);
		try {
			tomorrow = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tomorrow;
	}

	public static Date getDefineDay(Date date, int num){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date defineDay = new Date(date.getTime() + 1000 * 60 * 60 * 24 * num);
		String strDate = df.format(defineDay);
		try {
			defineDay = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return defineDay;
	}

	/**
	 * 获得今天的00:00分
	 */
	public static Date getZeroOfToday() {
		return getZeroOfOneday(new Date());
	}

	/**
	 * 获得某一天的00:00分
	 */
	public static Date getZeroOfOneday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获得某一天的00:01分
	 */
	public static Date getFirstMinuteOfOneday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获得今天的23:59分
	 */
	public static Date getLatestTimeOfToday() {
		return getLatestTimeOfOneday(new Date());
	}

	/**
	 * 获得某一天的23:59分
	 */
	public static Date getLatestTimeOfOneday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取i小时之后的时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDateAfterHour(Date date, int hour) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.HOUR, now.get(Calendar.HOUR) + hour);
		return now.getTime();
	}

	/**
	 * 获取i小时之前的时间
	 *
	 * @param date
	 * @return date - i小时
	 */
	public static Date getDateBeforeHour(Date date, int hour) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.HOUR, now.get(Calendar.HOUR) - hour);
		return now.getTime();
	}

	/**
	 * 判断日期是否在2个日期之间
	 *
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @return 如果等于开始时间或者结束时间，也返回true
	 */
	public static boolean isDateDuringPeriod(Date date, Date startDate, Date endDate) {
		if (date.equals(startDate) || date.equals(endDate))
			return true;
		if (date.before(endDate) && date.after(startDate))
			return true;
		return false;
	}

	/**
	 * date1是否比date2早
	 *
	 * @param date1
	 * @param date2
	 *            如果date1是null
	 * @return
	 */
	public static boolean isBefore(Date date1, Date date2) {
		boolean isBefore = false;
		if (date1 != null && date2 == null) {
			isBefore = true;
		}

		if (date1 != null && date2 != null) {
			isBefore = date1.before(date2);
		}

		if (date1 == null && date2 != null) {
			isBefore = false;
		}

		if (date1 == null && date2 == null) {
			isBefore = false;
		}
		return isBefore;
	}

	/**
	 * 获取两个日期之间相差小时数
	 */
	public static double getDifferHour(Date start, Date end) {
		if (start == null || end == null) {
			return 0;
		}
		Calendar calendarStart = Calendar.getInstance();
		calendarStart.setTime(start);
		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTime(end);
		double hour = ((double) calendarEnd.getTimeInMillis() - (double) calendarStart.getTimeInMillis()) / (1000 * 60 * 60);
		return hour;
	}

	public static boolean isAfter(Date date1, Date date2) {
		boolean isAfter = false;
		if (date1 != null && date2 == null) {
			isAfter = true;
		}

		if (date1 != null && date2 != null) {
			isAfter = date1.after(date2);
		}

		if (date1 == null && date2 != null) {
			isAfter = false;
		}

		if (date1 == null && date2 == null) {
			isAfter = false;
		}
		return isAfter;
	}

	/**
	 * 获得第二天的23:59分
	 */
	public static Date getLatestTimeOfTomorrow() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND,59);
		calendar.set(Calendar.MILLISECOND,59);
		return calendar.getTime();
	}
}
