package com.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dto.MenuDto;
import com.boot.entity.Menu;
import com.boot.mapper.MenuMapper;
import com.boot.service.MenuService;
import com.boot.utils.SnowId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    //顶层菜单名称
    private static final String TOP_MENU_NAME="顶层菜单";


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
        List<Menu> router = menuMapper.getRouterByUserId(userid);
        //这个代码十分重要，解决登陆时，前端因为有些用户没有菜单/路由（也就是这个getRouterByUserId方法查不到数据导致一直死循环）
        //设置一个默认的路由，不管是什么用户、有没有菜单都会有这个默认的路由。防止前端死循环
        Menu defaultRouter = Menu.builder()
                .path("/dashboard")
                .component("/dashboard/index")
                .build();
        router.add(0,defaultRouter);
        return JSON.toJSONString(router);
    }

    @Override
    public int addMenu(MenuDto menuDto) {

        Menu menu = menuDto.getMenu();
        //生成分布式id
        menu.setId(SnowId.nextId());
        //设置parentid
        menu.setParentId(menuDto.getParentId());
        //菜单类型
        menu.setType(menuDto.getMenuType());
        //创建时间
        menu.setCreateTime(LocalDateTime.now());
        //修改时间
        menu.setUpdateTime(LocalDateTime.now());

        return menuMapper.addMenu(menu);
    }

    @Override
    public int updateMenu(MenuDto menuDto) {

        Menu menu = menuDto.getMenu();
        //设置parentid
        menu.setParentId(menuDto.getParentId());
        //菜单类型
        menu.setType(menuDto.getMenuType());
        //修改时间
        menu.setUpdateTime(LocalDateTime.now());

        return menuMapper.updateMenu(menu);
    }

    @Override
    public int deleteMenu(long menuid) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getId,menuid);
        return menuMapper.delete(menuLambdaQueryWrapper);
    }

}
