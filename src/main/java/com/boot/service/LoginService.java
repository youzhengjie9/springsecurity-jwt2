package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.dto.UserLoginDto;
import com.boot.vo.TokenVO;

/**
 * @author youzhengjie 2022-09-23 11:36:55
 */
public interface LoginService {


    ResponseResult<TokenVO> login(UserLoginDto userLoginDto) throws Throwable;


}
