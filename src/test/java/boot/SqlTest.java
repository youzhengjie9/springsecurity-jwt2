package boot;

import com.alibaba.fastjson.JSON;
import com.boot.SecurityJwtApplication;
import com.boot.entity.Menu;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
import com.boot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest(classes = SecurityJwtApplication.class)
public class SqlTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuTreeService menuTreeService;

    @Autowired
    private UserService userService;

    @Test
    void test01(){

        menuService.getBlackEndPermissionByUserId(1001)
                .stream()
                .forEach(System.out::println);

    }

    @Test
    void buildTree(){

        String menuTree = menuTreeService.buildTree(1001);
        System.out.println(menuTree);
    }

    @Test
    void getUserList(){
        System.out.println(JSON.toJSONString(userService.selectAllUserByLimit(0,10)));
    }



}
