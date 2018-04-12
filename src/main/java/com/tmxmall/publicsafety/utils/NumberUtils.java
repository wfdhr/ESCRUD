package com.tmxmall.publicsafety.utils;

import java.text.DecimalFormat;

/**
 * 处理数字的工具类
 * @author wangfei
 *
 */
public class NumberUtils {

	/**
	 * 保留两位小数
	 * @param number
	 * @return
	 */
	public static String n2(float d) {
		DecimalFormat dFormat = new DecimalFormat("0.00%");
		return dFormat.format(d);
	}
	
	/**
	 * double转String,不保留小数s
	 */
//	public static String double2String(double d) {
//	}
}
