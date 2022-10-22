package com.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.data.ResponseResult;
import com.boot.entity.LoginLog;
import com.boot.enums.ResponseType;
import com.boot.service.LoginLogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志控制器
 *
 * @author youzhengjie
 * @date 2022/10/21 23:19:59
 */
@RestController
@Api("登录日志控制器")
@RequestMapping(path = "/login/log")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

//    @GetMapping(path = "/selectAllLoginLogByLimit")
//    public ResponseResult<List<LoginLog>> selectAllLoginLogByLimit(@RequestParam("page") int page,
//                                                                   @RequestParam("size") int size){
//        try {
//            page=(page-1)*size;
//
//            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),loginLogList);
//        }catch (Exception e){
//            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
//        }
//    }

    @DeleteMapping(path = "/deleteLoginLog")
    public ResponseResult deleteLoginLog(@RequestParam("id") long id){
        try {
            LambdaQueryWrapper<LoginLog> loginLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
            loginLogLambdaQueryWrapper.eq(LoginLog::getId,id);
            boolean removeResult = loginLogService.remove(loginLogLambdaQueryWrapper);
            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),removeResult);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }



}
