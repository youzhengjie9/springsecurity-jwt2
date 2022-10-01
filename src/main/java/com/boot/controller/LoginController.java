package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.UserDto;
import com.boot.service.LoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("登录接口")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody UserDto userDto) throws Throwable {


        return loginService.login(userDto);
    }


}