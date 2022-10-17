package com.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.boot.entity.Menu;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            return JSON.toJSONString(rootMenu);
        } catch (Exception e) {
            return null;
        }
    }

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
