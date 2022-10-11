package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.entity.User;
import com.boot.security.LoginUser;

/**
 * UserService通过继承IService接口，便可以获得IService接口提供的基础功能
 * IService的泛型：为我们要操作的实体类
 * @author youzhengjie 2022-09-22 22:45:02
 */
public interface UserService extends IService<User> {


    /**
     * 更新Redis中的loginUser。
     * 作用：当我们修改了用户的一些信息就可以通过调用这个方法更新Redis中的loginUser，使得前端可以实时获取到当前用户最新数据
     * @param loginUser
     * @return true为更新成功，false为更新失败
     */
    boolean updateLoginUser(LoginUser loginUser);

}
