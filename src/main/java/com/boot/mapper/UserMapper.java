package com.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.entity.User;
import com.boot.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询所有用户并分页（并对分页功能进行性能调优）
     */
    List<User> selectAllUserByLimit(@Param("page") int page, @Param("size") int size);

    /**
     * 查询总用户数
     */
    int selectAllUserCount();

    /**
     * 用户信息修改
     */
    int updateUser(User user);


    /**
     * 给用户添加角色
     *
     * @param userRoleList 用户角色列表
     * @return int
     */
    int addRoleToUser(@Param("userRoleList") List<UserRole> userRoleList);

    /**
     * 删除用户所有角色
     *
     * @param userid 用户标识
     * @return int
     */
    int deleteUserAllRoles(@Param("userid") long userid);
}
