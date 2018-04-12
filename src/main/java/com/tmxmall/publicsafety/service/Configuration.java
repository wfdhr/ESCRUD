package com.tmxmall.publicsafety.service;

import java.util.Properties;

import com.tmxmall.publicsafety.utils.FileUtil;

public class Configuration
{
  public static String INDEX_SERVER_ADDRESS = "";
  public static String CLUSTER_NAME;
  public static String TOTAL_INDEX_SERVER_ADDRESS = "";
  public static String TOTAL_CLUSTER_NAME;
  public static String INDEX_NAME;
  public static String INDEX_TYPE_ARTICLE;
  public static String COMMON_INDEX_NAME;
  public static String INDEX_TYPE_MEDIA;
  public static String SOCIALITY_INDEX_NAME;
  public static String SOCIALITY_INDEX_TYPE_WEIBO;
  public static String SOCIALITY_INDEX_TYPE_WEIBO_COMMENT;
  public static String PRODUCT_INDEX_NAME;
  public static String PRODUCT_INDEX_TYPE_COMMENT;
  public static String PRODUCT_INDEX_TYPE_PRODUCT;
  public static String PRODUCT_INDEX_TYPE_COMPANY;
  public static float QUERY_RESULT_MIN_SCORE = 1.0F;

  public static int SEARCH_TYPE = 0;
  public static int TITLE_SRC_WEIGHT = 100;
  public static int TITLE_WEIGHT = 10;
  public static int TEXT_WEIGHT = 2;
  public static int MEDIA_WEIGHT = 10;
  public static long BASE_TIME = 1413000000000L;
  public static String TIME_WEIGHT = "0.00000000001";
  public static String TIME_TODAY_WEIGHT = "10";
  public static String TIME_YEAR_WEIGHT = "5";
  public static double TIME_DECAY = 0.5D;
  public static double MEDIA_DECAY = 0.5D;
  public static int TIME_SCALE = 2;
  public static int MEDIA_SCALE = 1;
  public static int TIME_SCALE_WEIGHT = 1;
  public static int MEDIA_SCALE_WEIGHT = 1;
  public static String SCORE_MODE = "multiply";

  private static Properties prop = FileUtil.getPropertyFile("es_config.properties");

  static { INDEX_SERVER_ADDRESS = prop.getProperty("INDEX_SERVER_ADDRESS").trim();
    CLUSTER_NAME = prop.getProperty("CLUSTER_NAME", "es_new").trim();

    TOTAL_INDEX_SERVER_ADDRESS = prop.getProperty("TOTAL_INDEX_SERVER_ADDRESS").trim();
    TOTAL_CLUSTER_NAME = prop.getProperty("TOTAL_CLUSTER_NAME", "es_total").trim();

    String score = prop.getProperty("MIN_SCORE", "1");
    QUERY_RESULT_MIN_SCORE = Float.parseFloat(score);

    INDEX_NAME = prop.getProperty("INDEX_NAME", "news").trim();
    INDEX_TYPE_ARTICLE = prop.getProperty("INDEX_TYPE_ARTICLE", "article2").trim();

    COMMON_INDEX_NAME = prop.getProperty("COMMON_INDEX_NAME", "common").trim();
    INDEX_TYPE_MEDIA = prop.getProperty("INDEX_TYPE_MEDIA", "media").trim();

    SOCIALITY_INDEX_NAME = prop.getProperty("SOCIALITY_INDEX_NAME", "sociality").trim();
    SOCIALITY_INDEX_TYPE_WEIBO = prop.getProperty("SOCIALITY_INDEX_TYPE_WEIBO", "weibo").trim();
    SOCIALITY_INDEX_TYPE_WEIBO_COMMENT = prop.getProperty("SOCIALITY_INDEX_TYPE_WEIBO_COMMENT", "weibocomment").trim();

    PRODUCT_INDEX_NAME = prop.getProperty("PRODUCT_INDEX_NAME", "product").trim();
    PRODUCT_INDEX_TYPE_COMMENT = prop.getProperty("PRODUCT_INDEX_TYPE_COMMENT", "comment2").trim();
    PRODUCT_INDEX_TYPE_PRODUCT = prop.getProperty("PRODUCT_INDEX_TYPE_PRODUCT", "product2").trim();
    PRODUCT_INDEX_TYPE_COMPANY = prop.getProperty("PRODUCT_INDEX_TYPE_COMPANY", "company").trim();

    SEARCH_TYPE = Integer.parseInt(prop.getProperty("SEARCH_TYPE", "0"));
    TITLE_SRC_WEIGHT = Integer.parseInt(prop.getProperty("TITLE_SRC_WEIGHT", "100"));
    TITLE_WEIGHT = Integer.parseInt(prop.getProperty("TITLE_WEIGHT", "10"));
    TEXT_WEIGHT = Integer.parseInt(prop.getProperty("TEXT_WEIGHT", "2"));

    MEDIA_WEIGHT = Integer.parseInt(prop.getProperty("MEDIA_WEIGHT", "10"));
    TIME_WEIGHT = prop.getProperty("TIME_WEIGHT", "0.00000000001");
    TIME_TODAY_WEIGHT = prop.getProperty("TIME_TODAY_WEIGHT", "10");
    TIME_YEAR_WEIGHT = prop.getProperty("TIME_YEAR_WEIGHT", "5");

    TIME_DECAY = Double.parseDouble(prop.getProperty("TIME_DECAY", "0.5F"));
    MEDIA_DECAY = Double.parseDouble(prop.getProperty("MEDIA_DECAY", "0.5F"));
    TIME_SCALE = Integer.parseInt(prop.getProperty("TIME_SCALE", "2"));
    MEDIA_SCALE = Integer.parseInt(prop.getProperty("MEDIA_SCALE", "1"));
    TIME_SCALE_WEIGHT = Integer.parseInt(prop.getProperty("TIME_SCALE_WEIGHT", "2"));
    MEDIA_SCALE_WEIGHT = Integer.parseInt(prop.getProperty("MEDIA_SCALE_WEIGHT_", "2"));

    SCORE_MODE = prop.getProperty("SCORE_MODE", "multiply").trim();
  }
}