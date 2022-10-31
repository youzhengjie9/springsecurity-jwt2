package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.dto.UserLoginDTO;
import com.boot.vo.TokenVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author youzhengjie 2022-09-23 11:36:55
 */
public interface LoginService {


    ResponseResult<TokenVO> login(UserLoginDTO userLoginDto, HttpServletRequest request) throws Throwable;



}
