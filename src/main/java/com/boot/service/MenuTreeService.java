package com.boot.service;


/**
 * @author youzhengjie 2022-10-06 14:31:43
 */
public interface MenuTreeService {

    /**
     * 根据用户的userid来构建前端的后台管理系统侧边栏菜单
     * @return 菜单的json串
     */
    String buildTreeByUserId(long userid);

    /**
     * 将系统所有菜单权限构建成一棵树
     * @return
     */
    String buildAllMenuPermissionTree();

    /**
     * 构建分配菜单的树（和上面buildAllMenuPermissionTree方法区别仅仅是这个方法只展示部分需要的字段、而buildAllMenuPermissionTree方法展示所有字段）
     *
     * @return {@link String}
     */
    String buildAssignMenuTree();
}
