package boot;

import cn.hutool.core.bean.BeanUtil;
import com.boot.SecurityJwtApplication;
import com.boot.entity.OperationLog;
import com.boot.service.OperationLogService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = SecurityJwtApplication.class)
public class ElasticSearchTest02 {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private OperationLogService operationLogService;

    @Test
    void test1(){

        List<OperationLog> operationLogList = operationLogService.selectAllOperationLogByLimit(0, 10);

        List<OperationLog> operationLogList1 = operationLogService.selectAllOperationLogByLimit(10, 10);

        System.out.println(operationLogList);

        System.out.println(operationLogList1);


    }

    @Test
    void test2(){

        long count = operationLogService.count();
        System.out.println(count);

    }

    @Test
    void test3(){

        OperationLog operationLog = operationLogService
                .lambdaQuery()
                .eq(OperationLog::getId, 5529215069651973L)
                .one();

        Map<String, Object> operationLogMap = new HashMap<>();
        BeanUtil.copyProperties(operationLog,operationLogMap);

        //把map中的id去掉
        operationLogMap.remove("id");

        operationLogMap.forEach((k,v) ->{

            System.out.println(k+"====>"+v);

        });


    }

    @Test
    void delete() throws IOException {

        DeleteRequest deleteRequest = new DeleteRequest("operation-log-index");
        deleteRequest.id("5540810540647429");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status().getStatus());
        System.out.println(deleteResponse.toString());

    }


}
