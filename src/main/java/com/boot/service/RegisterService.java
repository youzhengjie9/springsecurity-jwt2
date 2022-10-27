package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.dto.UserRegisterDTO;

/**
 * 注册服务
 *
 * @author youzhengjie
 * @date 2022/10/25 23:45:02
 */
public interface RegisterService {

    /**
     * 注册
     *
     * @param userRegisterDTO 用户注册dto
     * @return {@link ResponseResult}
     */
    ResponseResult register(UserRegisterDTO userRegisterDTO);


    ResponseResult<String> sendCode(String phone);
}
