package com.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.dto.RoleFormDto;
import com.boot.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色映射器
 *
 * @author youzhengjie
 * @date 2022/10/13 12:12:12
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 查询所有角色并分页（并对分页功能进行性能调优）
     */
    List<Role> selectAllRoleByLimit(@Param("page") int page, @Param("size") int size);

    /**
     * 查询总角色数
     */
    int selectAllRoleCount();

    /**
     * 查询所有角色
     */
    List<Role> selectAllRole();


    /**
     * 通过userid来查询指定用户当前所拥有的role角色列表
     *
     * @param userid 用户标识
     * @return {@link List}<{@link Role}>
     */
    List<Role> selectUserCheckedRoleByUserId(@Param("userid") long userid);


}
