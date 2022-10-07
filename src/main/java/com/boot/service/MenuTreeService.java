package com.boot.service;

import com.boot.entity.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author youzhengjie 2022-10-06 14:31:43
 */
@FunctionalInterface //函数式接口
public interface MenuTreeService {

    /**
     * 前端的后台管理系统侧边栏菜单
     */
    List<Menu> buildTree(long userid);



}
