package com.tmxmall.publicsafety.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingyinTool
{
  HanyuPinyinOutputFormat format = null;
  
  public static enum Type
  {
    UPPERCASE,  LOWERCASE,  FIRSTUPPER,  ONLYFIRST;
  }
  
  public PingyinTool()
  {
    this.format = new HanyuPinyinOutputFormat();
    this.format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
    this.format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
  }
  
  public String toPinYin(String str)
    throws BadHanyuPinyinOutputFormatCombination
  {
    return toPinYin(str, "", Type.UPPERCASE);
  }
  
  public String toPinYin(String str, String spera, Type type)
    throws BadHanyuPinyinOutputFormatCombination
  {
    if ((str == null) || (str.trim().length() == 0)) {
      return "";
    }
    if (type == Type.UPPERCASE) {
      this.format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
    } else {
      this.format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    }
    String py = "";
    String temp = "";
    for (int i = 0; i < str.length(); i++)
    {
      char c = str.charAt(i);
      if (c <= '')
      {
        py = py + c;
      }
      else
      {
        String[] t = PinyinHelper.toHanyuPinyinStringArray(c, this.format);
        if (t == null)
        {
          py = py + c;
        }
        else
        {
          temp = t[0];
          if (type == Type.FIRSTUPPER) {
            temp = t[0].toUpperCase().charAt(0) + temp.substring(1);
          } else if (type == Type.ONLYFIRST) {
            temp = String.valueOf(t[0].charAt(0));
          }
          py = py + temp + (i == str.length() - 1 ? "" : spera);
        }
      }
    }
    return py.trim();
  }
  
//  public static void main(String[] args)
//  {
//    try
//    {
//      PingyinTool tool = new PingyinTool();
//      
//
//
//
//
//
//
//      System.out.println(tool.toPinYin("江 賊民", "", Type.FIRSTUPPER));
//      System.out.println(tool.toPinYin("⒍4学潮", "", Type.FIRSTUPPER));
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
}
