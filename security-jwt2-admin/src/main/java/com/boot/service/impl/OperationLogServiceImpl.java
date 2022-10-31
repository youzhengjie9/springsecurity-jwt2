package com.boot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.entity.OperationLog;
import com.boot.mapper.OperationLogMapper;
import com.boot.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 操作日志服务impl
 *
 * @author youzhengjie
 * @date 2022/10/21 23:42:49
 */
@Service
@Slf4j
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 操作日志的es索引
     */
    private static final String OPER_LOG_INDEX="operation-log-index";


    @Override
    public List<OperationLog> selectAllOperationLogByLimit(int page, int size) {

        try {

            SearchRequest searchRequest = new SearchRequest(OPER_LOG_INDEX);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            //查询条件
            searchSourceBuilder.query(QueryBuilders.termQuery("delFlag",0));

            //根据_id进行排序
            searchSourceBuilder.sort("_id", SortOrder.DESC);

            //分页
            searchSourceBuilder.from(page);
            searchSourceBuilder.size(size);

            searchRequest.source(searchSourceBuilder);


            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHits = searchResponse.getHits().getHits();

            List<OperationLog> operationLogList=new LinkedList<>();
            for (SearchHit searchHit : searchHits) {

                OperationLog operationLog = new OperationLog();
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                //将map拷贝到对象中
                BeanUtil.copyProperties(sourceAsMap,operationLog);
                //因为上面是不会拷贝id的，所以需要手动set一下
                operationLog.setId(Long.parseLong(searchHit.getId()));

                operationLogList.add(operationLog);

            }

            return operationLogList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override
    public long selectAllOperationLogCount() {

        try {
            CountRequest countRequest = new CountRequest(OPER_LOG_INDEX);
            countRequest.query(QueryBuilders.termQuery("delFlag",0));
            CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        }catch (Exception e){
            return 0;
        }

    }

    @Override
    public boolean addOperationLogToEs(OperationLog operationLog) {

        try {
            IndexRequest indexRequest = new IndexRequest(OPER_LOG_INDEX);
            indexRequest.id(operationLog.getId().toString());

            Map<String, Object> sources = new ConcurrentHashMap<>();
            sources.put("username", operationLog.getUsername());
            sources.put("type", operationLog.getType());
            sources.put("uri", operationLog.getUri());
            sources.put("time", operationLog.getTime());
            sources.put("ip", operationLog.getIp());
            sources.put("address",operationLog.getAddress());
            sources.put("browser", operationLog.getBrowser());
            sources.put("os", operationLog.getOs());
            sources.put("operTime", operationLog.getOperTime());
            sources.put("delFlag", operationLog.getDelFlag());

            indexRequest.source(sources);

            restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteOperationLogToEs(Long id) {

        try {
            DeleteRequest deleteRequest = new DeleteRequest(OPER_LOG_INDEX);
            deleteRequest.id(id.toString());
            restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateOperationLogToEs(OperationLog operationLog) {

        try {
            //将operationLog封装成Map
            Map<String,Object> operationLogMap=new ConcurrentHashMap<>();
            //将operationLog拷贝到Map中
            BeanUtil.copyProperties(operationLog,operationLogMap);
            //把map中的id去掉
            operationLogMap.remove("id");

            String idStr = operationLog.getId().toString();
            UpdateRequest updateRequest = new UpdateRequest(OPER_LOG_INDEX,idStr);
            updateRequest.doc(operationLogMap);
            restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<OperationLog> searchOperationLogByUserNameAndLimit(String username, int page, int size) {

        try {

            SearchRequest searchRequest = new SearchRequest(OPER_LOG_INDEX);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            //查询条件
            searchSourceBuilder.query(QueryBuilders.termQuery("delFlag",0))
                    .query(QueryBuilders.matchQuery("username",username));


            //根据_id进行排序
            searchSourceBuilder.sort("_id", SortOrder.DESC);

            //分页
            searchSourceBuilder.from(page);
            searchSourceBuilder.size(size);

            searchRequest.source(searchSourceBuilder);


            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHits = searchResponse.getHits().getHits();

            List<OperationLog> operationLogList=new LinkedList<>();
            for (SearchHit searchHit : searchHits) {

                OperationLog operationLog = new OperationLog();
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                //将map拷贝到对象中
                BeanUtil.copyProperties(sourceAsMap,operationLog);
                //因为上面是不会拷贝id的，所以需要手动set一下
                operationLog.setId(Long.parseLong(searchHit.getId()));

                operationLogList.add(operationLog);
            }
            return operationLogList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override
    public long searchOperationLogCountByUserName(String username) {

        try {
            CountRequest countRequest = new CountRequest(OPER_LOG_INDEX);
            countRequest.query(QueryBuilders.termQuery("delFlag",0))
                    .query(QueryBuilders.matchQuery("username",username));
            CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        }catch (Exception e){
            return 0;
        }


    }


}
