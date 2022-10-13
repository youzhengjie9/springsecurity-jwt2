package com.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.entity.User;
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

}
