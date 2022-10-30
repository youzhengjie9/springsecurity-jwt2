package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youzhengjie 2022-09-30 00:41:03
 */
@RestController
@Api("测试接口")
public class TestController {

    //使用map来模拟JVM本地缓存
    public static final Map<String,Object> hashMap=new HashMap<>();

    /**
     * 当用户在数据库查询出有sys:user:list菜单才可以访问这个接口
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping(path = "/test")
    public ResponseResult test(){

        System.out.println(hashMap.size());
        System.out.println(Thread.currentThread());
        System.out.println(Thread.currentThread().getId());

        hashMap.put(UUID.randomUUID().toString(),new Random().nextDouble());


        return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage());
    }

    /**
     * 当用户在数据库查询出有sys:test2菜单才可以访问这个接口
     */
    @PreAuthorize("hasAuthority('sys:test2')")
    @GetMapping(path = "/test2")
    public ResponseResult test2(){

        return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage());
    }

    /**
     * 当用户在数据库查询出有sys:test3菜单才可以访问这个接口
     */
    @PreAuthorize("hasAuthority('sys:test3')")
    @GetMapping(path = "/test3")
    public ResponseResult test3(){

        return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage());
    }



}
