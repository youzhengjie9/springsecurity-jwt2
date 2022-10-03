package com.boot.service.impl;

import com.boot.config.JwtProperties;
import com.boot.data.ResponseResult;
import com.boot.dto.UserDto;
import com.boot.enums.ResponseType;
import com.boot.exception.UserNameOrPassWordException;
import com.boot.security.LoginUser;
import com.boot.service.LoginService;
import com.boot.utils.JwtUtil;
import com.boot.vo.TokenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author youzhengjie 2022-09-29 23:36:12
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    /**
     * redis key前缀。记录每一个登录用户的信息
     */
    private static final String LOGIN_KEY_PREFIX="loginUser:";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * loginUser过期时间。默认单位（毫秒）,accessToken初始化过期时间+（refreshToken初始化过期时间/2）*最大刷新token次数
     */
    private long expired;

    /**
     * 初始化配置
     */
    @PostConstruct
    void initProperties(){

        expired=(jwtProperties.getRefreshTokenExpired()/2) * jwtProperties.getMaxRefreshCount() + jwtProperties.getAccessTokenExpired();

    }

    @Override
    public ResponseResult<TokenVO> login(UserDto userDto) throws Throwable {

        //这个就是我们前端表单传入的UserDto（封装了前端提交的帐号密码），目的是为了后面检查帐号密码是否正确
        //--------------------
        //UsernamePasswordAuthenticationToken两个参数的构造方法就是用来分别传递帐号密码的。（这里我们一定要使用这个）
        //UsernamePasswordAuthenticationToken三个参数的构造方法才是用来证明用户已经登录。
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUserName(),userDto.getPassword());

        //1：authenticationManager.authenticate底层就是调用了UserDetailsService的loadUserByUserName方法，获取到UserDetails对象（也就是LoginUser对象）
        //2：将usernamePasswordAuthenticationToken（前端传入的帐号密码）和loadUserByUsername中的userMapper.selectOne(lambdaQueryWrapper)方法查询的帐号密码进行比对，判断帐号密码输入是否正确。
        //如果验证失败的话，就会在loadUserByUsername方法中抛出异常并且被AuthenticationEntryPointImpl方法捕获
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        //------走到这一步，证明帐号密码都是正确的，可以给前端返回jwt token了
        // 本质上authenticate.getPrincipal()拿到的就是LoginUser对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        //根据userid生成accessToken和refreshToken
        Map<String, String> accessTokenAndRefreshTokenMap = JwtUtil.createAccessTokenAndRefreshToken(userid);

        //从accessTokenAndRefreshTokenMap取出accessToken和refreshToken
        String accessToken = accessTokenAndRefreshTokenMap.get(jwtProperties.getAccessTokenName());
        String refreshToken = accessTokenAndRefreshTokenMap.get(jwtProperties.getRefreshTokenName());

        //将LoginUser对象存入Redis，证明已经登录了
        redisTemplate.opsForValue().set(LOGIN_KEY_PREFIX+userid,loginUser,expired, TimeUnit.MILLISECONDS);

        //将accessToken和refreshToken封装成TokenVO返回给前端
        TokenVO tokenVO = new TokenVO()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);

        return new ResponseResult(ResponseType.SUCCESS.getCode(),"登录成功--success",tokenVO);
    }

}
