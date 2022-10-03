package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.service.RefreshTokenService;
import com.boot.vo.TokenVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author youzhengjie 2022-09-30 00:39:43
 */
@RestController
@Api("刷新token接口")
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * RequestHeader注解可以接收前端传来的名为value（refreshToken）的请求头，
     * 并自动把接收到的refreshToken请求头存入String refreshToken
     * @param refreshToken 请求头中名为（refreshToken）的内容
     * @return ResponseResult<TokenVO>
     */
    @PostMapping("/refreshToken")
    public ResponseResult<TokenVO> refreshToken(@RequestHeader(value = "refreshToken") String refreshToken){

        return refreshTokenService.refresh(refreshToken);
    }

}
