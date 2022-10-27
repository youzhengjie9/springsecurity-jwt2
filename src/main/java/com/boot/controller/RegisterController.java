package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.UserRegisterDTO;
import com.boot.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 注册用户控制器
 *
 * @author youzhengjie
 * @date 2022/10/26 00:23:31
 */
@RestController
@Api("注册用户控制器")
@RequestMapping(path = "/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 注册
     *
     * @param userRegisterDTO 用户注册dto
     * @return {@link ResponseResult}
     */
    @PostMapping(path = "/")
    @ApiOperation("注册用户接口")
    public ResponseResult register(@RequestBody @Valid UserRegisterDTO userRegisterDTO){

        return registerService.register(userRegisterDTO);
    }

    /**
     * 发送代码
     *
     * @param phone 电话
     * @return {@link ResponseResult}<{@link String}>
     */
    @GetMapping(path = "/sendCode")
    @ApiOperation("发送手机验证码")
    public ResponseResult<String> sendCode(@RequestParam("phone") String phone){

        return registerService.sendCode(phone);
    }


}
