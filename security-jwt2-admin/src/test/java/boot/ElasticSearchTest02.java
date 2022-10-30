package boot;

import com.boot.SecurityJwtApplication;
import com.boot.entity.OperationLog;
import com.boot.service.OperationLogService;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

}
