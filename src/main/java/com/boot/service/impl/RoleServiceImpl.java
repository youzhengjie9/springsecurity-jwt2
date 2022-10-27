package com.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dto.RoleFormDTO;
import com.boot.entity.Role;
import com.boot.entity.RoleMenu;
import com.boot.mapper.RoleMapper;
import com.boot.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色服务impl
 *
 * @author youzhengjie
 * @date 2022/10/17 23:23:21
 */
@Service
@Slf4j
@Transactional //开启事务
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

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
    public int addRole(RoleFormDTO roleFormDto) {

        // TODO: 2022/10/27
        Role role = BeanUtil.copyProperties(roleFormDto, Role.class);
        System.out.println(role);

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
    public int updateRole(RoleFormDTO roleFormDto) {

        // TODO: 2022/10/27
        Role role = BeanUtil.copyProperties(roleFormDto, Role.class);
        System.out.println(role);
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

        try {
            //先删除角色对应的所有菜单
            roleMapper.deleteRoleAllMenu(roleMenuList.get(0).getRoleId());
            //再把所有新的菜单（包括以前选过的）都重新添加到数据库中
            roleMapper.addMenuToRole(roleMenuList);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("assignMenuToRole异常,事务已回滚。");
        }

    }

    @Override
    public List<Role> searchRoleByRoleNameAndLimit(String roleName, int page, int size) {
        return roleMapper.searchRoleByRoleNameAndLimit(roleName, page, size);
    }

    @Override
    public int searchRoleCountByRoleName(String roleName) {
        return roleMapper.searchRoleCountByRoleName(roleName);
    }
}
