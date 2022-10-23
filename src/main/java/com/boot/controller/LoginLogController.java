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
     * 下面这段增强版分页sql会出现bug，如果使用到下面这段sql，那就不能在最下面加上ORDER BY，下面的ORDER BY login_time DESC会引起bug
     * SELECT id, username, ip, address, browser, os, login_time
     * FROM sys_login_log
     * WHERE id >= (SELECT id FROM sys_login_log ORDER BY id LIMIT 0,1)
     *   AND del_flag = 0
     * ORDER BY login_time DESC
     * LIMIT 7
     *
     * TODO :暂时先用下面这种性能低的
     *
     * SELECT id, username, ip, address, browser, os, login_time
     * FROM sys_login_log
     * WHERE del_flag = 0
     * ORDER BY login_time DESC
     * LIMIT 0,7
     *
     * @param page
     * @param size
     * @return
     */

    @OperationLog("查询所有登录日志并分页")
    @GetMapping(path = "/selectAllLoginLogByLimit")
    @ApiOperation("查询所有登录日志并分页")
    public ResponseResult<List<LoginLog>> selectAllLoginLogByLimit(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size){
        try {
            //分页
            page=(page-1)*size;

            // TODO: 2022/10/22 selectAllLoginLogByLimit方法后期要进行优化，这个SQL性能比较低
            List<LoginLog> loginLogList = loginLogService.selectAllLoginLogByLimit(page, size);

            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),loginLogList);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

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



}
