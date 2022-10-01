package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author youzhengjie 2022-09-30 00:41:03
 */
@RestController
@Api("测试接口")
public class TestController {

    @GetMapping(path = "/test")
    public ResponseResult test(){

        return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage());
    }




}
