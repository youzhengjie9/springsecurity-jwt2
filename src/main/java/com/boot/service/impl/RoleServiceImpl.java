package com.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dto.RoleFormDto;
import com.boot.entity.Role;
import com.boot.entity.RoleMenu;
import com.boot.mapper.RoleMapper;
import com.boot.mapstruct.RoleMapStruct;
import com.boot.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMapStruct roleMapStruct;

    @Override
    public List<Role> selectAllRoleByLimit(int page, int size) {
        return roleMapper.selectAllRoleByLimit(page, size);
    }

    @Override
    public int selectAllRoleCount() {
        return roleMapper.selectAllRoleCount();
    }

    @Override
    public List<Role> selectAllRole() {
        return roleMapper.selectAllRole();
    }

    @Override
    public List<Role> selectUserCheckedRoleByUserId(long userid) {
        return roleMapper.selectUserCheckedRoleByUserId(userid);
    }

    @Override
    public int addRole(RoleFormDto roleFormDto) {

        Role role = roleMapStruct.roleFormDtoToRole(roleFormDto);
        if(roleFormDto.getStatus()){
            role.setStatus(0);
        }else {
            role.setStatus(1);
        }
        //然后再补充一些前端没有传过来的属性
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());

        //只有调用mybatis-plus内置的sql方法才会自动生成分布式主键id
        return roleMapper.insert(role);
    }

    @Override
    public int updateRole(RoleFormDto roleFormDto) {
        Role role = roleMapStruct.roleFormDtoToRole(roleFormDto);
        if(roleFormDto.getStatus()){
            role.setStatus(0);
        }else {
            role.setStatus(1);
        }
        //然后再补充一些前端没有传过来的属性
        role.setUpdateTime(LocalDateTime.now());

        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId,role.getId());
        return roleMapper.update(role,roleLambdaQueryWrapper);
    }

    @Override
    public int deleteRole(long id) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId,id);
        return roleMapper.delete(roleLambdaQueryWrapper);
    }

    @Override
    public boolean assignMenuToRole(List<RoleMenu> roleMenuList) {
        return false;
    }
}
