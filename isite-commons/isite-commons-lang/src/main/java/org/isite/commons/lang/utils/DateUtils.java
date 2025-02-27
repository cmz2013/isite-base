package org.isite.commons.lang.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static java.util.TimeZone.getDefault;
import static org.isite.commons.lang.Constants.FOUR;
import static org.isite.commons.lang.Constants.NINE;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.SEVEN;
import static org.isite.commons.lang.Constants.SIX;
import static org.isite.commons.lang.Constants.TEN;
import static org.isite.commons.lang.Constants.THREE;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class DateUtils {

	/**
	 * 时间格式：yyyy-MM-dd
	 */
	public static final String PATTERN_DATE_DIVIDE = "yyyy-MM-dd";
	/**
	 * 时间格式：yyyyMMdd
	 */
	public static final String PATTERN_DATE = "yyyyMMdd";
	/**
	 * 时间格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String PATTERN_DATETIME_DIVIDE = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 时间格式：yyyyMMddHHmmss
	 */
	public static final String PATTERN_DATETIME = "yyyyMMddHHmmss";
	/**
	 * 时间格式：HH:mm:ss
	 */
	public static final String PATTERN_TIME_DIVIDE = "HH:mm:ss";
	/**
	 * 时间格式：HHmmss
	 */
	public static final String PATTERN_TIME = "HHmmss";

	private DateUtils() {
	}

	public static Calendar getCalendar() {
		return getInstance(getDefault());
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = getCalendar();
		if (null != date) {
			calendar.setTime(date);
		}
		return calendar;
	}

	public static DateFormat getDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(getDefault());
		return sdf;
	}

	public static Date parseDate(String source, String pattern) throws ParseException {
		return getDateFormat(pattern).parse(source);
	}

	public static String formatDate(Date date, String pattern) {
		return getDateFormat(pattern).format(date);
	}

	/**
	 * 获取当前周的当前天数
	 */
	public static int getDayOfWeek() {
		return getDayOfWeek(null);
	}

	/**
	 * 获取日期所在周的天数
	 * 国际标准ISO 8601明确指出：每个日历星期从星期一开始，星期日为第7天。
	 */
	public static int getDayOfWeek(Date date) {
		int day = getCalendar(date).get(DAY_OF_WEEK);
		return (ONE == day) ? SEVEN : (day - ONE);
	}

	/**
	 * 获取当前月的当前天数
	 */
	public static int getDayOfMonth() {
		return getDayOfMonth(null);
	}

	/**
	 * 获取日期所在月的天数
	 */
	public static int getDayOfMonth(Date date) {
		return getCalendar(date).get(DAY_OF_MONTH);
	}

	/**
	 * 获取当前月的总天数
	 */
	public static int getMaxDayOfMonth() {
		return getMaxDayOfMonth(null);
	}

	/**
	 * 获取日期所在月的总天数
	 */
	public static int getMaxDayOfMonth(Date date) {
		return getCalendar(date).getActualMaximum(DAY_OF_MONTH);
	}

	/**
	 * 获取当前周的星期几
	 */
	public static String getWeek() {
		return getWeek(null);
	}

	/**
	 * 获取日期所在周的星期几
	 */
	public static String getWeek(Date date) {
		return getDateFormat("E").format(null == date ? new Date(currentTimeMillis()) : date);
	}

	/**
	 * 获取当前的小时
	 */
	public static int getHourOfDay() {
		return getHourOfDay(getCalendar());
	}

	/**
	 * 获取日期的小时
	 */
	public static int getHourOfDay(Calendar calendar) {
		return calendar.get(HOUR_OF_DAY);
	}

	/**
	 * 获取当前的分钟
	 */
	public static int getMinute() {
		return getMinute(getCalendar());
	}

	/**
	 * 获取日期的分钟
	 */
	public static int getMinute(Calendar calendar) {
		return calendar.get(MINUTE);
	}

	/**
	 * 获取两个日期相差的月份数
	 */
	public static int getMonth(Date minDate, Date maxDate) {
		Calendar minTime = getCalendar(minDate);
		Calendar maxTime = getCalendar(maxDate);
		int years = maxTime.get(YEAR) - minTime.get(YEAR);
		int months = years * 12 + maxTime.get(MONTH) - minTime.get(MONTH);
		minTime.add(YEAR, years);
		minTime.add(MONTH, months);

		if (minTime.getTimeInMillis()- maxTime.getTimeInMillis() > ZERO) {
			months = months - ONE;
		}
		return months;
	}

	/**
	 * 获得当前秒的开始时间
	 */
	public static Date getStartTimeOfSecond() {
		return getStartTimeOfSecond(null);
	}

	/**
	 * 获得秒的开始时间
	 */
	public static Date getStartTimeOfSecond(Date date) {
		Calendar calendar = getCalendar(date);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 获得当前分钟的开始时间
	 */
	public static Date getStartTimeOfMinute() {
		return getStartTimeOfMinute(null);
	}

	/**
	 * 获得分钟的开始时间
	 */
	public static Date getStartTimeOfMinute(Date date) {
		Calendar calendar = getCalendar(date);
		calendar.set(SECOND, ZERO);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 获取当前分钟的结束时间
	 */
	public static Date getEndTimeOfMinute() {
		Calendar calendar = getCalendar();
		calendar.set(SECOND, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获得当前小时的开始时间
	 */
	public static Date getStartTimeOfHour() {
		return getStartTimeOfHour(null);
	}

	/**
	 * 获得小时的开始时间
	 */
	public static Date getStartTimeOfHour(Date date) {
		Calendar calendar = getCalendar(date);
		calendar.set(SECOND, ZERO);
		calendar.set(MINUTE, ZERO);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 获取当前小时的结束时间
	 */
	public static Date getEndTimeOfHour() {
		Calendar calendar = getCalendar();
		calendar.set(SECOND, 59);
		calendar.set(MINUTE, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取当日的开始时间
	 */
	public static Date getStartTimeOfDay() {
		return getStartTimeOfDay(null);
	}

	/**
	 * 获取天的开始时间
	 */
	public static Date getStartTimeOfDay(Date date) {
		Calendar calendar = getCalendar(date);
		calendar.set(HOUR_OF_DAY, ZERO);
		calendar.set(SECOND, ZERO);
		calendar.set(MINUTE, ZERO);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 获取当日的结束时间
	 */
	public static Date getEndTimeOfDay() {
		Calendar calendar = getCalendar();
		calendar.set(HOUR_OF_DAY, 23);
		calendar.set(MINUTE, 59);
		calendar.set(SECOND, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取当前周的开始时间
	 */
	public static Date getStartTimeOfWeek() {
		return getStartTimeOfWeek(null);
	}

	/**
	 * 获取周的开始时间
	 */
	public static Date getStartTimeOfWeek(Date date) {
		Calendar calendar = getCalendar(date);
		int weekday = calendar.get(DAY_OF_WEEK) - 2;
		calendar.add(DATE, -weekday);
		calendar.set(HOUR_OF_DAY, ZERO);
		calendar.set(SECOND, ZERO);
		calendar.set(MINUTE, ZERO);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 获取当前周的结束时间
	 */
	public static Date getEndTimeOfWeek() {
		Calendar calendar = getCalendar();
		int weekday = calendar.get(DAY_OF_WEEK);
		calendar.add(DATE, 8 - weekday);
		calendar.set(HOUR_OF_DAY, 23);
		calendar.set(SECOND, 59);
		calendar.set(MINUTE, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取当前月的开始时间
	 */
	public static Date getStartTimeOfMonth() {
		return getStartTimeOfMonth(null);
	}

	/**
	 * 获取月的开始时间
	 */
	public static Date getStartTimeOfMonth(Date date) {
		Calendar calendar = getCalendar(date);
		calendar.set(DATE, ONE);
		calendar.set(HOUR_OF_DAY, ZERO);
		calendar.set(SECOND, ZERO);
		calendar.set(MINUTE, ZERO);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 获取当前月的结束时间
	 */
	public static Date getEndTimeOfMonth() {
		Calendar calendar = getCalendar();
		calendar.set(DATE, 1);
		calendar.add(MONTH, 1);
		calendar.add(DATE, -1);
		calendar.set(HOUR_OF_DAY, 23);
		calendar.set(SECOND, 59);
		calendar.set(MINUTE, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 当前季度的开始时间
	 */
	public static Date getStartTimeOfQuarter() {
		return getStartTimeOfQuarter(null);
	}

	/**
	 * 季度的开始时间
	 */
	public static Date getStartTimeOfQuarter(Date date) {
		Calendar calendar = getCalendar(date);
		int currentMonth = calendar.get(MONTH) + ONE;
		if (currentMonth >= ONE && currentMonth <= THREE) {
			calendar.set(MONTH, ZERO);
		} else if (currentMonth >= FOUR && currentMonth <= SIX) {
			calendar.set(MONTH, THREE);
		} else if (currentMonth >= SEVEN && currentMonth <= NINE) {
			calendar.set(MONTH, FOUR);
		} else if (currentMonth >= TEN && currentMonth <= 12) {
			calendar.set(MONTH, NINE);
		}
		calendar.set(DATE, ONE);
		calendar.set(HOUR_OF_DAY, ZERO);
		calendar.set(SECOND, ZERO);
		calendar.set(MINUTE, ZERO);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 当前季度的结束时间
	 * 平年的2月是28天，闰年2月是29天。
	 * 4月、6月、9月、11月各是30天。
	 * 1月、3月、5月、7月、8月、10月、12月各是31天。
	 */
	public static Date getEndTimeOfQuarter() {
		Calendar calendar = getCalendar();
		int currentMonth = calendar.get(MONTH) + 1;
		if (currentMonth >= 1 && currentMonth <= 3) {
			calendar.set(MONTH, 2);
			calendar.set(DATE, 31);
		} else if (currentMonth >= 4 && currentMonth <= 6) {
			calendar.set(MONTH, 5);
			calendar.set(DATE, 30);
		} else if (currentMonth >= 7 && currentMonth <= 9) {
			calendar.set(MONTH, 8);
			calendar.set(DATE, 30);
		} else if (currentMonth >= 10 && currentMonth <= 12) {
			calendar.set(MONTH, 11);
			calendar.set(DATE, 31);
		}
		calendar.set(HOUR_OF_DAY, 23);
		calendar.set(SECOND, 59);
		calendar.set(MINUTE, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取当前年的开始时间
	 */
	public static Date getStartTimeOfYear() {
		return getStartTimeOfYear(null);
	}

	/**
	 * 获取年的开始时间
	 */
	public static Date getStartTimeOfYear(Date date) {
		Calendar calendar = getCalendar(date);
		calendar.set(MONTH, ZERO);
		calendar.set(DATE, ONE);
		calendar.set(HOUR_OF_DAY, ZERO);
		calendar.set(SECOND, ZERO);
		calendar.set(MINUTE, ZERO);
		calendar.set(MILLISECOND, ZERO);
		return calendar.getTime();
	}

	/**
	 * 当前年的结束时间
	 */
	public static Date getEndTimeOfYear() {
		Calendar calendar = getCalendar();
		calendar.add(MONTH, 11);
		calendar.set(DATE, 31);
		calendar.set(HOUR_OF_DAY, 23);
		calendar.set(SECOND, 59);
		calendar.set(MINUTE, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取当前的前/后半年的开始时间
	 */
	public static Date getStartTimeOfHalfYear() {
		return getStartTimeOfHalfYear(null);
	}

	/**
	 * 获取前/后半年的开始时间
	 */
	public static Date getStartTimeOfHalfYear(Date date) {
		Calendar calendar = getCalendar(date);
		int currentMonth = calendar.get(MONTH) + 1;
		if (currentMonth >= 1 && currentMonth <= 6) {
			calendar.set(MONTH, 0);
		} else if (currentMonth >= 7 && currentMonth <= 12) {
			calendar.set(MONTH, 6);
		}
		calendar.set(DATE, 1);
		calendar.set(HOUR_OF_DAY, 0);
		calendar.set(SECOND, 0);
		calendar.set(MINUTE, 0);
		calendar.set(MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当前的前/后半年的结束时间
	 */
	public static Date getEndTimeOfHalfYear() {
		Calendar calendar = getCalendar();
		int currentMonth = calendar.get(MONTH) + 1;
		if (currentMonth >= 1 && currentMonth <= 6) {
			calendar.set(MONTH, 5);
			calendar.set(DATE, 30);
		} else if (currentMonth >= 7 && currentMonth <= 12) {
			calendar.set(MONTH, 11);
			calendar.set(DATE, 31);
		}
		calendar.set(HOUR_OF_DAY, 23);
		calendar.set(SECOND, 59);
		calendar.set(MINUTE, 59);
		calendar.set(MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取n小时前的时间
	 */
	public static Date getTimeBeforeHour(int n) {
		return getTimeBeforeHour(null, n);
	}

	public static Date getTimeBeforeHour(Date date, int n) {
		Calendar calendar = getCalendar(date);
		calendar.set(HOUR, calendar.get(HOUR) - n);
		return calendar.getTime();
	}

	/**
	 * 获取n天前的时间
	 */
	public static Date getTimeBeforeDay(int n) {
		return getTimeBeforeDay(null, n);
	}

	public static Date getTimeBeforeDay(Date date, int n) {
		Calendar calendar = getCalendar(date);
		calendar.set(DATE, calendar.get(DATE) - n);
		return calendar.getTime();
	}

	/**
	 * 获取n个月前的时间
	 */
	public static Date getTimeBeforeMonth(int n) {
		return getTimeBeforeMonth(null, n);
	}

	public static Date getTimeBeforeMonth(Date date, int n) {
		Calendar calendar = getCalendar(date);
		calendar.set(MONTH, calendar.get(MONTH) - n);
		return calendar.getTime();
	}

	/**
	 * 获取n年前的时间
	 */
	public static Date getTimeBeforeYear(int n) {
		return getTimeBeforeYear(null, n);
	}

	public static Date getTimeBeforeYear(Date date, int n) {
		Calendar calendar = getCalendar(date);
		calendar.set(YEAR, calendar.get(YEAR) - n);
		return calendar.getTime();
	}

	/**
	 * 获取n个月后的时间
	 */
	public static Date getTimeAfterMonth(int n) {
		return getTimeAfterMonth(null, n);
	}

	public static Date getTimeAfterMonth(Date date, int n) {
		Calendar calendar = getCalendar(date);
		calendar.set(MONTH, calendar.get(MONTH) + n);
		return calendar.getTime();
	}

	/**
	 * 获取n天后的时间
	 */
	public static Date getTimeAfterDay(int n) {
		return getTimeAfterDay(null, n);
	}

	public static Date getTimeAfterDay(Date date, int n) {
		Calendar calendar = getCalendar(date);
		calendar.set(DAY_OF_MONTH, calendar.get(DAY_OF_MONTH) + n);
		return calendar.getTime();
	}
}
