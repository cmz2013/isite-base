package org.isite.commons.lang.utils;

import org.isite.commons.lang.Constants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * java.util.Calendar：是Java旧的日期时间API的一部分，它支持时区(Calendar.getInstance(TimeZone.getDefault()))。
 * 但Calendar月份是从0开始的，不是直观的1-12表示法，而且它是可变的，这在多线程环境下可能会导致线程安全问题。
 * java.util.Date: 它表示特定的瞬间，精确到毫秒，不提供时区信息。它依赖于默认的时区来解释日期和时间（new SimpleDateFormat(pattern).setTimeZone(getDefault())）。
 *
 * java.time.LocalDate:是Java 8引入的时间处理 API 的一部分。LocalDate 只表示日期，没有时区信息。LocalDate 是不可变的，线程安全，而且API设计得更加清晰和易于使用。
 * 使用LocalDate来获取日期，是基于系统默认的时区。需要注意的是，LocalDate 本身并不存储时区信息，它只是一个日期值。
 * java.time.LocalDateTime: 表示日期和时间，不带有时区信息。LocalDateTime 也是不可变的，线程安全，并且提供了丰富的日期和时间操作方法。
 *
 * 如果只需要处理日期，推荐使用 LocalDate；
 * 如果需要处理日期和时间，但不需要考虑时区，推荐使用 LocalDateTime；
 * 如果需要处理日期、时间和时区，应该使用 ZonedDateTime。
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

	/**
	 * 获取当前是星期几
	 */
	public static int getDayOfWeek() {
		return getDayOfWeek(LocalDate.now());
	}

	/**
	 * 获取日期是星期几
	 * 国际标准ISO 8601明确指出：每个日历星期从星期一开始，星期日为第7天。
	 */
	public static int getDayOfWeek(LocalDate date) {
		return date.getDayOfWeek().getValue();
	}

	/**
	 * 获取当前日期的月份中的第几天
	 */
	public static int getDayOfMonth() {
		return getDayOfMonth(LocalDate.now());
	}

	/**
	 * 获取指定日期的月份中的第几天
	 */
	public static int getDayOfMonth(LocalDate date) {
		return date.getDayOfMonth();
	}

	/**
	 * 获取当前月份的总天数
	 */
	public static int lengthOfMonth() {
		return lengthOfMonth(LocalDate.now());
	}

	/**
	 * 获取日期所在月份的总天数
	 */
	public static int lengthOfMonth(LocalDate date) {
		return YearMonth.from(date).lengthOfMonth();
	}

	/**
	 * 获取当前的小时
	 */
	public static int getHour() {
		return getHour(LocalDateTime.now());
	}

	/**
	 * 获取日期的小时
	 */
	public static int getHour(LocalDateTime dateTime) {
		return dateTime.getHour();
	}

	/**
	 * 获取当前的分钟
	 */
	public static int getMinute() {
		return getMinute(LocalDateTime.now());
	}

	/**
	 * 获取日期的分钟
	 */
	public static int getMinute(LocalDateTime dateTime) {
		return dateTime.getMinute();
	}

	/**
	 * 获取两个日期相差的天数
	 */
	public static long getDays(LocalDateTime inclusive, LocalDateTime exclusive) {
		return ChronoUnit.DAYS.between(inclusive, exclusive);
	}

	/**
	 * 获取两个日期相差的月份数
	 */
	public static long getMonths(LocalDateTime inclusive, LocalDateTime exclusive) {
		return ChronoUnit.MONTHS.between(inclusive, exclusive);
	}

	/**
	 * 获取当前小时的开始时间
	 */
	public static LocalDateTime startOfHour() {
		return startOfHour(LocalDateTime.now());
	}

	/**
	 * 获取小时的开始时间
	 */
	public static LocalDateTime startOfHour(LocalDateTime date) {
		//1毫秒等于 100 0000 纳秒
		return date.withMinute(Constants.ZERO).withSecond(Constants.ZERO).withNano(Constants.ZERO);
	}

	/**
	 * 获取当前小时的结束时间
	 */
	public static LocalDateTime endOfHour() {
		return endOfHour(LocalDateTime.now());
	}

	/**
	 * 获取小时的结束时间
	 */
	public static LocalDateTime endOfHour(LocalDateTime date) {
		return startOfHour(date).plusHours(Constants.ONE).minus(Constants.ONE, ChronoUnit.MILLIS);
	}


	/**
	 * 获取当前天的开始时间
	 */
	public static LocalDateTime startOfDay() {
		return startOfDay(LocalDateTime.now());
	}

	/**
	 * 获取天的开始时间
	 */
	public static LocalDateTime startOfDay(LocalDateTime date) {
		return date.with(LocalTime.MIN);
	}

	/**
	 * 获取当前天的结束时间
	 */
	public static LocalDateTime endOfDay() {
		return endOfDay(LocalDateTime.now());
	}

	/**
	 * 获取天的结束时间
	 */
	public static LocalDateTime endOfDay(LocalDateTime date) {
		return date.with(LocalTime.MAX);
	}

	/**
	 * 获取当前周的开始日期
	 */
	public static LocalDateTime startOfWeek() {
		return startOfWeek(LocalDateTime.now());
	}

	/**
	 * 获取周的开始时间
	 */
	public static LocalDateTime startOfWeek(LocalDateTime date) {
		return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
	}

	/**
	 * 获取当前周的结束时间
	 */
	public static LocalDateTime endOfWeek() {
		return endOfWeek(LocalDateTime.now());
	}

	/**
	 * 获取周的结束时间
	 */
	public static LocalDateTime endOfWeek(LocalDateTime date) {
		return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
	}

	/**
	 * 获取当前月的开始日期
	 */
	public static LocalDateTime startOfMonth() {
		return startOfMonth(LocalDateTime.now());
	}

	/**
	 * 获取月的开始时间
	 */
	public static LocalDateTime startOfMonth(LocalDateTime date) {
		return date.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
	}

	/**
	 * 获取当前月的结束时间
	 */
	public static LocalDateTime endOfMonth() {
		return endOfMonth(LocalDateTime.now());
	}

	/**
	 * 获取月的结束时间
	 * 平年的2月是28天，闰年2月是29天。
	 * 4月、6月、9月、11月各是30天。
	 * 1月、3月、5月、7月、8月、10月、12月各是31天。
	 */
	public static LocalDateTime endOfMonth(LocalDateTime date) {
		return date.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
	}

	/**
	 * 当前季度的开始时间
	 */
	public static LocalDateTime startOfQuarter() {
		return startOfQuarter(LocalDateTime.now());
	}

	/**
	 * 季度的开始时间
	 */
	public static LocalDateTime startOfQuarter(LocalDateTime date) {
		int month = (date.getMonthValue() - Constants.ONE) / Constants.THREE * Constants.THREE + Constants.ONE;
		return LocalDateTime.of(date.getYear(), month,
				Constants.ONE, Constants.ZERO, Constants.ZERO, Constants.ZERO, Constants.ZERO);
	}

	/**
	 * 当前季度的结束时间
	 */
	public static LocalDateTime endOfQuarter() {
		return endOfQuarter(LocalDateTime.now());
	}

	/**
	 * 季度的结束时间
	 */
	public static LocalDateTime endOfQuarter(LocalDateTime date) {
		int month = ((date.getMonthValue() - Constants.ONE) / Constants.THREE + Constants.ONE) * Constants.THREE;
		return date.withMonth(month).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
	}

	/**
	 * 当前年的开始时间
	 */
	public static LocalDateTime startOfYear() {
		return startOfYear(LocalDateTime.now());
	}

	/**
	 * 年的开始时间
	 */
	public static LocalDateTime startOfYear(LocalDateTime date) {
		return date.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
	}

	/**
	 * 当前年的结束时间
	 */
	public static LocalDateTime endOfYear() {
		return endOfYear(LocalDateTime.now());
	}

	/**
	 * 年的结束时间
	 */
	public static LocalDateTime endOfYear(LocalDateTime date) {
		return date.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
	}

	public static LocalDate parseDate(String source, String pattern) {
		return LocalDate.parse(source, DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDateTime parseDateTime(String source, String pattern) {
		return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(pattern));
	}

	public static String format(LocalDateTime dateTime, String pattern) {
		return dateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String format(LocalDate date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}
}
