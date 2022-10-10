package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.UserDto;
import com.boot.service.LoginService;
import com.boot.vo.TokenVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 *
 * @author youzhengjie
 * @date 2022/10/10 16:51:55
 */
@RestController
@Api("登录接口")
public class LoginController {

    @Autowired
    private LoginService loginService;


    /**
     * 登录
     *
     * @param userDto 用户dto
     * @return {@link ResponseResult}<{@link TokenVO}>
     * @throws Throwable throwable
     */
    @PostMapping("/user/login")
    public ResponseResult<TokenVO> login(@RequestBody UserDto userDto) throws Throwable {


        return loginService.login(userDto);
    }


}