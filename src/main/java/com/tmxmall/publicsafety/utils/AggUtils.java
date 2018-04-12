package com.tmxmall.publicsafety.utils;


import net.sf.json.JSONObject;

public class AggUtils {

	
	public static String getTimeType(JSONObject message) {
		String startTime = message.getString("startTime");
		String endTime = message.getString("endTime");
		String timeType = "";
		long startTimeUnix = DateUtil.time2Unix(startTime,"yyyy-MM-dd HH:mm:ss");
		long endTimeUnix = DateUtil.time2Unix(endTime,"yyyy-MM-dd HH:mm:ss");
		long time = (endTimeUnix - startTimeUnix) / 1000;
		//小于48小时
		if(time <= 172800) {
			timeType = "1";   //按小时
		}
		//大于48小时
		if(time > 172800) {
			timeType = "2"; //按天
		}
		return timeType;
	}
	public static String getTimeType(String startTime, String endTime) {
		String timeType = "";
		long startTimeUnix = DateUtil.time2Unix(startTime,"yyyy-MM-dd HH:mm:ss");
		long endTimeUnix = DateUtil.time2Unix(endTime,"yyyy-MM-dd HH:mm:ss");
		long time = (endTimeUnix - startTimeUnix) / 1000;
		//小于48小时
		if(time <= 172800) {
			timeType = "1";   //按小时
		}
		//大于48小时
		if(time > 172800) {
			timeType = "2"; //按天
		}
		return timeType;
	}

	public static String getDateAggType(String timeType) {
		String aggParams = "";
		if("1".equals(timeType)) {
			aggParams = "hour";
		} 
		if("2".equals(timeType)) {
			aggParams = "day";
		} 
		return aggParams;
	}
	public static String getAggFormate(String dateAggType) {
		String formate = "";
		if("hour".equals(dateAggType)) {
			formate = "yyyy-MM-dd HH";
		}
		if("day".equals(dateAggType)) {
			formate = "yyyy-MM-dd";
		}
		if("month".equals(dateAggType)) {
			formate = "yyyy-MM";
		}
		if("year".equals(dateAggType)) {
			formate = "yyyy";
		}
		return formate;
	}
}
