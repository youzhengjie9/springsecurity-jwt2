package com.boot.service.impl;

import com.boot.config.JwtProperties;
import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.service.RefreshTokenService;
import com.boot.utils.JwtUtil;
import com.boot.vo.TokenVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author youzhengjie 2022-10-03 14:38:15
 */
@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public ResponseResult<TokenVO> refresh(String refreshToken) {
        //1：判断前端传过来的refreshToken是否合法、是否过期
        if(JwtUtil.canRefresh(refreshToken)){
            //如果可以刷新token的话
            Claims claims = JwtUtil.parseRefreshToken(refreshToken);

            String subject = claims.getSubject();

            Map<String, String> accessTokenAndRefreshTokenMap = JwtUtil.createAccessTokenAndRefreshToken(subject);

            //从accessTokenAndRefreshTokenMap取出accessToken和refreshToken
            String newAccessToken = accessTokenAndRefreshTokenMap.get(jwtProperties.getAccessTokenName());
            String newRefreshToken = accessTokenAndRefreshTokenMap.get(jwtProperties.getRefreshTokenName());

            //将accessToken和refreshToken封装成TokenVO返回给前端
            TokenVO tokenVO = new TokenVO()
                    .setAccessToken(newAccessToken)
                    .setRefreshToken(newRefreshToken);
            return new ResponseResult<>
                    (ResponseType.REFRESH_TOKEN_SUCCESS.getCode(),ResponseType.REFRESH_TOKEN_SUCCESS.getMessage(),tokenVO);
        }
        return new ResponseResult<>(ResponseType.REFRESH_TOKEN_ERROR.getCode(),ResponseType.REFRESH_TOKEN_ERROR.getMessage(),null);

    }
}
