package com.tmxmall.publicsafety.domain.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery.ScoreMode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmxmall.publicsafety.constant.Configuration;
import com.tmxmall.publicsafety.domain.PublicSafetySearchDao;
import com.tmxmall.publicsafety.service.ConfigPropertiesForEsData;
import com.tmxmall.publicsafety.utils.Constant;
import com.tmxmall.publicsafety.utils.PropertiesUtilsForEsData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 接口方法的实现
 * 
 * @COMPANY:tmxmall.com,Inc
 * @CLASS:PublicSafetySearchDaoImpl
 * @DESCRIPTION:User测试类
 * @AUTHOR:hanyazhou@tmxmall.com
 * @VERSION:v1.0
 * @DATE:2017年3月29日 下午54:13
 */
@Repository("publicSafetySearchDao")
public class PublicSafetySearchDaoImpl implements PublicSafetySearchDao {

	private static final Logger logger = LoggerFactory.getLogger(PublicSafetySearchDaoImpl.class);

	@Autowired
	private ConfigPropertiesForEsData configPropertiesForEsData;
	@Autowired
	private PropertiesUtilsForEsData propertiesUtilsForEsData; 


	public static TransportClient client;


	public TransportClient getTransportClient() throws Exception {
		if (client == null) {
			Settings settings = Settings.builder() //
					.put("cluster.name", configPropertiesForEsData.getEsCluserName().trim()) //public_safetys,esProperties.getClusterName()
					.put("client.transport.sniff", true) //
					.build();
			client = new PreBuiltTransportClient(settings);
			try {
				String[] esIps = configPropertiesForEsData.getEsIps().trim().split(",");
				for (String esip : esIps) {
					//										clien4t.addTransportAddress(new InetSocketTransportAddress( //esProperties.getIps(),Integer.parseInt(esProperties.getPorts())
					//												InetAddress.getByName(esip), 8000));
					client.addTransportAddress(new InetSocketTransportAddress( //esProperties.getIps(),Integer.parseInt(esProperties.getPorts())
							InetAddress.getByName(esip.trim()), 9300));
				}
				logger.info("+++++++++++=client:" + client);
			} catch (Exception e) {
				logger.error("client error!");
			}
		}
		return client;

	}
	@Override
	public TransportClient getTransportClient1(String ip) throws Exception {
		TransportClient client = null;
		if (client == null) {
			Settings settings = Settings.builder() //
					.put("cluster.name", configPropertiesForEsData.getEsCluserName().trim()) //public_safetys,esProperties.getClusterName()
					.put("client.transport.sniff", true) //
					.build();
			client = new PreBuiltTransportClient(settings);
			try {
				client.addTransportAddress(new InetSocketTransportAddress( //esProperties.getIps(),Integer.parseInt(esProperties.getPorts())
						InetAddress.getByName(ip), 9300));
				logger.info("+++++++++++=client:" + client);
			} catch (Exception e) {
				logger.error("client error!");
			}
		}
		return client;
	}

	//	public CreateIndexResponse createIndex(String indexName) throws Exception {
	//		return getTransportClient().admin().indices().prepareCreate(indexName) //
	//				.setSettings(Settings.builder() //
	//						.put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, esProperties.getMasterShards()) //
	//						.put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, esProperties.getReplicasShards())) //
	//				.execute().actionGet();
	//	}

	public boolean isIndexExists(String indexName) throws Exception {
		return getTransportClient().admin().indices().prepareExists(indexName).execute().actionGet().isExists();
	}

	// 删除整个index
	public DeleteIndexResponse deleteByIndex(String indexName) throws Exception {
		return getTransportClient().admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
		// return getTransportClient().prepareDelete(indexName, type, id);
	}

	public OpenIndexResponse openIndex(String indexName) throws Exception {
		return getTransportClient().admin().indices().prepareOpen(indexName).execute().actionGet();
	}

	// 创建index的type，并添加Mapping
	public PutMappingResponse createAndAddMappings(String indexName, String type, XContentBuilder mappingSource)
			throws Exception {
		return getTransportClient().admin().indices().preparePutMapping(indexName).setType(type)
				.setSource(mappingSource).execute().actionGet();
	}

	public boolean isTypeExists(String indexName, String type) throws Exception {
		return getTransportClient().admin().indices().prepareTypesExists(indexName).setTypes(type).execute().actionGet()
				.isExists();
	}

