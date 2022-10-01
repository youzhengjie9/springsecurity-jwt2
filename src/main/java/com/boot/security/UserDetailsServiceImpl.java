package com.boot.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.entity.User;
import com.boot.exception.UserNameOrPassWordException;
import com.boot.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //根据用户名查询user
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));

        if(Objects.isNull(user)){
            log.error("用户名或者密码不正确，请重新输入");
            throw new UserNameOrPassWordException();
        }

        //封装返回loginUser对象
        return new LoginUser(user,null);
    }
}
