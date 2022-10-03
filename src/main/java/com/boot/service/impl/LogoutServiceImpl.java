package com.boot.service.impl;

import com.boot.config.JwtProperties;
import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.security.LoginUser;
import com.boot.service.LogoutService;
import com.boot.utils.JwtUtil;
import com.boot.vo.TokenVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * redis key前缀。记录每一个登录用户的信息
     */
    private static final String LOGIN_KEY_PREFIX="loginUser:";

    @Override
    public ResponseResult logout(TokenVO tokenVO) {

        try {
            String accessToken = tokenVO.getAccessToken();
            String refreshToken = tokenVO.getRefreshToken();

            //-----------计算出token的黑名单过期时间最好为多久（单位：毫秒）
            //解析accessToken
            Claims claims = JwtUtil.parseAccessToken(accessToken);
            //获取accessToken最开始生成的时间
            Date accessTokenIssuedAt = claims.getIssuedAt();
            //获取accessToken最开始生成的时间的毫秒值
            long accessTokenIssuedAtMillis = accessTokenIssuedAt.getTime();
            //获取当前时间的毫秒值
            long currentTimeMillis = System.currentTimeMillis();

            //计算accessToken已经用了多久（当前时间毫秒值-accessToken最开始生成的时间的毫秒值）
            long accessTokenTimed=currentTimeMillis-accessTokenIssuedAtMillis;

            //获取refreshToken最大过期时间
            long refreshTokenExpired = jwtProperties.getRefreshTokenExpired();

            //=====计算出黑名单key过期时间最好为多少毫秒
            long blackListExpiredMillis=refreshTokenExpired-accessTokenTimed;

            //黑名单key
            String accessTokenBlackListKey=jwtProperties.getAccessTokenBlacklistPrefix()+accessToken;
            //由于refreshToken可能为null，当refreshToken不为空的时候才算上它
            String refreshTokenBlackListKey = null;
            if(StringUtils.hasText(refreshToken)){
                refreshTokenBlackListKey=jwtProperties.getRefreshTokenBlacklistPrefix()+refreshToken;
            }

            //从获取当前用户
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //把用户对应的redis中存放loginUser对象的key删除掉，这个才能真正保证用户退出了
            redisTemplate.delete(LOGIN_KEY_PREFIX+loginUser.getUser().getId());

            //此时将accessToken和refreshToken分别放入redis黑名单（实际上就是String类型的key并设置了blackListExpiredMillis过期时间）
            redisTemplate.opsForValue().set(accessTokenBlackListKey,"",blackListExpiredMillis, TimeUnit.MILLISECONDS);

            if(StringUtils.hasText(refreshToken)){
                redisTemplate.opsForValue().set(refreshTokenBlackListKey,"",blackListExpiredMillis, TimeUnit.MILLISECONDS);
            }

            return new ResponseResult(ResponseType.LOGOUT_SUCCESS.getCode(),ResponseType.LOGOUT_SUCCESS.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(ResponseType.LOGOUT_ERROR.getCode(),ResponseType.LOGOUT_ERROR.getMessage());
        }
    }


}
