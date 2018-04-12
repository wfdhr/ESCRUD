package com.tmxmall.publicsafety.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
	
	/*--------------------------------
	程序版权：上海创明信息技有限公司
	服务热线：4008885262
	技术  QQ：2355373292
	修改时间：2013-08-18
	程序功能：创明网PHP接口示例 通过接口进行单发、群发；
	接口说明: http://smsapi.c123.cn/OpenPlatform/OpenApi?action=sendOnce&ac=用户账号&authkey=认证密钥&cgid=通道组编号&c=短信内容&m=发送号码
	状态:
		1 操作成功
		0 帐户格式不正确(正确的格式为:员工编号@企业编号)
		-1 服务器拒绝(速度过快、限时或绑定IP不对等)如遇速度过快可延时再发
		-2 密钥不正确
		-3 密钥已锁定
		-4 参数不正确(内容和号码不能为空，手机号码数过多，发送时间错误等)
		-5 无此帐户
		-6 帐户已锁定或已过期
		-7 帐户未开启接口发送
		-8 不可使用该通道组
		-9 帐户余额不足
		-10 内部错误
		-11 扣费失败

	--------------------------------*/
	private static String url="http://smsapi.c123.cn/OpenPlatform/OpenApi";           //接口地址
	private static String ac="1001@501160050001";		                             //用户账号
	private static String authkey = "66C89F1A862E83DFD38AC082B11055A9";		         //认证密钥
	private static String cgid="4137";                                                  //通道组编号
	private static String c =null;		 //内容
	private static String m = null;	                                         //号码
	private static String csid="4263";  //签名编号 ,可以为空时，使用系统默认的编号
	private static String t="";                                                       //发送时间,可以为空表示立即发送,yyyyMMddHHmmss 如:20130721182038
	  
	/**
	 * 获取当前时间
	 */
	public static String getNowTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		long currentTime = System.currentTimeMillis();// 获取当前时间磋
		String temp = df.format(new Date(currentTime));// 获取当前时间
		return temp;
	}
	/**
	 * 
	 * @param pageUrl  原文URL
	 * @param encoding 传入编码
	 * @return 返回url连接html源码
	 */
	public static String getPageSource(String pageUrl,String encoding) {    
        StringBuffer sb = new StringBuffer();    
        try {    
            //构建一URL对象    
            URL url = new URL(pageUrl);    
            //使用openStream得到一输入流并由此构造一个BufferedReader对象    
            HttpURLConnection connection = (HttpURLConnection) url.
                    openConnection();
            connection.setConnectTimeout(10 * 1000); //设置连接超时
            connection.setReadTimeout(10 * 1000);
             connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
             
             BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));    
            //connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            String line;    
            //读取www资源    
            while ((line = in.readLine()) != null) {    
                sb.append(line);    
                sb.append("\n");  
            }    
            in.close();    
        } catch (Exception ex) {     
        }    
        if(sb.length() == 0 )
        	sb.append("<error>java.lang.NullPointerException</error>");   
        return sb.toString();    
    }   
	public static String  PostPortDate(String portUrl,String XMLdata) throws IOException   {
		StringBuffer sb = new StringBuffer("");
		BufferedReader		reader		= null;
		OutputStreamWriter	out			= null;
		HttpURLConnection	connection	= null;
		try {
			URL url = new URL(portUrl);
			 connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);

//			connection.setRequestProperty("Content-Type",
//					"application/x-www-form-urlencoded");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("ContentType","text/xml;charset=utf-8");
			connection.connect();
			out = new OutputStreamWriter(
					connection.getOutputStream(),"utf-8");

			//System.out.println(obj);
			out.write(XMLdata); // post 接口
//			out.write(XMLdata);
			
			// 读取返回结果
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;
			
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			//System.out.println(sb);

		} catch (MalformedURLException e) {
			System.out.println(e);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
			
		}
		finally{
			if(reader != null)
				
			reader.close();
				
			if(connection != null)
			connection.disconnect();
			if(out != null)
			{
				out.flush();
				out.close();
			}
		}
		return sb.toString();
	}
	/**
	 * 短信接口调用
	 * @param text 短信内容
	 * @param Number 短信手机
	 * @return
	 */
	public static String NoteToNumber(String text ,String Number){
		String pageUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi?action=sendOnce&ac="+ac+
				"&authkey="+authkey+"&cgid="+cgid+
				"&c="+text+"&m="+Number;
		String result  = getPageSource(pageUrl,"UTF-8");
		
		return result;
	}
}
