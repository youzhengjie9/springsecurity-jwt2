package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.entity.LoginLog;

import java.util.List;

/**
 * 登录日志服务
 *
 * @author youzhengjie
 * @date 2022/10/21 23:26:16
 */
public interface LoginLogService extends IService<LoginLog> {

    List<LoginLog> selectAllLoginLogByLimit(int page,int size);

    List<LoginLog> searchLoginLogByUserNameAndLimit(String username,
                                                    int page,
                                                    int size);

    Integer searchLoginLogCountByUserName(String username);
}
