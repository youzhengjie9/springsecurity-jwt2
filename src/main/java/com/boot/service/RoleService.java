package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.entity.Role;
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
}
