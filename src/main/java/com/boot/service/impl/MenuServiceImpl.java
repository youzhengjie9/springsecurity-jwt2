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
    public List<String> getBlackEndPermissionByUserId(long id) {

        return menuMapper.getBlackEndPermissionByUserId(id);
    }

    @Override
    public List<Menu> getFrontEndMenuByUserId(long id) {
        return menuMapper.getFrontEndMenuByUserId(id);
    }

    @Override
    public List<Menu> getFrontEndMenuByUserIdAndParantId(long id, long parentId) {
        return menuMapper.getFrontEndMenuByUserIdAndParantId(id, parentId);
    }

}
