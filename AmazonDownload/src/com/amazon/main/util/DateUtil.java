package com.amazon.main.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/**
	 * 获取当前的年月日,英文的
	 * 
	 * @return
	 */
	public static String[] currencyDate() {
		String[] dateStr = new String[3];
		Date date = new Date();
		String dateString = date.toString();
		int length = dateString.length(); // xx:xx:xx
		String currencyDate = dateString.substring(4, 7) + " "
				+ dateString.substring(8, 10) + ", "
				+ dateString.substring(length - 4, length);
		dateStr[0] = dateString.substring(length - 4, length);
		dateStr[1] = dateString.substring(4, 7);
		dateStr[2] = dateString.substring(8, 10);
		System.out.println("-----currencyDate---" + currencyDate);
		return dateStr;
	}

	/**
	 * 获取当前的年月日,数字的
	 * 
	 * @return
	 */
	public static String[] getDate() {
		String[] date = new String[3];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String format = df.format(new Date());
		date[0] = format.substring(0, 4);// 年
		date[1] = format.substring(5, 7);// 月
		date[2] = format.substring(8, 10);// 日
		return date;
	}

	/**
	 * 获取上个月的月份英文
	 * 
	 * @return
	 */
	public static String lastMonth() {
		SimpleDateFormat format = new SimpleDateFormat(
				"E, dd MMM yyyy HH:mm:ss z", Locale.UK);
		format.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
		// 获取前月的第一天
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDay = format.format(cal_1.getTime());
		// System.out.println("-----1------firstDay:" + firstDay);
		// // 获取前月的最后一天
		// Calendar cale = Calendar.getInstance();
		// cale.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
		// String lastDay = format.format(cale.getTime());
		System.out.println("-----lastMonth---" + firstDay.substring(8, 11));
		return firstDay.substring(8, 11);
	}

	/**
	 * 获取上个月的月份，数字的
	 * 
	 * @return
	 */
	public static String lastMonthNum() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cl = Calendar.getInstance();
		// cl.setTime(dateNow);
		// cl.add(Calendar.DAY_OF_YEAR, -1);
		// 一天
		// cl.add(Calendar.WEEK_OF_YEAR, -1);
		// 一周
		cl.add(Calendar.MONTH, -1);
		// 从现在算，之前一个月,如果是2个月，那么-1-----》改为-2
		Date dateFrom = cl.getTime();
		return sdf.format(dateFrom).substring(4, 6);
	}
}
