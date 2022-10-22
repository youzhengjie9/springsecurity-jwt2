package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.UserLoginDTO;
import com.boot.enums.ResponseType;
import com.boot.mapstruct.UserMapStruct;
import com.boot.security.LoginUser;
import com.boot.service.LoginService;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
import com.boot.vo.TokenVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private MenuTreeService menuTreeService;

    @Autowired
    private MenuService menuService;

    /**
     * 登录
     *
     * @param userLoginDto 用户登录dto
     * @return {@link ResponseResult}<{@link TokenVO}>
     * @throws Throwable throwable
     */
    @PostMapping("/user/login")
    public ResponseResult<TokenVO> login(@RequestBody @Valid UserLoginDTO userLoginDto, HttpServletRequest request) throws Throwable {


        return loginService.login(userLoginDto,request);
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

            //此处追加一个获取用户动态菜单,生成该用户的动态菜单（由于可能会频繁操作mysql，所以后期可以用缓存技术来减少数据库压力）
            String dynamicMenu = menuTreeService.buildTreeByUserId(loginUser.getUser().getId());
            //由于VUE动态路由刷新会丢失，所以需要再获取获取该用户的所有路由（只包含类型为菜单，type=1的菜单）
            String dynamicRouter = menuService.getRouterByUserId(loginUser.getUser().getId());
            tokenVO.setDynamicMenu(dynamicMenu);
            tokenVO.setDynamicRouter(dynamicRouter);

            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),tokenVO);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage(),null);
        }
    }

}