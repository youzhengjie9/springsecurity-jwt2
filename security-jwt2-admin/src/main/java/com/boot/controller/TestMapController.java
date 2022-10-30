package com.boot.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMapController {

    @GetMapping(path = "/testMap")
    public String testMap(){

        return JSON.toJSONString(TestController.hashMap);
    }

}
