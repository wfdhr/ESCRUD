package com.tmxmall.publicsafety.elasticsearch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tmxmall.publicsafety.domain.PublicSafetySearchDao;

@Component
public class ESCRUD {

	@Autowired
	private PublicSafetySearchDao publicSafetySearchDao;
	
	public  boolean importBulk(String indexName, String type, String routing, List<String> jsons) throws Exception {
		return publicSafetySearchDao.importBulk(indexName, type, routing, jsons);
	}

	public  boolean importBulk(String indexName, String type, String routing, String json) throws Exception {
		return publicSafetySearchDao.importBulkSingle(indexName, type, routing, json);
	}
}