	public boolean importDataBulk(String path, String indexName, String type, String routing) throws Exception {
		BulkRequestBuilder bulkReq = getTransportClient().prepareBulk();
		File dirFile = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(dirFile));
		String json = null;
		int a = 1;
		while ((json = br.readLine()) != null) {
			IndexRequestBuilder indexRequest = getTransportClient().prepareIndex(indexName, type).setRouting(routing).setSource(json);
			bulkReq.add(indexRequest);
			System.out.println(a);
			System.out.println(json);
			a++;
		}
		BulkResponse bulkResponse = bulkReq.execute().actionGet();
		br.close();
		System.out.println(bulkResponse.buildFailureMessage()); 
		return bulkResponse.hasFailures();
	}

	public UpdateResponse testUpdate(String indexName, String type, String routing, String docID,
			XContentBuilder updateSource) throws Exception {
		return getTransportClient().prepareUpdate(indexName, type, docID).setRouting(routing).setDoc(updateSource)
				.get();
	}



	@Override
	public SearchResponse esNewsSearch(String type, String routing, JSONObject params) throws Exception {
		// TODO Auto-generated catch block
		SearchRequestBuilder request = null;
		String[] fiedls = null;
		Boolean isNotlist = null;
		//获取关键词
		String keyword = "";
		QueryBuilder initQb = null;
		JSONObject message = params.getJSONObject("message");
		//查询字段是否是列表 默认为列表
		if(!Constant.isNullKey(message,"isNotlist")) {
			isNotlist = message.getBoolean("isNotlist");
			if(isNotlist) {
				fiedls = propertiesUtilsForEsData.getEsParamsFields("esFields/esNewsFieldsText.properties");
			}
		} else {
			fiedls = propertiesUtilsForEsData.getEsParamsFields("esFields/esNewsFieldsList.properties");
		}
		if(!Constant.isNullKey(message,"returnFields")) {
			String returnFields = message.getString("returnFields");
			fiedls = returnFields.trim().split(",");
		}
		request = getTransportClient().prepareSearch(configPropertiesForEsData.getIndexName().trim().split(",")).setTypes(type).setRouting(routing).storedFields(fiedls).setSearchType(SearchType.QUERY_THEN_FETCH);
		//如果存在关键字
		if(!Constant.isNullKey(message,"keyword")) {
			keyword = message.getString("keyword");
			initQb = QueryBuilders.queryStringQuery(keyword)
					.defaultField("FIELD")
					.field("titleZh",3.0F)
					.field("textZh", 2.0F)
					.field("abstractZh", 2.0F);
		} 

		//先过滤在查询	
		QueryBuilder fqb = null;
		if(StringUtils.isNotBlank(keyword)) {
			fqb = QueryBuilders.boolQuery().filter(filterQuery(message)).must(initQb);
		} else {
			fqb = QueryBuilders.boolQuery().filter(filterQuery(message));
		}
		request.setQuery(fqb);
		if (!Constant.isNullKey(message, "factor") && StringUtils.isNotBlank(message.getString("factor"))) {
			String factor = message.getString("factor");
			if("pubdate".equals(factor)) {
				FilterFunctionBuilder[] functions = {
						new FunctionScoreQueryBuilder.FilterFunctionBuilder(
								ScoreFunctionBuilders.scriptFunction(new Script("doc['pubTime'].value*" + Configuration.TIME_WEIGHT)))    
				};
				FunctionScoreQueryBuilder fcqb = QueryBuilders.functionScoreQuery(fqb,functions).scoreMode(ScoreMode.MULTIPLY); 
				request.setQuery(fcqb);
			}
			if("opinionValue".equals(factor)) {
				FilterFunctionBuilder[] functions = {
						new FunctionScoreQueryBuilder.FilterFunctionBuilder(
								ScoreFunctionBuilders.scriptFunction(new Script("doc['opinionValue'].value*" + Configuration.OPINIONVALUE_WEIGHT)))    
				};
				FunctionScoreQueryBuilder fcqb = QueryBuilders.functionScoreQuery(fqb,functions).scoreMode(ScoreMode.MULTIPLY); 
				request.setQuery(fcqb);
			}
			if("transfer".equals(factor)) {
				FilterFunctionBuilder[] functions = {
						new FunctionScoreQueryBuilder.FilterFunctionBuilder(
								ScoreFunctionBuilders.scriptFunction(new Script("doc['transfer'].value*" + Configuration.TRANSFER_WEIGHT)))    
				};
				FunctionScoreQueryBuilder fcqb = QueryBuilders.functionScoreQuery(fqb,functions).scoreMode(ScoreMode.MULTIPLY); 
				request.setQuery(fcqb);
			}
		}
		//是否高亮
		if (!Constant.isNullKey(message, "highlight") && StringUtils.isNotBlank(message.getString("highlight"))) {
			boolean highlight = message.getBoolean("highlight");
			if (highlight) {
				HighlightBuilder highlightBuilder = new HighlightBuilder();
				highlightBuilder.preTags("<em>");
				highlightBuilder.postTags("</em>");
				highlightBuilder.field("textZh");
				highlightBuilder.field("titleZh");
				highlightBuilder.field("abstractZh");
				highlightBuilder.numOfFragments(0);
				request.highlighter(highlightBuilder);
			}
		}
		//聚合
		AggregationBuilder aggregationBuilder = null;
		if (!Constant.isNullKey(message, "agg") && StringUtils.isNotBlank(message.getString("agg"))) {
			String agg = message.getString("agg");
			aggregationBuilder = AggregationBuilders.terms(agg + "_count").field(agg);
			if (!Constant.isNullKey(message, "subagg") && StringUtils.isNotBlank(message.getString("subagg"))) {
				String subagg = message.getString("subagg");
				aggregationBuilder.subAggregation(AggregationBuilders.terms(subagg + "_count").field(subagg).size(0));
			}
			if (!Constant.isNullKey(message, "subDateAgg") && StringUtils.isNotBlank(message.getString("subDateAgg"))) {
				String subDateAgg = message.getString("subDateAgg");
				DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram(subDateAgg + "_count").field(subDateAgg);
				//默认是天
				if (!Constant.isNullKey(message, "dateAggType") && StringUtils.isNotBlank(message.getString("dateAggType"))) {
					String dateAggType = message.getString("dateAggType");
					if("hour".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.HOUR);
					} else if("day".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.DAY);
					} else if("week".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.WEEK);
					} else if("month".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.MONTH);
					} else if("year".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.YEAR);
					}
					dateHistogram.format("yyyy-MM-dd HH:mm:ss");  
					dateHistogram.minDocCount(0L);
					this.setExtendedBounds(dateHistogram, message);
				} 
				aggregationBuilder.subAggregation(dateHistogram);
			}
			request.addAggregation(aggregationBuilder);
		}
		//按照时间维度聚合
		if (!Constant.isNullKey(message, "dateAgg") && StringUtils.isNotBlank(message.getString("dateAgg"))) {
			String dateAgg = message.getString("dateAgg");
			DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram(dateAgg + "_count").field(dateAgg);
			//默认是天
			if (!Constant.isNullKey(message, "dateAggType") && StringUtils.isNotBlank(message.getString("dateAggType"))) {
				String dateAggType = message.getString("dateAggType");
				if("hour".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.HOUR);
				} else if("day".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.DAY);
				} else if("week".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.WEEK);
				} else if("month".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.MONTH);
				} else if("year".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.YEAR);
				}
				dateHistogram.format("yyyy-MM-dd HH:mm:ss");  
				dateHistogram.minDocCount(0L);
			} 
			request.addAggregation(dateHistogram);
		}
		//用于数量排重
		if(!Constant.isNullKey(message,"cardinality")) {
			String cardinality = message.getString("cardinality");
			CardinalityAggregationBuilder distinctBuilder = AggregationBuilders.cardinality("distinct_" + cardinality).field(cardinality);
			request.addAggregation(distinctBuilder);
		}
		//是否排序
		//		this.setSort(request, message);
		if(!Constant.isNullKey(message, "pageNo") && !Constant.isNullKey(message, "pageSize")) {
			//分页信息
			request.setFrom((message.getInt("pageNo") - 1) * message.getInt("pageSize")).setSize(message.getInt("pageSize"));
		}
		//字段折叠
		loop:if(!Constant.isNullKey(message, "collapse")) {
			String collapse = message.getString("collapse");
			if("none".equals(collapse)) {
				break loop;
			}
			CollapseBuilder collapseBuilder = new CollapseBuilder(collapse);
			request.setCollapse(collapseBuilder);
		} 

		//标题去重复
		return request.execute().actionGet();
	}

	private void setExtendedBounds(DateHistogramAggregationBuilder dateHistogram,JSONObject message) {
		if(!Constant.isNullKey(message, "startTime") && !Constant.isNullKey(message, "endTime")) {
			String startTime = message.getString("startTime");
			String endTime = message.getString("endTime");
			ExtendedBounds extendedBounds = new ExtendedBounds(startTime,endTime);
			dateHistogram.extendedBounds(extendedBounds);
		}
	}
	@Override
	public SearchResponse esSocialSearch(String type, String routing, JSONObject params) throws Exception {
		// TODO Auto-generated catch block
		SearchRequestBuilder request = null;
		String[] fiedls = null;
		Boolean isNotlist = null;
		//获取关键词
		String keyword = "";
		QueryBuilder initQb = null;
		JSONObject message = params.getJSONObject("message");
		//查询字段是否是列表 默认为列表
		if(!Constant.isNullKey(message,"isNotlist")) {
			isNotlist = message.getBoolean("isNotlist");
			if(isNotlist) {
				fiedls = propertiesUtilsForEsData.getEsParamsFields("esFields/esSocialFieldsText.properties");
			}
		} else {
			fiedls = propertiesUtilsForEsData.getEsParamsFields("esFields/esSocialFieldsList.properties");
		}

		if(!Constant.isNullKey(message,"returnFields")) {
			String returnFields = message.getString("returnFields");
			fiedls = returnFields.trim().split(",");
		}
		request = getTransportClient().prepareSearch(configPropertiesForEsData.getIndexName().trim().split(",")).setTypes(type).setRouting(routing).storedFields(fiedls).setSearchType(SearchType.QUERY_THEN_FETCH);

		//如果存在关键字
		if(!Constant.isNullKey(message,"keyword")) {
			if(StringUtils.isNotBlank(message.getString("keyword"))) {
				keyword = message.getString("keyword");
				initQb = QueryBuilders.queryStringQuery(keyword)
						.field("text", 2.0F)
						.field("title",3.0F);
			} 
		} 
		//先过滤在查询
		QueryBuilder fqb = null;
		if(StringUtils.isNotBlank(keyword)) {
			fqb = QueryBuilders.boolQuery().filter(filterQuery(message)).must(initQb);
		} else {
			fqb = QueryBuilders.boolQuery().filter(filterQuery(message));
		}
		request.setQuery(fqb);
		//自定义得分
		if (!Constant.isNullKey(message, "factor") && StringUtils.isNotBlank(message.getString("factor"))) {
			String factor = message.getString("factor");
			if("timeStr".equals(factor)) {
				FilterFunctionBuilder[] functions = {
						new FunctionScoreQueryBuilder.FilterFunctionBuilder(
								ScoreFunctionBuilders.scriptFunction(new Script("doc['timeStr'].value*" + Configuration.TIME_WEIGHT)))    
				};
				FunctionScoreQueryBuilder fcqb = QueryBuilders.functionScoreQuery(fqb,functions).scoreMode(ScoreMode.MULTIPLY); 
				request.setQuery(fcqb);
			}
			if("opinionValue".equals(factor)) {
				FilterFunctionBuilder[] functions = {
						new FunctionScoreQueryBuilder.FilterFunctionBuilder(
								ScoreFunctionBuilders.scriptFunction(new Script("doc['opinionValue'].value*" + Configuration.OPINIONVALUE_WEIGHT)))    
				};
				FunctionScoreQueryBuilder fcqb = QueryBuilders.functionScoreQuery(fqb,functions).scoreMode(ScoreMode.MULTIPLY); 
				request.setQuery(fcqb);
			}
			if("rpsCnt".equals(factor)) {
				FilterFunctionBuilder[] functions = {
						new FunctionScoreQueryBuilder.FilterFunctionBuilder(
								ScoreFunctionBuilders.scriptFunction(new Script("doc['rpsCnt'].value")))    
				};
				FunctionScoreQueryBuilder fcqb = QueryBuilders.functionScoreQuery(fqb,functions).scoreMode(ScoreMode.MULTIPLY); 
				request.setQuery(fcqb);
			}
			if("view".equals(factor)) {
				FilterFunctionBuilder[] functions = {
						new FunctionScoreQueryBuilder.FilterFunctionBuilder(
								ScoreFunctionBuilders.scriptFunction(new Script("doc['view'].value")))    
				};
				FunctionScoreQueryBuilder fcqb = QueryBuilders.functionScoreQuery(fqb,functions).scoreMode(ScoreMode.MULTIPLY); 
				request.setQuery(fcqb);
			}
		}
		//是否高亮
		if (!Constant.isNullKey(message, "highlight") && StringUtils.isNotBlank(message.getString("highlight"))) {
			boolean highlight = message.getBoolean("highlight");
			if (highlight) {
				HighlightBuilder highlightBuilder = new HighlightBuilder();
				highlightBuilder.preTags("<em>");
				highlightBuilder.postTags("</em>");
				highlightBuilder.field("text");
				highlightBuilder.field("title");
				highlightBuilder.field("abstractZh");
				highlightBuilder.numOfFragments(0);
				request.highlighter(highlightBuilder);
			}
		}
		//聚合
		AggregationBuilder aggregationBuilder = null;
		if (!Constant.isNullKey(message, "agg") && StringUtils.isNotBlank(message.getString("agg"))) {
			String agg = message.getString("agg");
			aggregationBuilder = AggregationBuilders.terms(agg + "_count").field(agg);
			if (!Constant.isNullKey(message, "subagg") && StringUtils.isNotBlank(message.getString("subagg"))) {
				String subagg = message.getString("subagg");
				aggregationBuilder.subAggregation(AggregationBuilders.terms(subagg + "_count").field(subagg).size(0));
			}
			if (!Constant.isNullKey(message, "subDateAgg") && StringUtils.isNotBlank(message.getString("subDateAgg"))) {
				String subDateAgg = message.getString("subDateAgg");
				DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram(subDateAgg + "_count").field(subDateAgg);
				//默认是天
				if (!Constant.isNullKey(message, "dateAggType") && StringUtils.isNotBlank(message.getString("dateAggType"))) {
					String dateAggType = message.getString("dateAggType");
					if("hour".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.HOUR);
					} else if("day".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.DAY);
					} else if("week".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.WEEK);
					} else if("month".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.MONTH);
					} else if("year".equals(dateAggType)) {
						dateHistogram.dateHistogramInterval(DateHistogramInterval.YEAR);
					}
					dateHistogram.format("yyyy-MM-dd HH:mm:ss");  
					dateHistogram.minDocCount(0L);
				} 
				aggregationBuilder.subAggregation(dateHistogram);
			}
			request.addAggregation(aggregationBuilder);
		}
		//按照时间维度聚合
		if (!Constant.isNullKey(message, "dateAgg") && StringUtils.isNotBlank(message.getString("dateAgg"))) {
			String dateAgg = message.getString("dateAgg");
			DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram(dateAgg + "_count").field(dateAgg);
			//默认是天
			if (!Constant.isNullKey(message, "dateAggType") && StringUtils.isNotBlank(message.getString("dateAggType"))) {
				String dateAggType = message.getString("dateAggType");
				if("hour".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.HOUR);
				} else if("day".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.DAY);
				} else if("week".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.WEEK);
				} else if("month".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.MONTH);
				} else if("year".equals(dateAggType)) {
					dateHistogram.dateHistogramInterval(DateHistogramInterval.YEAR);
				}
				dateHistogram.format("yyyy-MM-dd HH:mm:ss");  
				dateHistogram.minDocCount(0L);
			} 
			request.addAggregation(dateHistogram);
		}
		//用于数量排重
		if(!Constant.isNullKey(message,"cardinality")) {
			String cardinality = message.getString("cardinality");
			CardinalityAggregationBuilder distinctBuilder = AggregationBuilders.cardinality("distinct_" + cardinality).field(cardinality);
			request.addAggregation(distinctBuilder);
		}
		//是否排序
		//		this.setSort(request, message);
		if(!Constant.isNullKey(message, "pageNo") && !Constant.isNullKey(message, "pageSize")) {
			//分页信息
			request.setFrom((message.getInt("pageNo") - 1) * message.getInt("pageSize")).setSize(message.getInt("pageSize"));
		}
		//字段折叠
		loop:if(!Constant.isNullKey(message, "collapse")) {
			String collapse = message.getString("collapse");
			if("none".equals(collapse)) {
				break loop;
			}
			CollapseBuilder collapseBuilder = new CollapseBuilder(collapse);
			//标题去重复
			request.setCollapse(collapseBuilder);
		} 
		return request.execute().actionGet();
	}
	/**
	 * 排序
	 * @param obj
	 * @return
	 */
	private SortBuilder getSort(JSONObject obj)
	{
		if (!obj.containsKey("order")) {
			return SortBuilders.scoreSort();
		}
		String orderStr = obj.getString("order");
		if ((orderStr == null) || ("".equals(orderStr))) {
			return SortBuilders.scoreSort();
		}
		SortOrder order = "asc".equals(orderStr) ? SortOrder.ASC : SortOrder.DESC;

		return SortBuilders.fieldSort(obj.getString("fieldName")).order(order);
	}

	private void setSort(SearchRequestBuilder srb, JSONObject obj)
	{
		if ((!obj.containsKey("fieldName")) || (!obj.containsKey("order")))
		{
			srb.addSort(SortBuilders.scoreSort());
			return;
		}
		SortOrder order = "asc".equals(obj.getString("order")) ? SortOrder.ASC : SortOrder.DESC;
		if (!obj.getString("fieldName").equals("_score"))
		{
			srb.addSort(SortBuilders.fieldSort(obj.getString("fieldName")).order(order));
			srb.addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC));
		}
		else
		{
			srb.addSort(SortBuilders.fieldSort(obj.getString("fieldName")).order(order));
		}
	}
	// TODO Auto-generated catch block
	private QueryBuilder filterQuery(JSONObject jsonQuery)
	{
		List<QueryBuilder> filters = new ArrayList();
		//时间过滤
		QueryBuilder fbDate = null;
		if(!Constant.isNullKey(jsonQuery, "startTime") && !Constant.isNullKey(jsonQuery, "endTime")) { //时间
			String startTime = jsonQuery.getString("startTime");
			String endTime = jsonQuery.getString("endTime");
			if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
				String searchType = jsonQuery.getString("searchType");
				if("news".equals(searchType)) {
					fbDate = 
							QueryBuilders.rangeQuery("pubdate").gte(startTime).lte(endTime).format("yyyy-MM-dd HH:mm:ss");
					filters.add(fbDate);
				}
				if("social".equals(searchType)) {
					fbDate = 
							QueryBuilders.rangeQuery("timeStr").gte(startTime).lte(endTime).format("yyyy-MM-dd HH:mm:ss");
					filters.add(fbDate);
				}
			}
		}
		QueryBuilder fbisOri = null;
		if (!Constant.isNullKey(jsonQuery, "isOri"))  //社交是否原创
		{
			String isOri = jsonQuery.getString("isOri");
			if(StringUtils.isNotBlank(isOri)) {
				fbisOri = QueryBuilders.termQuery("isOri", isOri);
				filters.add(fbisOri);
			}
		}
		QueryBuilder fbisOriginal = null;
		if (!Constant.isNullKey(jsonQuery, "isOriginal"))  //新闻是否原创
		{
			String isOriginal = jsonQuery.getString("isOriginal");
			if(StringUtils.isNotBlank(isOriginal)) {
				fbisOriginal = QueryBuilders.termQuery("isOriginal", isOriginal);
				filters.add(fbisOriginal);
			}
		}
		QueryBuilder fbmediaTname = null;
		if (!Constant.isNullKey(jsonQuery, "mediaTname"))  //媒体类型
		{
			JSONArray arr = jsonQuery.getJSONArray("mediaTname");
			if(arr != null && arr.size() > 0) {
				String[] mediaTnames = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					mediaTnames[i] = arr.getString(i);
				}
				fbmediaTname = QueryBuilders.termsQuery("mediaTname", mediaTnames);
				filters.add(fbmediaTname);
			}
		}
		//判断是否有观点
		QueryBuilder fviewponit = null;
		if (!Constant.isNullKey(jsonQuery, "isviewponit")) 
		{
			Boolean isviewponit = jsonQuery.getBoolean("isviewponit");
			if(isviewponit) {
				fviewponit = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("viewpoint_list", "[]"));
				filters.add(fviewponit);;
			}
		}

		QueryBuilder fbopinionValue = null;
		if (!Constant.isNullKey(jsonQuery, "opinionValue")) //舆情价值
		{
			String opinionValue = jsonQuery.getString("opinionValue");
			if("其它".equals(opinionValue)) {
				fbopinionValue = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("opinionValue").gte(0).lte(19));
				System.out.println("执行舆情画像其他-----------------------------------------");
			} else if("全部".equals(opinionValue)) {
				fbopinionValue = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("opinionValue").gte(0).lte(100));
			} else if("首页预警".equals(opinionValue)) {
				fbopinionValue = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("opinionValue").gte(20).lte(100));
			} else {
				List<Integer> opinionValues = returnOpinionValueList(opinionValue);
				Collections.sort(opinionValues);
				if(opinionValues != null && !opinionValues.isEmpty()) {
					fbopinionValue = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("opinionValue").gte(20).lte(100)).must(QueryBuilders.termsQuery("opinionValue", opinionValues.toArray()));
				}
			}
			filters.add(fbopinionValue);
		}
		QueryBuilder fbCountryNameZh = null; 
		if (!Constant.isNullKey(jsonQuery, "countryNameZh")) //国家否
		{
			JSONArray arr = jsonQuery.getJSONArray("countryNameZh");
			if(arr != null && arr.size() > 0) {
				String[] regionIds = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					regionIds[i] = arr.getString(i);
				}
				fbCountryNameZh = QueryBuilders.termsQuery("countryNameZh", regionIds);
				filters.add(fbCountryNameZh);
			}
		}
		QueryBuilder fbLang = null;
		if (!Constant.isNullKey(jsonQuery, "languageCode"))  //语言编码
		{
			JSONArray arr = jsonQuery.getJSONArray("languageCode");
			if(arr != null && arr.size() > 0) {
				String[] langIds = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					langIds[i] = arr.getString(i);
				}
				fbLang = QueryBuilders.termsQuery("languageCode", langIds);
				filters.add(fbLang);
			}
		}
		QueryBuilder fbfinger = null;
		if (!Constant.isNullKey(jsonQuery, "finger"))  //指纹id
		{
			JSONArray arr = jsonQuery.getJSONArray("finger");
			if(arr != null && arr.size() > 0) {
				String[] fingers = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					fingers[i] = arr.getString(i);
				}
				fbfinger = QueryBuilders.termsQuery("finger", fingers);
				filters.add(fbfinger);
			}
		}
		QueryBuilder fbsourceType = null;
		if (!Constant.isNullKey(jsonQuery, "sourceType"))  //社交来源
		{
			JSONArray arr = jsonQuery.getJSONArray("sourceType");
			if(arr != null && arr.size() > 0) {
				String[] sourceTypes = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					sourceTypes[i] = arr.getString(i);
				}
				fbsourceType = QueryBuilders.termsQuery("sourceType", sourceTypes);
				filters.add(fbsourceType);	
			}
		}
		QueryBuilder fbLangTName = null;
		if (!Constant.isNullKey(jsonQuery, "languageTname")) //语言
		{
			JSONArray arr = jsonQuery.getJSONArray("languageTname");
			if(arr != null && arr.size() > 0) {
				String[] langIds = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					langIds[i] = arr.getString(i);
				}
				fbLangTName = QueryBuilders.termsQuery("languageTname", langIds);
				filters.add(fbLangTName);
			}
		}
		QueryBuilder fbMediaLevel = null;
		if (!Constant.isNullKey(jsonQuery, "mediaLevel"))  //媒体权重
		{
			JSONArray arr = jsonQuery.getJSONArray("mediaLevel");
			if(arr != null && arr.size() > 0) {
				int[] mediaLevels = new int[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					Object value = arr.get(i);
					if(value instanceof String) {
						mediaLevels[i] = Integer.parseInt(arr.getString(i));
					}
					if(value instanceof Integer) {
						mediaLevels[i] = arr.getInt(i);
					}
				}
				fbMediaLevel = QueryBuilders.termsQuery("mediaLevel", mediaLevels);
				filters.add(fbMediaLevel);
			}
		}
		QueryBuilder fbMediaLevel360 = null;
		if (!Constant.isNullKey(jsonQuery, "mediaLevel360"))  //媒体权重360
		{
			JSONArray arr = jsonQuery.getJSONArray("mediaLevel360");
			if(arr != null && arr.size() > 0) {
				int[] mediaLevl360s = new int[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					Object value = arr.get(i);
					if(value instanceof String) {
						mediaLevl360s[i] = Integer.parseInt(arr.getString(i));
					}
					if(value instanceof Integer) {
						mediaLevl360s[i] = arr.getInt(i);
					}
				}
				fbMediaLevel360 = QueryBuilders.termsQuery("mediaLevel360", mediaLevl360s);
				filters.add(fbMediaLevel360);
			}
		}

		QueryBuilder fid = null;
		if (!Constant.isNullKey(jsonQuery, "id"))
		{
			JSONArray arr = jsonQuery.getJSONArray("id");
			if(arr != null && arr.size() > 0) {
				String[] ids = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					ids[i] = arr.getString(i);
				}
				fid = QueryBuilders.termsQuery("id", ids);
				filters.add(fid);
			}

		}

		QueryBuilder fbuuid = null;
		if (!Constant.isNullKey(jsonQuery, "uuid")) 
		{
			JSONArray arr = jsonQuery.getJSONArray("uuid");
			if(arr != null && arr.size() > 0) {
				String[] uuids = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					uuids[i] = arr.getString(i);
				}
				System.out.println("所有的uuids" + Arrays.toString(uuids));
				fbuuid = QueryBuilders.termsQuery("uuid", uuids);
				filters.add(fbuuid);
			}

		}

		QueryBuilder fmyId = null;
		if (!Constant.isNullKey(jsonQuery, "myId"))
		{
			JSONArray arr = jsonQuery.getJSONArray("myId");
			if(arr != null && arr.size() > 0) {
				String[] myIds = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					myIds[i] = arr.getString(i);
				}
				fmyId = QueryBuilders.termsQuery("myId", myIds);
				filters.add(fmyId);
			}

		}
		//根据uuid去重复
		QueryBuilder fbDeduplicationNews = null;
		if (!Constant.isNullKey(jsonQuery, "DeduplicationNews")) 
		{
			String uuid = jsonQuery.getString("DeduplicationNews");
			if(StringUtils.isNotBlank(uuid)) {
				fbDeduplicationNews = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("uuid", uuid.split(",")));
				filters.add(fbDeduplicationNews);
			}
		}
		//根据myId去重复
		QueryBuilder fDeduplicationSocial = null;
		if (!Constant.isNullKey(jsonQuery, "DeduplicationSocial"))
		{
			String myId = jsonQuery.getString("DeduplicationSocial");
			if(StringUtils.isNotBlank(myId)) {
				fDeduplicationSocial = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("myId", myId.split(",")));
				filters.add(fDeduplicationSocial);
			}

		}
		QueryBuilder fbSentiment = null;
		if (!Constant.isNullKey(jsonQuery, "sentimentId"))  //情感id
		{
			JSONArray arr = jsonQuery.getJSONArray("sentimentId");
			if(arr != null && arr.size() > 0) {
				int[] sentimentIds = new int[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					Object value = arr.get(i);
					if(value instanceof String) {
						sentimentIds[i] = Integer.parseInt(arr.getString(i));
					}
					if(value instanceof Integer) {
						sentimentIds[i] = arr.getInt(i);
					}
					sentimentIds[i] = arr.getInt(i);
				}
				fbSentiment = QueryBuilders.termsQuery("sentimentId", sentimentIds);
				filters.add(fbSentiment);
			}
		}
		QueryBuilder fbsentimentOrient = null;
		if (!Constant.isNullKey(jsonQuery, "sentimentOrient"))   //
		{
			JSONArray arr = jsonQuery.getJSONArray("sentimentOrient");
			if(arr != null && arr.size() > 0) {
				int[] sentimentOrients = new int[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					Object value = arr.get(i);
					if(value instanceof String) {
						sentimentOrients[i] = Integer.parseInt(arr.getString(i));
					}
					if(value instanceof Integer) {
						sentimentOrients[i] = arr.getInt(i);
					}
					sentimentOrients[i] = arr.getInt(i);
				}
				fbsentimentOrient = QueryBuilders.termsQuery("sentimentOrient", sentimentOrients);
				filters.add(fbsentimentOrient);
			}
		}
		QueryBuilder fbMediaType = null;
		if (!Constant.isNullKey(jsonQuery, "mediaType"))
		{
			JSONArray arr = jsonQuery.getJSONArray("mediaType");
			if(arr != null && arr.size() > 0) {
				int[] mediaTypes = new int[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					mediaTypes[i] = arr.getInt(i);
				}
				fbMediaType = QueryBuilders.termsQuery("mediaType", mediaTypes);
				filters.add(fbMediaType);
			}
		}
		QueryBuilder fcountryId = null;
		if (!Constant.isNullKey(jsonQuery, "countryId"))
		{
			JSONArray arr = jsonQuery.getJSONArray("countryId");
			if(arr != null && arr.size() > 0) {
				String[] countryIds = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					countryIds[i] = arr.getString(i);
				}
				fcountryId = QueryBuilders.termsQuery("countryId", countryIds);
				filters.add(fcountryId);
			}
		}
		//判断是否有观点
		QueryBuilder fisInter = null;
		if (!Constant.isNullKey(jsonQuery, "isInter")) 
		{
			Boolean isInter = jsonQuery.getBoolean("isInter");
			if(isInter) {
				fisInter = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("isInter", isInter));
				filters.add(fisInter);
			}
		}
		QueryBuilder fbpubDay = null;
		if (!Constant.isNullKey(jsonQuery, "pubDay"))
		{
			JSONArray arr = jsonQuery.getJSONArray("pubDay");
			if(arr != null && arr.size() > 0) {
				String[] pubDays = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					pubDays[i] = arr.getString(i);
				}
				fbpubDay = QueryBuilders.termsQuery("pubDay", pubDays);
				filters.add(fbpubDay);
			}
		}
		QueryBuilder fbtimeDay = null;
		if (!Constant.isNullKey(jsonQuery, "timeDay"))
		{
			JSONArray arr = jsonQuery.getJSONArray("timeDay");
			if(arr != null && arr.size() > 0) {
				String[] timeDays = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					timeDays[i] = arr.getString(i);
				}
				fbtimeDay = QueryBuilders.termsQuery("timeDay", timeDays);
				filters.add(fbtimeDay);
			}
		}
		QueryBuilder fbnotMediaLevel360 = null; //除了mediaLevel360
		if (!Constant.isNullKey(jsonQuery, "notMediaLevel360"))
		{
			Boolean flag = jsonQuery.getBoolean("notMediaLevel360");
			if(flag) {
				fbnotMediaLevel360 = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("mediaLevel360", new String[]{"6","7","8","9","10"}));
				filters.add(fbnotMediaLevel360);
			}
		}
		QueryBuilder fbpubTime = null;
		if (!Constant.isNullKey(jsonQuery, "pubTime"))
		{
			JSONArray arr = jsonQuery.getJSONArray("pubTime");
			if(arr != null && arr.size() > 0) {
				String[] pubTimes = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					pubTimes[i] = arr.getString(i);
				}
				fbpubTime = QueryBuilders.termsQuery("pubTime", pubTimes);
				filters.add(fbpubTime);
			}
		}
		QueryBuilder fbMediaNameZh = null;
		if (!Constant.isNullKey(jsonQuery, "mediaNameZh"))
		{
			JSONArray arr = jsonQuery.getJSONArray("mediaNameZh");
			if(arr != null && arr.size() > 0) {
				String[] mediaTypes = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					mediaTypes[i] = arr.getString(i);
				}
				fbMediaNameZh = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("mediaNameZh", mediaTypes)).must(QueryBuilders.existsQuery("mediaNameZh"));
				filters.add(fbMediaNameZh);
			}
		}
		QueryBuilder fbtransFromM = null;
		if (!Constant.isNullKey(jsonQuery, "transFromM"))
		{
			String transFromM = jsonQuery.getString("transFromM");
			if(StringUtils.isNotBlank(transFromM)) {
				fbtransFromM = QueryBuilders.fuzzyQuery("transFromM", transFromM);
				filters.add(fbtransFromM);
			}
		}
		QueryBuilder fbForbiddenMedia = null;
		if (!Constant.isNullKey(jsonQuery, "forbiddenMedia"))
		{
			JSONArray arr = jsonQuery.getJSONArray("forbiddenMedia");
			if(arr != null && arr.size() > 0) {
				String[] forbiddenMedias= new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					forbiddenMedias[i] = arr.getString(i);
				}
				fbForbiddenMedia = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("mediaNameZh", forbiddenMedias));
				filters.add(fbForbiddenMedia);
			}
		}
		QueryBuilder fbName = null;  //名称
		if (!Constant.isNullKey(jsonQuery, "name"))
		{
			JSONArray arr = jsonQuery.getJSONArray("name");
			if(arr != null && arr.size() > 0) {
				String[] names = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					names[i] = arr.getString(i);
				}
				fbName = QueryBuilders.termsQuery("name", names);
				filters.add(fbName);
			}
		}
		QueryBuilder fbdetriment = null;  //治理后的情感值
		if (!Constant.isNullKey(jsonQuery, "detriment"))
		{
			JSONArray arr = jsonQuery.getJSONArray("detriment");
			if(arr != null && arr.size() > 0) {
				String[] names = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					names[i] = arr.getString(i);
				}
				fbdetriment = QueryBuilders.termsQuery("detriment", names);
				filters.add(fbdetriment);
			}
		}
		QueryBuilder fbNotProvince= null;  //治理后的情感值
		if (!Constant.isNullKey(jsonQuery, "notProvince"))
		{
			JSONArray arr = jsonQuery.getJSONArray("notProvince");
			if(arr != null && arr.size() > 0) {
				String[] notProvinces = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					notProvinces[i] = arr.getString(i);
				}
				fbNotProvince = QueryBuilders.boolQuery().must(QueryBuilders.existsQuery("provinceNameZh")).mustNot(QueryBuilders.termsQuery("provinceNameZh", notProvinces));
				filters.add(fbNotProvince);
			}
		}
		QueryBuilder fbNotCountry= null;  
		if (!Constant.isNullKey(jsonQuery, "notCountry"))
		{
			JSONArray arr = jsonQuery.getJSONArray("notCountry");
			if(arr != null && arr.size() > 0) {
				String[] notCountry = new String[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					notCountry[i] = arr.getString(i);
				}
				fbNotCountry = QueryBuilders.boolQuery().must(QueryBuilders.existsQuery("countryNameZh")).mustNot(QueryBuilders.termsQuery("countryNameZh", notCountry));
				filters.add(fbNotCountry);
			}
		}
		QueryBuilder fbUuid = null;
		if (!Constant.isNullKey(jsonQuery, "_id"))
		{
			JSONArray arr = jsonQuery.getJSONArray("_id");
			String[] ids = new String[arr.size()];
			for (int i = 0; i < arr.size(); i++) {
				ids[i] = arr.getString(i);
			}
			fbUuid = QueryBuilders.termsQuery("_id", ids);
			filters.add(fbUuid);
		}
		QueryBuilder[] fbs = new QueryBuilder[filters.size()];
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		for (int i = 0; i < fbs.length; i++) {
			qb.must((QueryBuilder)filters.get(i));
		}
		return qb;
	}
	/**
	 * 匹配舆情价值
	 * @param messaage
	 * @return
	 */
	private static List<Integer> returnOpinionValueList(String opinionValue) {
		List<Integer> list = new ArrayList<>();
		String[] opinionValues = opinionValue.split(",");
		for(int i = 0; i < opinionValues.length; i++) {
			if("蓝色".equals(opinionValues[i])) {
				for(int j = 20; j <= 39; j++) {
					list.add(j);
				}
			}
			if("黄色".equals(opinionValues[i])) {
				for(int j = 40; j <= 59; j++) {
					list.add(j);
				}
			}
			if("橙色".equals(opinionValues[i])) {
				for(int j = 60; j <= 79; j++) {
					list.add(j);
				}
			}
			if("红色".equals(opinionValues[i])) {
				for(int j = 80; j <= 100; j++) {
					list.add(j);
				}
			}
			if("预警".equals(opinionValues[i])) {
				for(int j = 72; j <= 80; j++) {
					list.add(j);
				}
			}
		}
		return list;
	}
	/**
	 * 治理情感
	 * @param message
	 * @return
	 */
	private List<Integer> returnDetrimentList(JSONObject message) {
		List<Integer> list = null;
		if(!Constant.isNullKey(message, "detriment") && StringUtils.isNotBlank(message.getString("detriment"))) {
			list = new ArrayList<>();
			String detriment = message.getString("detriment");
			String[] detriments = detriment.split(",");
			for(int i = 0; i < detriments.length; i++) {
				if("蓝色".equals(detriments[i])) {
					for(int j = -39; j <= -20; j++) {
						list.add(j);
					}
				}
				if("黄色".equals(detriments[i])) {
					for(int j = -59; j <= -40; j++) {
						list.add(j);
					}
				}
				if("橙色".equals(detriments[i])) {
					for(int j = -79; j <= -60; j++) {
						list.add(j);
					}
				}
				if("红色".equals(detriments[i])) {
					for(int j = -100; j <= -80; j++) {
						list.add(j);
					}
				}
			}
		}
		return list;
	}

	@Override
	public void updateSingleJson(String docId,XContentBuilder updateSource, String indexname, String type,
			String routing) {
		try {
			UpdateResponse response = getTransportClient().prepareUpdate(indexname, type, docId)
					.setRouting(routing).setDoc(updateSource).get();
			System.out.println("批量更新记录");
			System.out.println("批量更新记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public GetResponse getSingleJson(String docId, String indexname, String type,
			String routing) {
		try {
			GetResponse response = getTransportClient().prepareGet(indexname, type, docId)
					.setRouting(routing).get();
			System.out.println("批量更新记录");
			System.out.println("批量更新记录");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public CreateIndexResponse createIndex(String indexName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean importBulk(String indexName, String type, String routing, List<String> jsons) throws Exception {
		BulkRequestBuilder bulkRequest = getTransportClient().prepareBulk();
		jsons.forEach(json -> {
			bulkRequest.add(client.prepareIndex(indexName, configPropertiesForEsData.getSocialType()).setRouting(configPropertiesForEsData.getSocialRouting()).setSource(JSONObject.fromObject(json)));
		});	
		return bulkRequest.get().hasFailures();
	}
	@Override
	public boolean importBulkSingle(String indexName, String type, String routing, String json) throws Exception {
		BulkRequestBuilder bulkRequest = getTransportClient().prepareBulk();
		bulkRequest.add(client.prepareIndex(indexName, configPropertiesForEsData.getSocialType()).setRouting(configPropertiesForEsData.getSocialRouting()).setSource(JSONObject.fromObject(json)));
		return bulkRequest.get().hasFailures();
	}


}
