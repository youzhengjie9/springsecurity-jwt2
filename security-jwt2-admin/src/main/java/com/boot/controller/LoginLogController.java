package com.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.annotation.OperationLog;
import com.boot.data.ResponseResult;
import com.boot.entity.LoginLog;
import com.boot.enums.ResponseType;
import com.boot.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * 查询所有登录日志并分页
     * @param page
     * @param size
     * @return
     */

    @PreAuthorize("hasAuthority('sys:log:login')")
    @OperationLog("查询所有登录日志并分页")
    @GetMapping(path = "/selectAllLoginLogByLimit")
    @ApiOperation("查询所有登录日志并分页")
    public ResponseResult<List<LoginLog>> selectAllLoginLogByLimit(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size){
        try {
            //分页
            page=(page-1)*size;

            List<LoginLog> loginLogList = loginLogService.selectAllLoginLogByLimit(page, size);

            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),loginLogList);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:log:login')")
    @OperationLog("查询所有登录日志数量")
    @GetMapping(path = "/selectAllLoginLogCount")
    @ApiOperation("查询所有登录日志数量")
    public ResponseResult selectAllLoginLogCount(){

        try {
            long count = loginLogService.count();
            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('sys:log:login:delete')")
    @OperationLog("删除登录日志")
    @DeleteMapping(path = "/deleteLoginLog")
    @ApiOperation("删除登录日志")
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

    /**
     * 根据用户名分页搜索登录日志
     *
     * @param username 用户名
     * @param page     页面
     * @param size     大小
     * @return {@link List}<{@link LoginLog}>
     */
    @PreAuthorize("hasAuthority('sys:log:login')")
    @OperationLog("根据用户名分页搜索登录日志")
    @GetMapping(path = "/searchLoginLogByUserNameAndLimit")
    @ApiOperation("根据用户名分页搜索登录日志")
    public ResponseResult<List<LoginLog>> searchLoginLogByUserNameAndLimit(@RequestParam("username") String username,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size){
        try {
            List<LoginLog> loginLogList = loginLogService.searchLoginLogByUserNameAndLimit(username, page, size);
            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),loginLogList);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 根据用户名查询符合条件的数量
     *
     * @param username 用户名
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:log:login')")
    @GetMapping(path = "/searchLoginLogCountByUserName")
    @ApiOperation("根据用户名查询符合条件的数量")
    public ResponseResult searchLoginLogCountByUserName(@RequestParam("username") String username){

        try {
            int count = loginLogService.searchLoginLogCountByUserName(username);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }



}
