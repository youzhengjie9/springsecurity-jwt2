package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.dto.UserDto;

/**
 * @author youzhengjie 2022-09-23 11:36:55
 */
public interface LoginService {


    ResponseResult login(UserDto userDto) throws Throwable;


}
