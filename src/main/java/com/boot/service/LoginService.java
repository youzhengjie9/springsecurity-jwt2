package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.dto.UserDto;
import com.boot.vo.TokenVO;

/**
 * @author youzhengjie 2022-09-23 11:36:55
 */
public interface LoginService {


    ResponseResult<TokenVO> login(UserDto userDto) throws Throwable;


}
