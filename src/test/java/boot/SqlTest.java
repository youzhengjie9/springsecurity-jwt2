package boot;

import com.alibaba.fastjson.JSON;
import com.boot.SecurityJwtApplication;
import com.boot.entity.Menu;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
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

    @Test
    void test01(){

        menuService.getBlackEndPermissionByUserId(1001)
                .stream()
                .forEach(System.out::println);

    }

    @Test
    void buildTree(){

        String tree = this.findTree();

        System.out.println(tree);
    }


    public String findTree(){
        try {
            //查询所有菜单
            List<Menu> allMenu = menuService.getFrontEndMenuByUserId(1001);
            //根节点
            List<Menu> rootMenu = new ArrayList<Menu>();
            for (Menu nav : allMenu) {
                if(nav.getParentId()==0){//父节点是0的，为根节点。
                    rootMenu.add(nav);
                }
            }
            /* 根据Menu类的order排序 */
//            Collections.sort(rootMenu, order());

            //为根菜单设置子菜单，getClild是递归调用的
            for (Menu nav : rootMenu) {
                /* 获取根节点下的所有子节点 使用getChild方法*/
                List<Menu> childList = getChild(nav.getId(), allMenu);
                nav.setChildren(childList);//给根节点设置子节点
            }
            return JSON.toJSONString(rootMenu);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Menu> getChild(long id,List<Menu> allMenu){
        //子菜单
        List<Menu> childList = new ArrayList<Menu>();
        for (Menu nav : allMenu) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getParentId().equals(id)){
                childList.add(nav);
            }
        }
        //递归
        for (Menu nav : childList) {
            nav.setChildren(getChild(nav.getId(), allMenu));
        }
//        Collections.sort(childList,order());//排序

        //如果节点下没有子节点，返回一个空List（递归退出）
        if(childList.size() == 0){
            return new ArrayList<Menu>();
        }
        return childList;
    }

}
