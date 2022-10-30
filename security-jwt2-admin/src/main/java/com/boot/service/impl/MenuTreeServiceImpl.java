package com.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.entity.Menu;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单树服务impl
 *
 * @author youzhengjie
 * @date 2022-10-06 15:05:52
 */
@Service
@Slf4j
public class MenuTreeServiceImpl implements MenuTreeService {

    @Autowired
    private MenuService menuService;

    //顶层菜单固定id
    private static final Long TOP_MENU_ID=0L;

    /**
     * 根据用户的userid来构建前端的后台管理系统侧边栏菜单
     * @return 菜单的json串
     */
    @Override
    public String buildTreeByUserId(long userid){
        try {
            //查询所有菜单
            List<Menu> allMenu = menuService.getMenuListByUserId(userid);
            //根节点
            List<Menu> rootMenu = new ArrayList<Menu>();
            for (Menu nav : allMenu) {
                if(nav.getParentId()==0){//父节点是0的，为根节点。
                    rootMenu.add(nav);
                }
            }
            /* 根据Menu类的order排序 */
            Collections.sort(rootMenu);

            //为根菜单设置子菜单，getClild是递归调用的
            for (Menu nav : rootMenu) {
                /* 获取根节点下的所有子节点 使用getChild方法*/
                List<Menu> childList = getChild(nav.getId(), allMenu);
                nav.setChildren(childList);//给根节点设置子节点
            }
            Menu dashboardMenu = Menu.builder()
                    .id(-66L)
                    .menuName("仪表盘")
                    .path("/dashboard")
                    .icon("el-icon-s-home")
                    .component("../views/dashboard/index")
                    .children(new ArrayList<>())
                    .build();
            rootMenu.add(0,dashboardMenu);

            return JSON.toJSONString(rootMenu);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 将系统所有菜单权限构建成一棵树（应用于菜单管理表格数据）
     * @return
     */
    @Override
    public String buildAllMenuPermissionTree() {
        try {
            //查询所有菜单
            List<Menu> allMenu = menuService.getAllMenuPermission();
            //根节点
            List<Menu> rootMenu = new ArrayList<Menu>();
            for (Menu nav : allMenu) {
                if(nav.getParentId()==0){//父节点是0的，为根节点。
                    rootMenu.add(nav);
                }
            }
            /* 根据Menu类的order排序 */
            Collections.sort(rootMenu);

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

    /**
     * 构建分配菜单的树（和上面buildAllMenuPermissionTree方法区别仅仅是这个方法只展示部分需要的字段、而buildAllMenuPermissionTree方法展示所有字段）
     *
     * @return {@link String}
     */
    @Override
    public String buildAssignMenuTree() {
        try {
            //查询菜单
            List<Menu> allMenu = menuService.getAssignMenuTreePermission();
            //根节点
            List<Menu> rootMenu = new ArrayList<Menu>();
            for (Menu nav : allMenu) {
                if(nav.getParentId()==0){//父节点是0的，为根节点。
                    rootMenu.add(nav);
                }
            }
            /* 根据Menu类的order排序 */
            Collections.sort(rootMenu);

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

    /**
     * 根据新增的菜单的菜单类型来构建可以选择的菜单树（应用于菜单管理中的所属菜单上）
     *
     * 如果新增的是目录type=0（可以选择的所属菜单有）：顶层目录（也就是第一层目录）、其他目录
     * 如果新增的是菜单type=1（可以选择的所属菜单有）：顶层目录（也就是第一层目录）、其他目录
     * 如果新增的是按钮type=2（可以选择的所属菜单有）：菜单
     * @param type 类型
     * @return {@link String}
     */
    @Override
    public String buildCanChooseMenuTreeByNewMenuType(int type) {

        //如果新增的是目录或者菜单
        if(type==0 || type==1){

            try {
                //如果新增的是目录或者菜单，那么只查询目录-----
                List<Menu> allMenu = menuService.onlySelectDirectory();
                //根节点
                List<Menu> rootMenu = new ArrayList<Menu>();

                for (Menu nav : allMenu) {
                    if(nav.getParentId()==0){//父节点是0的，为根节点。
                        rootMenu.add(nav);
                    }
                }
                /* 根据Menu类的order排序 */
                Collections.sort(rootMenu);

                //为根菜单设置子菜单，getClild是递归调用的
                for (Menu nav : rootMenu) {
                    /* 获取根节点下的所有子节点 使用getChild方法*/
                    List<Menu> childList = getChild(nav.getId(), allMenu);
                    nav.setChildren(childList);//给根节点设置子节点
                }

                //当树生成好之后我们再给它第一个位置补上“顶层菜单”
                //初始化一个默认的顶层菜单
                Menu topMenu =
                        Menu.builder()
                                //顶层菜单名称
                                .menuName("顶层菜单")
                                //顶层菜单id
                                .id(TOP_MENU_ID)
                                .parentId(0L)
                                .sort(1)
                                .type(0)
                                .build();
                rootMenu.add(0,topMenu);

                return JSON.toJSONString(rootMenu);
            } catch (Exception e) {
                return null;
            }

        }
        //如果新增的是按钮
        else {

            //如果新增的是按钮，那么只查询菜单
            List<Menu> menus = menuService.onlySelectMenu();

            //由于菜单不具有子菜单，所以这里不需要调用getChild进行构建菜单树，直接返回即可

            return JSON.toJSONString(menus);
        }
    }

    private List<Menu> getChild(long id,List<Menu> allMenu){
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
        Collections.sort(childList);//排序

        //如果节点下没有子节点，返回一个空List（递归退出）
        if(childList.size() == 0){
            return new ArrayList<Menu>();
        }
        return childList;
    }


}
