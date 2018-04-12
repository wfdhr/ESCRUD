package com.tmxmall.publicsafety.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.springframework.stereotype.Component;
/**
 * 国家工具类
 * @author Administrator
 *
 */
@Component
public class PropertiesUtilsForEsData {

	/**
	 * 国家中英切换
	 * @param countryNameZh
	 * @throws IOException 
	 */
	public String getCountryNameEn(String countryNameZh) throws IOException {
		InputStream in = PropertiesUtilsForEsData.class.getClassLoader().getResourceAsStream("country.properties"); 
		Properties props = new Properties();
		props.load(in);
		return props.getProperty(countryNameZh);
	}
	/**
	 * 更正不正确的参数
	 * @throws IOException 
	 */
	public String correctionsParms(String params) throws IOException {
		Map<String,String> map = new HashMap<>();
		InputStream in = PropertiesUtilsForEsData.class.getClassLoader().getResourceAsStream("correctionsParams.properties"); 
		Properties props = new Properties();
		props.load(in);
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
		while(iterator.hasNext()) {
			Entry<Object, Object> next = iterator.next();
			map.put((String)next.getKey(), (String)next.getValue());
		}
		if(map.containsKey(params)) {
			return map.get(params);
		}
		return params;
	}

	/**
	 * 获取媒体权重对应的名称
	 * @return
	 * @throws IOException 
	 */
	public String getmediaLevelName(String mediaLevel) throws IOException {
		Map<String,String> map = new HashMap<>();
		InputStream in = PropertiesUtilsForEsData.class.getClassLoader().getResourceAsStream("mediaLevel.properties"); 
		Properties props = new Properties();
		props.load(in);
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
		while(iterator.hasNext()) {
			Entry<Object, Object> next = iterator.next();
			map.put((String)next.getKey(), (String)next.getValue());
		}
		if(map.containsKey(mediaLevel)) {
			return map.get(mediaLevel);
		}
		return mediaLevel;
	}
	

	/**
	 * 获取es的参数字段
	 * @throws IOException 
	 */
	public String[] getEsParamsFields(String path) throws IOException {
		List<String> list = new ArrayList<String>();
		InputStream in = PropertiesUtilsForEsData.class.getClassLoader().getResourceAsStream(path); 
		Properties props = new Properties();
		props.load(in);
		Iterator<String> it = props.stringPropertyNames().iterator();
		while(it.hasNext()) {
			list.add(it.next());
		}
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 获取媒体类型
	 * @param mediaName
	 * @return
	 * @throws IOException
	 */
	public String getMediaName(String mediaName) throws IOException {
		InputStream in = PropertiesUtilsForEsData.class.getClassLoader().getResourceAsStream("mediaName.properties"); 
		Properties props = new Properties();
		props.load(in);
		return props.getProperty(mediaName);
	}
	
}
