package com.tmxmall.publicsafety.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {


	//中青华云接口
	public static String send(String url, Map<String,String> map,String encoding) throws IOException{  
		String body = "";  

		//创建httpclient对象  
		CloseableHttpClient client = HttpClients.createDefault();  
		//创建post方式请求对象  
		HttpPost httpPost = new HttpPost(url);  

		//装填参数  
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
		if(map!=null){  
			for (Entry<String, String> entry : map.entrySet()) {  
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
			}  
		}  
		//设置参数到请求对象中  
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));  

		System.out.println("请求地址："+url);  
		System.out.println("请求参数："+nvps.toString());  

		//设置header信息  
		//指定报文头【Content-type】、【User-Agent】  
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  

		//执行请求操作，并拿到结果（同步阻塞）  
		CloseableHttpResponse response = client.execute(httpPost);  
		//获取结果实体  
		HttpEntity entity = response.getEntity();  
		if (entity != null) {  
			//按指定编码转换结果实体为String类型  
			body = EntityUtils.toString(entity, encoding);  
		}  
		EntityUtils.consume(entity);  
		//释放链接  
		response.close();  
		return body;  
	}  
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param isproxy
	 *               是否使用代理模式
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) realUrl.openConnection();
			// 打开和URL之间的连接

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");    // POST方法


			// 设置通用的请求属性

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.connect();

			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}    

}
