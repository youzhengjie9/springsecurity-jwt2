package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.dto.RoleFormDTO;
import com.boot.entity.Role;
import com.boot.entity.RoleMenu;

import java.util.List;

/**
 * 角色服务
 *
 * @author youzhengjie
 * @date 2022/10/13 12:13:21
 */
public interface RoleService extends IService<Role> {

    /**
     * 查询所有角色并分页（并对分页功能进行性能调优）
     */
    List<Role> selectAllRoleByLimit(int page,int size);

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
    List<Role> selectUserCheckedRoleByUserId(long userid);

    /**
     * 添加角色
     *
     * @param roleFormDto 角色表单dto
     * @return int
     */
    int addRole(RoleFormDTO roleFormDto);

    /**
     * 更新角色
     *
     * @param roleFormDto 角色表单dto
     * @return int
     */
    int updateRole(RoleFormDTO roleFormDto);


    /**
     * 删除角色
     *
     * @param id id
     * @return int
     */
    boolean deleteRole(long id);

    /**
     * 给角色分配菜单权限
     * @param roleMenuList 角色菜单列表
     * @return boolean
     */
    boolean assignMenuToRole(List<RoleMenu> roleMenuList);

    /**
     * mysql通过role的name关键字搜索
     *
     * @param roleName 角色名
     * @param page     页面
     * @param size     大小
     * @return {@link List}<{@link Role}>
     */
    List<Role> searchRoleByRoleNameAndLimit(String roleName, int page, int size);

    /**
     * 按role的name搜索role数量
     *
     * @param roleName 角色名
     * @return int
     */
    int searchRoleCountByRoleName(String roleName);
}
