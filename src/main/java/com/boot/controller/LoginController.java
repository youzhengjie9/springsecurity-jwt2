package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.UserLoginDto;
import com.boot.enums.ResponseType;
import com.boot.mapstruct.UserMapStruct;
import com.boot.security.LoginUser;
import com.boot.service.LoginService;
import com.boot.vo.TokenVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @Autowired
    private UserMapStruct userMapStruct;

    /**
     * 登录
     *
     * @param userLoginDto 用户登录dto
     * @return {@link ResponseResult}<{@link TokenVO}>
     * @throws Throwable throwable
     */
    @PostMapping("/user/login")
    public ResponseResult<TokenVO> login(@RequestBody @Valid UserLoginDto userLoginDto) throws Throwable {


        return loginService.login(userLoginDto);
    }

    /**
     * 得到当前用户信息
     * 记住：要携带accessToken
     * @return {@link ResponseResult}
     */
    @GetMapping("/user/getCurrentUserInfo")
    public ResponseResult<TokenVO> getCurrentUserInfo(){

        try {
            LoginUser loginUser=(LoginUser)SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            TokenVO tokenVO = userMapStruct.loginUserToTokenVO(loginUser);
            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),tokenVO);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage(),null);
        }
    }

}