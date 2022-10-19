package com.boot.service;

import com.boot.entity.Menu;

import java.util.List;

/**
 * @author youzhengjie 2022-10-06 14:18:29
 */
public interface MenuService {

    /**
     * 查询指定用户的所有菜单（包括目录和菜单，但是不包括按钮）
     */
    List<Menu> getMenuListByUserId(long userId);

    /**
     * 根据userid获取用户权限。（说白了就是获取sys_menu表中type=1和type=2的perms），这就是我们访问任何接口和菜单的权限
     */
    List<String> getUserPermissionByUserId(long userid);

    /**
     * 获取菜单管理列表中的树型展示数据（说白了就是获取到sys_menu表中type=0和1和2所有数据）
     */
    List<Menu> getAllMenuPermission();

    /**
     * 获取菜单管理列表中的树型展示数据（说白了就是获取到sys_menu表中type=0和1和2所有数据）
     * 和上面getAllMenuPermission方法的区别仅仅是这个方法只展示部分字段
     *
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> getAssignMenuTreePermission();

    /**
     * 通过roleid来查询指定用户当前所拥有的menu菜单列表
     *
     * @param roleid 用户标识
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> selectRoleCheckedMenuByRoleId(long roleid);


    /**
     * 查询sys_menu表，但是只查询目录（type=0）
     */
    List<Menu> onlySelectDirectory();

    /**
     * 查询sys_menu表，但是只查询菜单（type=1）
     */
    List<Menu> onlySelectMenu();

    /**
     * 通过菜单id查询菜单名称
     *
     * @param menuid menuid
     * @return {@link String}
     */
    String selectMenuNameByMenuId(long menuid);

    /**
     * 根据用户id拿到这个用户的动态路由（也就是只获取type为1的菜单），返回vue实现动态路由添加
     *
     * @param userid 用户标识
     */
    String getRouterByUserId(long userid);
}
