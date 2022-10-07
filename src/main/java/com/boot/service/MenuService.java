package com.boot.service;

import com.boot.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author youzhengjie 2022-10-06 14:18:29
 */
public interface MenuService {

    /**
     * 查询普通后台接口权限perms集合
     */
    List<String> getBlackEndPermissionByUserId(long id);

    /**
     * 查询前端侧边栏菜单
     */
    List<Menu> getFrontEndMenuByUserId(long id);

    /**
     * 根据userid和parentid获取前端侧边栏菜单
     */
    List<Menu> getFrontEndMenuByUserIdAndParantId(@Param("id") long id,@Param("parentId") long parentId);



}
