package com.boot.service.impl;

import com.boot.config.JwtProperties;
import com.boot.data.ResponseResult;
import com.boot.dto.UserLoginDTO;
import com.boot.enums.ResponseType;
import com.boot.security.LoginUser;
import com.boot.service.LoginService;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
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
import java.util.concurrent.TimeUnit;

/**
 * 登录服务impl
 *
 * @author youzhengjie
 * @date 2022-09-29 23:36:12
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

    @Autowired
    private MenuTreeService menuTreeService;

    @Autowired
    private MenuService menuService;

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
    public ResponseResult<TokenVO> login(UserLoginDTO userLoginDto) throws Throwable {

//        //通过codeKey就可以从Redis中获取正确的验证码
//        String realCode = (String) redisTemplate.opsForValue().get(userLoginDto.getCodeKey());
//        //校验验证码是否正确，如果不正确返回604响应码
//        if(!userLoginDto.getCode().equals(realCode)){
//            return new ResponseResult<>(ResponseType.CODE_ERROR.getCode(),ResponseType.CODE_ERROR.getMessage());
//        }

        //这个就是我们前端表单传入的UserDto（封装了前端提交的帐号密码），目的是为了后面检查帐号密码是否正确
        //--------------------
        //UsernamePasswordAuthenticationToken两个参数的构造方法就是用来分别传递帐号密码的。（这里我们一定要使用这个）
        //UsernamePasswordAuthenticationToken三个参数的构造方法才是用来证明用户已经登录。
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(),userLoginDto.getPassword());

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

        //生成该用户的动态菜单
        String dynamicMenu = menuTreeService.buildTreeByUserId(Long.parseLong(userid));
        //获取该用户的所有路由（只包含类型为菜单，type=1的菜单）
        String dynamicRouter = menuService.getRouterByUserId(Long.parseLong(userid));
        //将accessToken和refreshToken封装成TokenVO返回给前端
        TokenVO tokenVO = new TokenVO()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setNickName(loginUser.getUser().getNickName())
                .setAvatar(loginUser.getUser().getAvatar())
                .setUserName(loginUser.getUser().getUserName())
                .setDynamicMenu(dynamicMenu)
                .setDynamicRouter(dynamicRouter);

        return new ResponseResult(ResponseType.LOGIN_SUCCESS.getCode(),ResponseType.LOGIN_SUCCESS.getMessage(),tokenVO);
    }

}
