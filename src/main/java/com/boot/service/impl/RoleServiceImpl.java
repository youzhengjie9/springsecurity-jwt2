package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.entity.Role;
import com.boot.mapper.RoleMapper;
import com.boot.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
}
