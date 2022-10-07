package com.boot.service.impl;

import com.boot.entity.Menu;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 动态构建前端的后台管理系统的侧边栏菜单
 * @author youzhengjie 2022-10-06 15:05:52
 */
@Service
@Slf4j
public class MenuTreeServiceImpl implements MenuTreeService {

    @Autowired
    private MenuService menuService;

    // 构建子树
    public Menu buildChildren(Menu rootNode,long userid){
        List<Menu> childrenTree = new ArrayList<>();
        List<Menu> menuList = menuService.getFrontEndMenuByUserId(userid);
        for (Menu menu : menuList) {
            if (menu.getParentId().equals(rootNode.getId())){
                childrenTree.add(buildChildren(rootNode,userid));
            }
        }
        rootNode.setChildren(childrenTree);
        return rootNode;
    }

    @Override
    public List<Menu> buildTree(long userid) {
        List<Menu> menus = menuService.getFrontEndMenuByUserIdAndParantId(userid,0);
        for (Menu menu : menus) {
            buildChildren(menu,userid);
        }
        return menus;
    }


}
