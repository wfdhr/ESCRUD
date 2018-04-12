package com.tmxmall.publicsafety.utils;
import java.util.List;

import com.shcm.bean.BalanceResultBean;
import com.shcm.bean.SendResultBean;
import com.shcm.send.DataApi;
import com.shcm.send.OpenApi;

/**
 * @author Chinafish
 *
 */
public class SendMessage
{
	private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
	private static String sDataUrl = "http://smsapi.c123.cn/DataPlatform/DataApi";
	
	// 接口帐号
	private static final String account = "1001@501160050001";
	
	// 接口密钥
	private static final String authkey = "66C89F1A862E83DFD38AC082B11055A9";
	
	// 通道组编号
	private static final int cgid = 4137;
	
	// 默认使用的签名编号(未指定签名编号时传此值到服务器)
	private static final int csid = 0;
	
	public static List<SendResultBean> sendOnce(String mobile, String content) throws Exception
	{
		// 发送短信
		return OpenApi.sendOnce(mobile, content, 0, 0, null);
	}
	
	public static void sendMesage(String mobile, String content) throws Exception {
		// 发送参数
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		
		// 状态及回复参数
		DataApi.initialzeAccount(sDataUrl, account, authkey);
		
		// 取帐户余额
		BalanceResultBean br = OpenApi.getBalance();
		if(br == null)
		{
			System.out.println("获取可用余额时发生异常!");
			return;
		}
		
		if(br.getResult() < 1)
		{
			System.out.println("获取可用余额失败: " + br.getErrMsg());
			return;
		}
		System.out.println("可用条数: " + br.getRemain());
		
		List<SendResultBean> listItem = sendOnce(mobile, content);
		if(listItem != null)
		{
			for(SendResultBean t:listItem)
			{
				if(t.getResult() < 1)
				{
					System.out.println("发送提交失败: " + t.getErrMsg());
					return;
				}
				
				System.out.println("发送成功: 消息编号<" + t.getMsgId() + "> 总数<" + t.getTotal() + "> 余额<" + t.getRemain() + ">");
			}
		}
	}
	
	
	
}
