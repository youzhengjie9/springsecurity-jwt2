package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.entity.LoginLog;
import com.boot.mapper.LoginLogMapper;
import com.boot.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录日志服务impl
 *
 * @author youzhengjie
 * @date 2022/10/21 23:29:47
 */
@Service
@Slf4j
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public List<LoginLog> selectAllLoginLogByLimit(int page, int size) {

        return loginLogMapper.selectAllLoginLogByLimit(page, size);
    }

    @Override
    public List<LoginLog> searchLoginLogByUserNameAndLimit(String username, int page, int size) {

        return loginLogMapper.searchLoginLogByUserNameAndLimit(username, page, size);
    }

    @Override
    public Integer searchLoginLogCountByUserName(String username) {
        return loginLogMapper.searchLoginLogCountByUserName(username);
    }

}
