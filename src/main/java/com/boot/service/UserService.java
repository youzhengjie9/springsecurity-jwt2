package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.entity.User;

/**
 * UserService通过继承IService接口，便可以获得IService接口提供的基础功能
 * IService的泛型：为我们要操作的实体类
 * @author youzhengjie 2022-09-22 22:45:02
 */
public interface UserService extends IService<User> {


}
