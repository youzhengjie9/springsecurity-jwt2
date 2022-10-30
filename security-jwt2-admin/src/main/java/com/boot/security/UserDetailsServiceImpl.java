package com.boot.security;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.data.ResponseResult;
import com.boot.entity.User;
import com.boot.enums.ResponseType;
import com.boot.exception.UserNameOrPassWordException;
import com.boot.mapper.UserMapper;
import com.boot.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //根据用户名查询user
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));

        if(Objects.isNull(user)){
            ResponseResult responseResult = new ResponseResult<>();
            responseResult.setCode(ResponseType.USERNAME_PASSWORD_ERROR.getCode());
            responseResult.setMsg(ResponseType.USERNAME_PASSWORD_ERROR.getMessage());

            String jsonString = JSON.toJSONString(responseResult);
            //将报错信息传来AuthenticationEntryPointImpl类，然后由它进行return到前端
            request.setAttribute("responseResult",jsonString);
            throw new UserNameOrPassWordException();
        }

        //查询用户菜单权限（就是查询Menu类中type=1和2的菜单权限标识perms，但是不包括type=0），并放到loginUser中返回
        List<String> permissions = menuService.getUserPermissionByUserId(user.getId());

        //封装返回loginUser对象
        return new LoginUser(user,permissions);
    }
}
