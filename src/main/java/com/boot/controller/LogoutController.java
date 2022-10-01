package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.service.LogoutService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author youzhengjie 2022-09-30 00:40:32
 */
@RestController
@Api("退出登录接口")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    @GetMapping("/user/logout")
    public ResponseResult logout(String token){

        return logoutService.logout(token);
    }
    

}
