package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.service.LogoutService;
import com.boot.vo.TokenVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author youzhengjie 2022-09-30 00:40:32
 */
@RestController
@Api("退出登录接口")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    /**
     * 用户注销接口。
     * @param accessToken （必须要传）
     * @param refreshToken （可选）
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout(@RequestHeader(value = "accessToken",required = true) String accessToken,
                                 @RequestHeader(value = "refreshToken",required = false) String refreshToken){

        return logoutService.logout(
                TokenVO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }
    

}
