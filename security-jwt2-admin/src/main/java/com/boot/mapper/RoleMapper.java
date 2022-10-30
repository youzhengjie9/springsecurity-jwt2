package com.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.entity.Role;
import com.boot.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色映射器
 *
 * @author youzhengjie
 * @date 2022/10/13 12:12:12
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 查询所有角色并分页（并对分页功能进行性能调优）
     */
    List<Role> selectAllRoleByLimit(@Param("page") int page, @Param("size") int size);

    /**
     * 查询总角色数
     */
    int selectAllRoleCount();

    /**
     * 查询所有角色
     */
    List<Role> selectAllRole();


    /**
     * 通过userid来查询指定用户当前所拥有的role角色列表
     *
     * @param userid 用户标识
     * @return {@link List}<{@link Role}>
     */
    List<Role> selectUserCheckedRoleByUserId(@Param("userid") long userid);


    /**
     * 删除角色所有菜单
     *
     * @param roleId 角色id
     * @return int
     */
    int deleteRoleAllMenu(@Param("roleId") Long roleId);

    /**
     * 给角色添加所有菜单
     *
     * @param roleMenuList 角色菜单列表
     * @return int
     */
    int addMenuToRole(@Param("roleMenuList") List<RoleMenu> roleMenuList);

    /**
     * mysql通过role的name关键字搜索
     *
     * @param roleName 角色名
     * @param page     页面
     * @param size     大小
     * @return {@link List}<{@link Role}>
     */
    List<Role> searchRoleByRoleNameAndLimit(@Param("roleName") String roleName,
                                            @Param("page") int page,
                                            @Param("size") int size);

    /**
     * 按role的name搜索role数量
     *
     * @param roleName 角色名
     * @return int
     */
    int searchRoleCountByRoleName(@Param("roleName") String roleName);

}
