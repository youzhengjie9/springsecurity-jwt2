package com.boot.security.handle;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.utils.WebUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义失败处理器（权限不足调用）
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ResponseResult result = new ResponseResult(ResponseType.NOT_PERMISSION.getCode(), ResponseType.NOT_PERMISSION.getMessage());
        String jsonString = JSON.toJSONString(result);
        WebUtil.writeJsonString(response,jsonString);
    }
}
