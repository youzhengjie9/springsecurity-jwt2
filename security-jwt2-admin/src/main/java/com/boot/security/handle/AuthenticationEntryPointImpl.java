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

        String responseResultJson = (String) request.getAttribute("responseResult");

        WebUtil.writeJsonString(response,responseResultJson);
    }
}
