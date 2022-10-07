package com.boot.mapper;

import com.boot.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author youzhengjie 2022-10-06 14:15:49
 */
@Mapper
@Repository
public interface MenuMapper {


    /**
     * 查询普通后台接口权限perms集合
     */
    List<String> getBlackEndPermissionByUserId(@Param("id") long id);


    /**
     * 查询前端侧边栏菜单所有数据
     */
    List<Menu> getFrontEndMenuByUserId(@Param("id") long id);


    /**
     * 根据userid和parentid获取前端侧边栏菜单
     */
    List<Menu> getFrontEndMenuByUserIdAndParantId(@Param("id") long id,@Param("parentId") long parentId);






}
