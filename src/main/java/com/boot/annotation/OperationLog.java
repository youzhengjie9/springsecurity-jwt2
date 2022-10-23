package com.boot.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解。
 * 功能：只要在controller层接口方法加上这个注解，那么所有访问这个接口的用户信息以及其他信息将会被记录下来
 *
 * @author youzhengjie
 * @date 2022/10/22 22:33:07
 */
//只能作用在方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 注解的value就是这个接口的类型（比如：新增菜单、删除用户等等，也可以算是接口描述）
     */
    String value();

}
