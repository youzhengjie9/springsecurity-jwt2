package com.boot.service;


import com.boot.data.ResponseResult;
import com.boot.vo.TokenVO;

public interface LogoutService {

    ResponseResult logout(TokenVO tokenVO);


}
