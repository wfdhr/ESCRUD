package com.tmxmall.publicsafety.utils;


import net.sf.json.JSONObject;

public class Constant
{
	public static final int QUERY_PAGE_SIZE_DEFAULT = 10;
	public static final String CLUSER_NAME = "xinhua";
	public static final String ES_IPS = "172.31.187.165";
	public static final int QUERY_PAGE_NO_DEFAULT = 1;
	public static final String QUERY_INDEX_TYPE_ALL = "typeAll";
	public static final String QUERY_SORT_ORDER_ASC = "asc";
	public static final String QUERY_SORT_ORDER_DESC = "desc";
	public static final String QUERY_OPERATOR_AND = " AND ";
	public static final String QUERY_OPERATOR_OR = " OR ";
	public static final String QUERY_OPERATOR_NOT = " NOT ";
	public static final String QUERY_RESULT_FRONT = "front";
	public static final String QUERY_RESULT_ANALYSIS = "analysis";
	public static final String QUERY_RESULT_DETAIL = "detail";
	public static final String QUERY_OPERATOR_AND2 = "_AND_";
	public static final String QUERY_OPERATOR_OR2 = "_OR_";
	public static final String QUERY_OPERATOR_NOT2 = "_NOT_";

	public static boolean isNullKey(JSONObject json, String key)
	{
		return (!json.containsKey(key)) || (json.get(key) == null);
	}

}
