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

}
