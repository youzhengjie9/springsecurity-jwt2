package com.boot.security;

import com.boot.config.JwtProperties;
import com.boot.exception.NotLoginException;
import com.boot.exception.ParseTokenException;
import com.boot.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * redis key前缀。记录每一个登录用户的信息
     */
    private static final String LOGIN_KEY_PREFIX="loginUser:";

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader(jwtProperties.getAccessTokenName());

        //由于所有请求都要经过这个过滤器，所以分为两种情况
        //1：无accessToken的情况下，直接放行即可。（因为没有token的情况下可能是/user/login接口的请求此时直接放行即可,然后这个请求会经过UsernamePasswordAuthenticationToken之类过滤器）
        if(!StringUtils.hasText(accessToken)){

            filterChain.doFilter(request,response);

        }
        //2:如果有accessToken的情况下
        else {

            String userid=null;
            //先解析accessToken，如果accessToken解析失败则直接报错即可。
            try {
                Claims claims = JwtUtil.parseAccessToken(accessToken);
                userid = claims.getSubject();
            }catch (Exception e){
                log.error("解析token失败，请检查token是否正确");
                throw new ParseTokenException();
            }

            // 判断这个accessToken是否在redis黑名单中,如果在黑名单中，则直接返回失败
            if(redisTemplate.hasKey(jwtProperties.getAccessTokenBlacklistPrefix()+accessToken)){
                log.error("用户未登录，请重新登录！");
                throw new NotLoginException();
            }

            //通过userid去redis中查询（拿到的就是loginUser对象），如果没有记录则直接报错（说明未登录）
            //只有redis中存在该用户的loginUser对象才能证明用户已经登录，否则说明用户未登录
            String key=LOGIN_KEY_PREFIX+userid;
            LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(key);
            if(Objects.isNull(loginUser)){
                log.error("用户未登录，请重新登录！");
                throw new NotLoginException();
            }


            //由于每一次新的请求SecurityContextHolder.getContext()的Authentication都会被自动清空（也就是变成null）。
            //为了解决这个问题，我们要在JwtAuthenticationFilter中判断用户是否登录（判断redis中LOGIN_KEY_PREFIX+userid的key）
            //如果登录了就要重新设置Authentication,把loginUser和权限放进去,确保SpringSecurity知道用户已经登录
            //UsernamePasswordAuthenticationToken两个参数的构造方法就是用来分别传递帐号密码的。
            //UsernamePasswordAuthenticationToken三个参数的构造方法才是用来证明用户已经登录。（这里我们一定要使用这个）
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());

            //这里会把loginUser封装成Authentication放进SecurityContextHolder中，告诉spring security我们已经成功登录了。
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            //最后要放行
            filterChain.doFilter(request, response);

        }

    }
}
