package com.tmxmall.publicsafety.domain;


import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;


import net.sf.json.JSONObject;

/**
 * Created by hanasian on 2017/3/29.
 * 所有方法的接口
 */
public interface PublicSafetySearchDao {
    /**
     * @description: 以transport的形式获取客户端客户端
     * @return:TransportClient
     * @return
     */
    public TransportClient getTransportClient() throws Exception;
    /**
     * @description: 以transport的形式获取客户端客户端
     * @return:TransportClient
     * @return
     */
    public TransportClient getTransportClient1(String ip) throws Exception;
    /**
     * @description:创建一个索引
     * @return:CreateIndexResponse
     * @param indexName
     * @return CreateIndexResponse实例
     */
    public CreateIndexResponse createIndex(String indexName) throws Exception;
    /**
     * @description:据index删除index
     * @return:DeleteIndexResponse
     * @param indexName
     * @return DeleteIndexResponse实例
     */
    public DeleteIndexResponse deleteByIndex(String indexName) throws Exception;
    /**
     * @description:打开index
     * @return:OpenIndexResponse
     * @param indexName
     * @return OpenIndexResponse实例
     */
    public OpenIndexResponse openIndex(String indexName) throws Exception;
    /**
     * @description:索引是否存在
     * @return:boolean
     * @param indexName
     * @return boolean
     */
    public boolean isIndexExists(String indexName) throws Exception;
    /**
     * @description:创建并添加用来描述index的mappingSource映射
     * @return:PutMappingResponse
     * @param indexName
     * @param type
     * @param mappingSource
     * @return
     * @throws Exception
     */
    public PutMappingResponse createAndAddMappings(String indexName, String type, XContentBuilder mappingSource) throws Exception;
    /**
     * @description:判断index下的type是否存在
     * @return:boolean
     * @param indexName
     * @param type
     * @return
     */
    public boolean isTypeExists(String indexName, String type) throws Exception;
    /**
     * 批量从文本文件导入数据
     * @param path
     * @param indexName
     * @param type
     * @param routing
     * @return
     * @throws Exception
     */
    public boolean importDataBulk(String path,String indexName, String type, String routing) throws Exception;
    /**
     * 聚合
     * @param indexName
     * @param type
     * @param routing
     * @param aggregationBuilder
     * @return
     * @throws Exception
     */
    
   /**
    * es新闻查询
    * @param type
    * @param routing
    * @param jsonObject
    * @return
    * @throws Exception
    */
    public SearchResponse esNewsSearch(String type,String routing,JSONObject jsonObject) throws Exception;
    /**
     * es社交查询
     * @param type
     * @param routing
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public SearchResponse esSocialSearch(String type,String routing,JSONObject jsonObject) throws Exception;
    
    public void updateSingleJson(String docID, XContentBuilder updateSource, String indexname_HISTORY, String newstype,
			String newsrouting);
    
    public GetResponse getSingleJson(String docId, String indexname, String type, String routing);

    public boolean importBulk(String indexName, String type, String routing, List<String> jsons) throws Exception;
    
    public boolean importBulkSingle(String indexName, String type, String routing, String json) throws Exception;
}
