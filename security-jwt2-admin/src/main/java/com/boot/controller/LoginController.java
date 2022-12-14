package com.boot.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.boot.annotation.OperationLog;
import com.boot.data.ResponseResult;
import com.boot.dto.UserLoginDTO;
import com.boot.enums.ResponseType;
import com.boot.security.LoginUser;
import com.boot.service.LoginService;
import com.boot.service.MenuService;
import com.boot.service.MenuTreeService;
import com.boot.vo.TokenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    @ApiOperation("登录接口")
    @PostMapping("/user/login")
    public ResponseResult<TokenVO> login(@RequestBody @Valid UserLoginDTO userLoginDto, HttpServletRequest request) throws Throwable {


        return loginService.login(userLoginDto,request);
    }

    /**
     * 获取当前用户信息
     * 记住：要携带accessToken
     * @return {@link ResponseResult}
     */
    @OperationLog("获取当前用户信息")
    @GetMapping("/user/getCurrentUserInfo")
    @ApiOperation("获取当前用户信息")
    public ResponseResult<TokenVO> getCurrentUserInfo(){

        try {
            LoginUser loginUser=(LoginUser)SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            // TODO: 2022/10/27
            TokenVO tokenVO = BeanUtil.copyProperties(loginUser.getUser(), TokenVO.class);
            System.out.println(tokenVO);

            Long userid = loginUser.getUser().getId();
            //此处追加一个获取用户动态菜单,生成该用户的动态菜单（由于可能会频繁操作mysql，所以后期可以用缓存技术来减少数据库压力）
            String dynamicMenu = menuTreeService.buildTreeByUserId(userid);
            //由于VUE动态路由刷新会丢失，所以需要再获取获取该用户的所有路由（只包含类型为菜单，type=1的菜单）
            String dynamicRouter = menuService.getRouterByUserId(userid);
            tokenVO.setDynamicMenu(dynamicMenu);
            tokenVO.setDynamicRouter(dynamicRouter);

            //设置用户权限perm
            List<String> userPerm = menuService.getUserPermissionByUserId(userid);
            tokenVO.setPerm(JSON.toJSONString(userPerm));

            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),tokenVO);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage(),null);
        }
    }

}