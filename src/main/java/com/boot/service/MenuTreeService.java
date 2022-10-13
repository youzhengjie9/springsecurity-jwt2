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

}
