package com.tmxmall.publicsafety.service;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.tmxmall.publicsafety.utils.Constant;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//外部类可以访问内部类的所有方法和属性，包括私有方法与属性
public class ClientHelper {

	private Settings setting;

	private Map<String, TransportClient> clientMap = new ConcurrentHashMap<String, TransportClient>();

	private Map<String, Integer> ips = new HashMap<String, Integer>(); // hostname port

	private String clusterName = Constant.CLUSER_NAME;

	private ClientHelper() {
		try {
			init();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//TO-DO 添加你需要的client到helper
	}

	public static final ClientHelper getInstance() {
		return ClientHolder.INSTANCE;
	}

	private static class ClientHolder {
		private static final ClientHelper INSTANCE = new ClientHelper();
	}

	/**
	 * 初始化默认的client
	 * @throws UnknownHostException 
	 */
	public void init() throws UnknownHostException {
		ips.put(Constant.ES_IPS, 9300);
		setting = Settings
				.builder()
				.put("client.transport.sniff",true)
				.put("cluster.name", clusterName).build();
		addClient(setting, getAllAddress(ips));
	}

	/**
	 * 获得所有的地址端口
	 *
	 * @return
	 * @throws UnknownHostException 
	 */
	public List<InetSocketTransportAddress> getAllAddress(Map<String, Integer> ips) throws UnknownHostException {
		List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
		for (String ip : ips.keySet()) {
			addressList.add(new InetSocketTransportAddress(InetAddress.getByName(ip.trim()), 9300));
		}
		return addressList;
	}

	public TransportClient getClient() {
		return getClient(clusterName);
	}

	public TransportClient getClient(String clusterName) {
		return clientMap.get(clusterName);
	}

	public void addClient(Settings setting, List<InetSocketTransportAddress> transportAddress) {
		TransportClient	client = new PreBuiltTransportClient(setting);
		for (InetSocketTransportAddress inetSocketTransportAddress : transportAddress) {
			client.addTransportAddress(inetSocketTransportAddress);
		}
		clientMap.put(setting.get("cluster.name"), client);
	}
}
