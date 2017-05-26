package com.amazon.main.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/**
	 * ��ȡ��ǰ��������,Ӣ�ĵ�
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
	 * ��ȡ��ǰ��������,���ֵ�
	 * 
	 * @return
	 */
	public static String[] getDate() {
		String[] date = new String[3];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// �������ڸ�ʽ
		String format = df.format(new Date());
		date[0] = format.substring(0, 4);// ��
		date[1] = format.substring(5, 7);// ��
		date[2] = format.substring(8, 10);// ��
		return date;
	}

	/**
	 * ��ȡ�ϸ��µ��·�Ӣ��
	 * 
	 * @return
	 */
	public static String lastMonth() {
		SimpleDateFormat format = new SimpleDateFormat(
				"E, dd MMM yyyy HH:mm:ss z", Locale.UK);
		format.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
		// ��ȡǰ�µĵ�һ��
		Calendar cal_1 = Calendar.getInstance();// ��ȡ��ǰ����
		cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// ����Ϊ1��,��ǰ���ڼ�Ϊ���µ�һ��
		String firstDay = format.format(cal_1.getTime());
		// System.out.println("-----1------firstDay:" + firstDay);
		// // ��ȡǰ�µ����һ��
		// Calendar cale = Calendar.getInstance();
		// cale.set(Calendar.DAY_OF_MONTH, 0);// ����Ϊ1��,��ǰ���ڼ�Ϊ���µ�һ��
		// String lastDay = format.format(cale.getTime());
		System.out.println("-----lastMonth---" + firstDay.substring(8, 11));
		return firstDay.substring(8, 11);
	}

	/**
	 * ��ȡ�ϸ��µ��·ݣ����ֵ�
	 * 
	 * @return
	 */
	public static String lastMonthNum() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cl = Calendar.getInstance();
		// cl.setTime(dateNow);
		// cl.add(Calendar.DAY_OF_YEAR, -1);
		// һ��
		// cl.add(Calendar.WEEK_OF_YEAR, -1);
		// һ��
		cl.add(Calendar.MONTH, -1);
		// �������㣬֮ǰһ����,�����2���£���ô-1-----����Ϊ-2
		Date dateFrom = cl.getTime();
		return sdf.format(dateFrom).substring(4, 6);
	}
}
