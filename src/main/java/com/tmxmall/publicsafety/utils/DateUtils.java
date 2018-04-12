package com.tmxmall.publicsafety.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static Date formatString2Date(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dates = sdf.parse(date);
		return dates; 
	}
	
	public static String unix2Date(long unix,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date(unix);
		return sdf.format(date);
	}
	
	public static Long String2GetTime(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long getTime = sdf.parse(date).getTime();
		return getTime; 
	}
	
	public static String unix2String(Long date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String day = sdf.format(new Date(date));
		return day; 
	}

}
