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
     * 将系统所有菜单权限构建成一棵树（应用于菜单管理表格数据）
     * @return
     */
    String buildAllMenuPermissionTree();

    /**
     * 构建分配菜单的树（和上面buildAllMenuPermissionTree方法区别仅仅是这个方法只展示部分需要的字段、而buildAllMenuPermissionTree方法展示所有字段）
     *
     * @return {@link String}
     */
    String buildAssignMenuTree();


    /**
     * 根据新增的菜单的菜单类型来构建可以选择的菜单树（应用于菜单管理中的所属菜单上）
     *
     * 如果新增的是目录type=0（可以选择的所属菜单有）：顶层目录（也就是第一层目录）、其他目录
     * 如果新增的是菜单type=1（可以选择的所属菜单有）：顶层目录（也就是第一层目录）、其他目录
     * 如果新增的是按钮type=2（可以选择的所属菜单有）：菜单
     * @param type 类型
     * @return {@link String}
     */
    String buildCanChooseMenuTreeByNewMenuType(int type);



}
