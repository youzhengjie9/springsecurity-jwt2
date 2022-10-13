package com.boot.service.impl;

import com.boot.entity.Menu;
import com.boot.mapper.MenuMapper;
import com.boot.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

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

}
