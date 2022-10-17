package com.boot.mapper;

import com.boot.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author youzhengjie 2022-10-06 14:15:49
 */
@Mapper
@Repository
public interface MenuMapper {

    /**
     * 查询指定用户的所有菜单列表（包括目录和菜单，但是不包括按钮）,说白了就是type=0和type=1，后面要构建菜单树
     */
    List<Menu> getMenuListByUserId(@Param("userid") long userid);


    /**
     * 根据userid获取用户权限。（说白了就是获取sys_menu表中type=1和type=2的perms），这就是我们访问任何接口和菜单的权限
     */
    List<String> getUserPermissionByUserId(@Param("userid") long userid);


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
    List<Menu> selectRoleCheckedMenuByRoleId(@Param("roleid") long roleid);

}
