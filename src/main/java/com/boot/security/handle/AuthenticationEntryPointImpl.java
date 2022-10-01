package com.boot.security.handle;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.utils.WebUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义失败处理器（登录认证失败后调用）
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ResponseResult result = null;
        //解析token失败异常
        if(authException instanceof InsufficientAuthenticationException){
            result=new ResponseResult(ResponseType.TOKEN_ERROR.getCode(), ResponseType.TOKEN_ERROR.getMessage());
        }
        //用户名或者密码不正确
        else if(authException instanceof InternalAuthenticationServiceException || authException instanceof BadCredentialsException){
            result=new ResponseResult(ResponseType.USERNAME_PASSWORD_ERROR.getCode(), ResponseType.USERNAME_PASSWORD_ERROR.getMessage());
        }
        else {
            result=new ResponseResult(ResponseType.LOGIN_ERROR.getCode(), ResponseType.LOGIN_ERROR.getMessage());
        }
        String jsonString = JSON.toJSONString(result);
        WebUtil.writeJsonString(response,jsonString);
    }
}
