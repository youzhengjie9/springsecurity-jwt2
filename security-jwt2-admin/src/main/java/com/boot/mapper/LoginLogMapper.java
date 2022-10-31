package com.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 登录日志映射器
 *
 * @author youzhengjie
 * @date 2022/10/21 23:24:36
 */
@Mapper
@Repository
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    List<LoginLog> selectAllLoginLogByLimit(@Param("page") int page,@Param("size") int size);

    List<LoginLog> searchLoginLogByUserNameAndLimit(@Param("username") String username,
                                                    @Param("page") int page,
                                                    @Param("size") int size);

    Integer searchLoginLogCountByUserName(@Param("username") String username);

}
