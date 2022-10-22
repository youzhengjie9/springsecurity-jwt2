package com.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单映射器
 *
 * @author youzhengjie
 * @date 2022-10-06 14:15:49
 */
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

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
    String selectMenuNameByMenuId(@Param("menuid") long menuid);

    /**
     * 根据用户id拿到这个用户的动态路由（也就是只获取type为1的菜单），返回vue实现动态路由添加
     *
     * @param userid 用户标识
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> getRouterByUserId(@Param("userid") long userid);

    /**
     * 添加菜单
     *
     * @param menu 菜单
     * @return int
     */
    int addMenu(Menu menu);


    /**
     * 修改菜单
     *
     * @param menu 菜单
     * @return int
     */
    int updateMenu(Menu menu);

}
