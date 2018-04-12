package com.tmxmall.publicsafety.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:config.properties"})
public class ConfigPropertiesForEsData {
	@Value("${index_name}")
	private String indexName;
	
	@Value("${news_routing}")
	private String newsRouting;
	
	@Value("${news_type}")
	private String newsType;
	
	@Value("${esCluserName}")
	private String esCluserName;
	
	@Value("${social_routing}")
	private String socialRouting;
	
	@Value("${social_type}")
	private String socialType;
	
	@Value("${public_opinion_psot_url}")
	private String psotUrl;
	
	@Value("${public_opinion_get_person}")
	private String getPerson;
	
	@Value("${public_opinion_get_location}")
	private String getLocation;
	
	@Value("${public_opinion_get_organization}")
	private String getOrganization;
	
	@Value("${public_opinion_get_remove}")
	private String remove;
	
	@Value("${public_opinion_get_mixed}")
	private String mixed;
	
	@Value("${es_ips}")
	private String esIps;
	
	@Value("${public_home_wanring}")
	private String homeWarning; 	//首页预警
	
	@Value("${public_agg_characterLeader}") 
	private String characterLeader;		//人物分析-意见领袖
	
	@Value("${public_agg_intenetLeader}")
	private String intenetLeader;  //人物分析-网络推手
	
	@Value("${stop_province}")
	private String stopProvince; //停用省份
	
	@Value("${stop_country}")
	private String stopCountry;	//停用国家
	
	@Value("${news_params}")
	private String newsParams; //新闻参数
	
	@Value("${social_params}")
	private String socialParams; //社交参数
	
	@Value("${sentimentOrient}")
	private String sentimentOrient;
	
	@Value("${sentimentId}")
	private String sentimentId;
	
	@Value("${letters}")
	private String letters;
	
	@Value("${news_weight}")
	private String newsWeight;
	
	@Value("${zhongqing}")
	private String zhongqing;
	
	
	
	
	public String getZhongqing() {
		return zhongqing;
	}
	public void setZhongqing(String zhongqing) {
		this.zhongqing = zhongqing;
	}
	public String getNewsWeight() {
		return newsWeight;
	}
	public void setNewsWeight(String newsWeight) {
		this.newsWeight = newsWeight;
	}
	public String getLetters() {
		return letters;
	}
	public void setLetters(String letters) {
		this.letters = letters;
	}
	public String getSentimentOrient() {
		return sentimentOrient;
	}
	public void setSentimentOrient(String sentimentOrient) {
		this.sentimentOrient = sentimentOrient;
	}
	public String getSentimentId() {
		return sentimentId;
	}
	public void setSentimentId(String sentimentId) {
		this.sentimentId = sentimentId;
	}
	public String getSocialParams() {
		return socialParams;
	}
	public void setSocialParams(String socialParams) {
		this.socialParams = socialParams;
	}
	public String getNewsParams() {
		return newsParams;
	}
	public void setNewsParams(String newsParams) {
		this.newsParams = newsParams;
	}
	public String getStopCountry() {
		return stopCountry;
	}
	public void setStopCountry(String stopCountry) {
		this.stopCountry = stopCountry;
	}
	public String getEsCluserName() {
		return esCluserName;
	}
	public void setEsCluserName(String esCluserName) {
		this.esCluserName = esCluserName;
	}
	public String getStopProvince() {
		return stopProvince;
	}
	public void setStopProvince(String stopProvince) {
		this.stopProvince = stopProvince;
	}
	public String getIntenetLeader() {
		return intenetLeader;
	}
	public void setIntenetLeader(String intenetLeader) {
		this.intenetLeader = intenetLeader;
	}
	public String getCharacterLeader() {
		return characterLeader;
	}
	public void setCharacterLeader(String characterLeader) {
		this.characterLeader = characterLeader;
	}
	public String getHomeWarning() {
		return homeWarning;
	}
	public void setHomeWarning(String homeWarning) {
		this.homeWarning = homeWarning;
	}
	
	public String getEsIps() {
		return esIps;
	}
	public void setEsIps(String esIps) {
		this.esIps = esIps;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getNewsRouting() {
		return newsRouting;
	}
	public void setNewsRouting(String newsRouting) {
		this.newsRouting = newsRouting;
	}
	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
	public String getSocialRouting() {
		return socialRouting;
	}
	public void setSocialRouting(String socialRouting) {
		this.socialRouting = socialRouting;
	}
	public String getSocialType() {
		return socialType;
	}
	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}
	public String getPsotUrl() {
		return psotUrl;
	}
	public void setPsotUrl(String psotUrl) {
		this.psotUrl = psotUrl;
	}
	public String getGetPerson() {
		return getPerson;
	}
	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
	}
	public String getGetLocation() {
		return getLocation;
	}
	public void setGetLocation(String getLocation) {
		this.getLocation = getLocation;
	}
	public String getGetOrganization() {
		return getOrganization;
	}
	public void setGetOrganization(String getOrganization) {
		this.getOrganization = getOrganization;
	}
	public String getRemove() {
		return remove;
	}
	public void setRemove(String remove) {
		this.remove = remove;
	}
	public String getMixed() {
		return mixed;
	}
	public void setMixed(String mixed) {
		this.mixed = mixed;
	}
	


}
