package com.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.entity.Menu;
import com.boot.mapper.MenuMapper;
import com.boot.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 菜单服务impl
 *
 * @author youzhengjie
 * @date 2022/10/17 23:22:12
 */
@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper,Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public List<Menu> getMenuListByUserId(long userId) {
        return menuMapper.getMenuListByUserId(userId);
    }

    @Override
    public List<String> getUserPermissionByUserId(long userid) {
        return menuMapper.getUserPermissionByUserId(userid);
    }

    @Override
    public List<Menu> getAllMenuPermission() {
        return menuMapper.getAllMenuPermission();
    }

    @Override
    public List<Menu> getAssignMenuTreePermission() {
        return menuMapper.getAssignMenuTreePermission();
    }

    @Override
    public List<Menu> selectRoleCheckedMenuByRoleId(long roleid) {

        return menuMapper.selectRoleCheckedMenuByRoleId(roleid);
    }

    @Override
    public List<Menu> onlySelectDirectory() {
        return menuMapper.onlySelectDirectory();
    }

    @Override
    public List<Menu> onlySelectMenu() {
        return menuMapper.onlySelectMenu();
    }

    @Override
    public String selectMenuNameByMenuId(long menuid) {

        return menuMapper.selectMenuNameByMenuId(menuid);
    }

    @Override
    public String getRouterByUserId(long userid) {
        return JSON.toJSONString(menuMapper.getRouterByUserId(userid));
    }

}
