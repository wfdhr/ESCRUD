package com.tmxmall.publicsafety.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil
{
	private static DateFormat formatA = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat formatB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat formatC = new SimpleDateFormat();
	public static String getForDate(String strformat, Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat(strformat);
		return df.format(date);
	}
	
	

	public static String formateDate(Date date, String format)
	{
		if ((date == null) || ("".equals(date))) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static long getTimeLong(Date date)
	{
		String str = formateDate(date, "yyyyMMddHHmmss");
		if ((str == null) || ("".equals(str))) {
			return 0L;
		}
		return Long.valueOf(str).longValue();
	}

	public static Date getDate(String strformat, String timeStr)
	{
		SimpleDateFormat df = new SimpleDateFormat(strformat);
		try
		{
			return df.parse(timeStr);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return new Date(timeStr);
	}

	public static String unix2Time(Date date)
	{
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String strDate = sd.format(date);
		return strDate;
	}

	public static String unix2Time(long timestamp,String format)
	{
		String date = new SimpleDateFormat(format).format(new Date(timestamp));
		return date;
	}

	public static long time2Unix(String date,String format)
	{
		long epoch = 0L;
		try
		{
			epoch = new SimpleDateFormat(format).parse(date).getTime();
		}
		catch (ParseException localParseException) {}
		return epoch;
	}

	public static long timeShort2Unix(String date)
	{
		long epoch = 0L;
		try
		{
			epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
		}
		catch (ParseException localParseException) {}
		return epoch;
	}

	public static long getLongTimestamp8(Timestamp time)
	{
		String str = getForDate("yyyyMMdd", time);
		return Long.parseLong(str);
	}

	public static long getLongTimestamp16(Timestamp time)
	{
		String str = getForDate("yyyyMMddHHmmss", time);
		return Long.parseLong(str);
	}

	public static Timestamp getLongTimestamp8(String time)
	{
		return new Timestamp(getDateYYYYMMDD(str2Format8(time)).getTime());
	}

	public static Timestamp getLongTimestamp16(String time)
	{
		Date date = null;
		try
		{
			date = formatB.parse(str2Format16(time));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return new Timestamp(date.getTime());
	}

	public static Date getDateYYYYMMDD(String timeStr)
	{
		Date date = null;
		try
		{
			date = formatA.parse(timeStr);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}

	private static String str2Format8(String time)
	{
		StringBuilder sb = new StringBuilder(time);
		sb.insert(4, '-');
		sb.insert(7, '-');
		sb.insert(10, '-');
		return sb.toString();
	}

	private static String str2Format16(String time)
	{
		StringBuilder sb = new StringBuilder(time);
		sb.insert(4, '-');
		sb.insert(7, '-');
		sb.insert(10, ' ');
		sb.insert(13, ':');
		sb.insert(16, ':');
		return sb.toString();
	}

	public static String getDate8(String time)
	{
		if ((time == null) || ("".equals(time)) || (time.length() < 8)) {
			return null;
		}
		return time.substring(0, 8);
	}

	public static String getToday8()
	{
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
	}

	public static boolean isToday(String str)
	{
		Timestamp today = getLongTimestamp8(getToday8());
		Timestamp date = getLongTimestamp8(str);
		return today.equals(date);
	}

	public static boolean isInWeek(String str)
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("China/Beijing"));
		Timestamp date = getLongTimestamp8(str);
		Timestamp today = getLongTimestamp8(getToday8());
		calendar.setTime(today);
		int dow = calendar.get(7);
		int sunGap = 7 - dow;
		Timestamp sunday = getDayBefore(today, sunGap);
		dow = dow * -1 + 1;
		Timestamp monday = getDayBefore(today, dow);
		if ((date.compareTo(monday) > -1) && (date.compareTo(sunday) < 1)) {
			return true;
		}
		return false;
	}

	public static boolean isInMonth(String str)
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("China/Beijing"));
		Timestamp date = getLongTimestamp8(str);
		Timestamp today = getLongTimestamp8(getToday8());
		calendar.setTime(today);
		int dow = calendar.get(5) + 1;
		int total = calendar.getActualMaximum(5);
		int gap = total - dow;
		Timestamp last = getDayBefore(today, gap);
		dow = -1 * dow + 1;
		Timestamp first = getDayBefore(today, dow);
		if ((date.compareTo(first) > -1) && (date.compareTo(last) < 1)) {
			return true;
		}
		return false;
	}

	public static boolean isBeforeDays(String str, int days)
	{
		Timestamp today = getLongTimestamp8(getToday8());
		Timestamp before = getDayBefore(today, (days - 1) * -1);
		Timestamp date = getLongTimestamp8(str);
		if ((date.compareTo(before) > -1) && (date.compareTo(today) < 1)) {
			return true;
		}
		return false;
	}

	public static Timestamp getDayBefore(Timestamp init, int n)
	{
		long day = 86400000L;
		long time = init.getTime() + day * n;
		if (time < 0L) {
			return null;
		}
		return new Timestamp(time);
	}

	public static String nextDay(String date)
	{
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try
		{
			calendar.setTime(format.parse(date));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		calendar.add(6, 1);
		return getForDate("MMdd", calendar.getTime());
	}

	public static String prevDay(String date)
	{
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try
		{
			calendar.setTime(format.parse(date));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		calendar.add(6, -1);
		return getForDate("MMdd", calendar.getTime());
	}

	public static String nextYear(String date)
	{
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try
		{
			calendar.setTime(format.parse(date));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		calendar.add(6, 1);
		return getForDate("yyyy", calendar.getTime());
	}

	public static String prevYear(String date)
	{
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		try
		{
			calendar.setTime(format.parse(date));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		calendar.add(6, -1);
		return getForDate("yyyy", calendar.getTime());
	}

	public static Date addGapTime(Date startTime, Date gapTime)
	{
		Calendar timerCalender = Calendar.getInstance();
		timerCalender.setTime(gapTime);
		GregorianCalendar start = new GregorianCalendar();
		start.setTime(startTime);
		start.add(11, timerCalender.get(11));
		start.add(12, timerCalender.get(12));
		start.add(13, timerCalender.get(13));
		start.add(14, timerCalender.get(14));
		return start.getTime();
	}

	public static Date addOneSecond(Date startTime)
	{
		GregorianCalendar start = new GregorianCalendar();
		start.setTime(startTime);
		start.add(13, 1);
		return start.getTime();
	}

	public static Date delOneSecond(Date startTime)
	{
		GregorianCalendar start = new GregorianCalendar();
		start.setTime(startTime);
		start.add(13, -1);
		return start.getTime();
	}

	public static Date addOneMillSecond(Date startTime)
	{
		GregorianCalendar start = new GregorianCalendar();
		start.setTime(startTime);
		start.add(14, 1);
		return start.getTime();
	}

	public static Date delOneMillSecond(Date startTime)
	{
		GregorianCalendar start = new GregorianCalendar();
		start.setTime(startTime);
		start.add(14, -1);
		return start.getTime();
	}

	public static Date delGapTime(Date endTime, Date gapTime)
	{
		Calendar timerCalender = Calendar.getInstance();
		timerCalender.setTime(gapTime);
		GregorianCalendar end = new GregorianCalendar();
		end.setTime(endTime);
		end.add(11, -timerCalender.get(11));
		end.add(12, -timerCalender.get(12));
		end.add(13, -timerCalender.get(13));
		return end.getTime();
	}
	public static Date getNeedTime(int hour,int minute,int second,int addDay,int ...args){
		Calendar calendar = Calendar.getInstance();
		if(addDay != 0){
			calendar.add(Calendar.DATE,addDay);
		}
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE,minute);
		calendar.set(Calendar.SECOND,second);
		if(args.length==1){
			calendar.set(Calendar.MILLISECOND,args[0]);
		}
		return calendar.getTime();
	}
	public static long date2timeMillis2(Date date) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取插入时间
		String time=sdf.format(date);  
		Date parse = sdf.parse(time);  
		return parse.getTime();
	}
	/**
	 * 获取几天前的日期
	 * @param past
	 * @return
	 */
	public static String getPastDate(int past) {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);  
		Date today = calendar.getTime();  
		String result = format.format(today);  
		return result;  
	} 
	/**
	 * 获取本月第一天的0点0分0秒
	 */
	public static String getFirstDayMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Calendar c = Calendar.getInstance();  
		c.add(Calendar.MONTH, 0);  
		c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天  
		//将小时至0  
		c.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		c.set(Calendar.MINUTE, 0);  
		//将秒至0  
		c.set(Calendar.SECOND,0);  
		//将毫秒至0  
		c.set(Calendar.MILLISECOND, 0);
		return sdf.format(new Date(c.getTimeInMillis()));
	}
	/**
	 * 获取本周的第一天的0点0分0秒
	 */
	public static String getFirstDayWeek(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
		if (1 == dayWeek) {  
			cal.add(Calendar.DAY_OF_MONTH, -1);  
		}  
		// System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期  
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
		cal.setFirstDayOfWeek(Calendar.MONDAY);  
		// 获得当前日期是一个星期的第几天  
		int day = cal.get(Calendar.DAY_OF_WEEK);  
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
		String imptimeBegin = sdf.format(cal.getTime());  
		return imptimeBegin;
	}

	public static long getUnixByHour(Date date,int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);//date 换成已经已知的Date对象
		cal.add(Calendar.HOUR_OF_DAY, hour);// before 8 hour
		return cal.getTime().getTime();
	}
	
	public static String formatStr(String time,String formate) {
		try {
			Date date = formatB.parse(time);
			formatC.applyPattern(formate);
			return formatC.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getneedDate(int hour,int min,int sec,int mill,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE,min);
		calendar.set(Calendar.SECOND,sec);
		calendar.set(Calendar.MILLISECOND,mill);
		calendar.add(Calendar.DAY_OF_YEAR,day);
		Date date = calendar.getTime();
		String formatDate = formateDate(date, "yyyy-MM-dd HH:mm:ss");
		return formatDate;
	}
	public static Date getneedDateStr(int hour,int min,int sec,int mill,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE,min);
		calendar.set(Calendar.SECOND,sec);
		calendar.set(Calendar.MILLISECOND,mill);
		calendar.add(Calendar.DAY_OF_YEAR,day); 
		Date date = calendar.getTime();
		return date;
	}
	public static String getAfterDate(Date d,int past) {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);  
		Date today = calendar.getTime();  
		String result = format.format(today);  
		return result;  
	} 
	

}
