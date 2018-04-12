package com.tmxmall.publicsafety.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @COMPANY:tmxmall.com,Inc
 * @CLASS:IndexScheduledTasks
 * @DESCRIPTION:公共安全搜索集群常量
 * @AUTHOR:hanyazhou@tmxmall.com
 * @VERSION:v1.0
 * @DATE:2017年3月29日 下午3:35
 */
@Component
public class ESProperties {

    @Value("${cluster.name}")
    private String clusterName;
    @Value("${public_safety_search_ips}")
    private String ips;
    @Value("${public_safety_search_ports}")
    private String ports;
    @Value("${client.transport.sniff}")
    private boolean isSniff;
    @Value("${number.master.shards}")
    private int masterShards;
    @Value("${number.replicas.shards}")
    private int replicasShards;
    @Value("${size}")
    private int size;
    @Value("${from}")
    private int from;

    
    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.username}")
    private String username;
    
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public boolean isSniff() {
        return isSniff;
    }

    public void setSniff(boolean sniff) {
        isSniff = sniff;
    }

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public int getMasterShards() {
        return masterShards;
    }

    public void setMasterShards(int masterShards) {
        this.masterShards = masterShards;
    }

    public int getReplicasShards() {
        return replicasShards;
    }

    public void setReplicasShards(int replicasShards) {
        this.replicasShards = replicasShards;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
