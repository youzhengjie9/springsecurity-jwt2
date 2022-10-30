package boot;

import com.boot.SecurityJwtApplication;
import com.boot.vo.ServerInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SecurityJwtApplication.class)
public class ServerInfoTest {


    /**
     * 获取服务器信息
     */
    @Test
    void test() throws Exception {

        //1：通过调用ServerInfoVO类的init方法获取到ServerInfoVO对象
        ServerInfoVO serverInfo = ServerInfoVO.init();

        //2：开始获取
        System.out.println(serverInfo.getCpu().getCpuNum());
        System.out.println(serverInfo.getMem().getTotal());
        System.out.println(serverInfo.getJvm().getMax());
        System.out.println(serverInfo.getSys().getComputerIp());
        System.out.println(serverInfo.getSysFiles().get(0).getUsage());

    }

}
