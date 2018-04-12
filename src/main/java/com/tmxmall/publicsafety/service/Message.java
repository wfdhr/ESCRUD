package com.tmxmall.publicsafety.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:shortMessage.properties"})
public class Message {

	@Value("${sOpenUrl}")
	private String sOpenUrl;
	
	@Value("${sDataUrl}")
	private String sDataUrl;
	
	@Value("${account}")
	private String account;
	
	@Value("${authkey}")
	private String authkey;
	
	@Value("${cgid}")
	private String cgid;
	
	@Value("${csid}")
	private String csid;
	
	
	public String getsOpenUrl() {
		return sOpenUrl;
	}
	public void setsOpenUrl(String sOpenUrl) {
		this.sOpenUrl = sOpenUrl;
	}
	public String getsDataUrl() {
		return sDataUrl;
	}
	public void setsDataUrl(String sDataUrl) {
		this.sDataUrl = sDataUrl;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAuthkey() {
		return authkey;
	}
	public void setAuthkey(String authkey) {
		this.authkey = authkey;
	}
	public String getCgid() {
		return cgid;
	}
	public void setCgid(String cgid) {
		this.cgid = cgid;
	}
	public String getCsid() {
		return csid;
	}
	public void setCsid(String csid) {
		this.csid = csid;
	}
	
	
}
