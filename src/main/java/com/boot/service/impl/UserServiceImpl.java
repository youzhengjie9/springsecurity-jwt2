package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.config.JwtProperties;
import com.boot.dto.UserFormDto;
import com.boot.entity.User;
import com.boot.mapper.UserMapper;
import com.boot.mapstruct.UserMapStruct;
import com.boot.security.LoginUser;
import com.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * redis key前缀。记录每一个登录用户的信息
     */
    private static final String LOGIN_KEY_PREFIX="loginUser:";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * loginUser过期时间。默认单位（毫秒）,accessToken初始化过期时间+（refreshToken初始化过期时间/2）*最大刷新token次数
     */
    private long expired;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMapStruct userMapStruct;
    /**
     * 初始化配置
     */
    @PostConstruct
    void initProperties(){

        expired=(jwtProperties.getRefreshTokenExpired()/2) * jwtProperties.getMaxRefreshCount() + jwtProperties.getAccessTokenExpired();

    }

    /**
     * 更新Redis中的loginUser。
     * 作用：当我们修改了用户的一些信息就可以通过调用这个方法更新Redis中的loginUser，使得前端可以实时获取到当前用户最新数据
     * @param loginUser
     * @return true为更新成功，false为更新失败
     */
    @Override
    public boolean updateLoginUser(LoginUser loginUser) {
        try {
            //将LoginUser对象存入Redis，证明已经登录了
            redisTemplate.opsForValue().
                    set(LOGIN_KEY_PREFIX+loginUser.getUser().getId(),loginUser,expired, TimeUnit.MILLISECONDS);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<User> selectAllUserByLimit(int page, int size) {
        return userMapper.selectAllUserByLimit(page, size);
    }

    @Override
    public int selectAllUserCount() {
        return userMapper.selectAllUserCount();
    }

    @Override
    public int addUser(UserFormDto userFormDto) {
        User user = userMapStruct.userFormDtoToUser(userFormDto);
        user.setStatus(userFormDto.getStatus() ?0:1);

        if("男".equals(userFormDto.getSex())){
            user.setSex(0);
        }else if("女".equals(userFormDto.getSex())){
            user.setSex(1);
        }else{
            user.setSex(2);
        }
        //然后再补充一些前端没有传过来的属性
        user.setCreateTime(LocalDate.now());
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.insert(user);
    }

    @Override
    public int updateUser(UserFormDto userFormDto) {

        User user = userMapStruct.userFormDtoToUser(userFormDto);
        user.setStatus(userFormDto.getStatus() ?0:1);

        if("男".equals(userFormDto.getSex())){
            user.setSex(0);
        }else if("女".equals(userFormDto.getSex())){
            user.setSex(1);
        }else{
            user.setSex(2);
        }
        //然后再补充一些前端没有传过来的属性
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateUser(user);
    }
}
