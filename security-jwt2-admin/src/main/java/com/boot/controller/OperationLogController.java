package com.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.data.ResponseResult;
import com.boot.entity.LoginLog;
import com.boot.entity.OperationLog;
import com.boot.enums.ResponseType;
import com.boot.service.OperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志控制器
 *
 * @author youzhengjie
 * @date 2022/10/21 23:56:35
 */
@RestController
@Api("操作日志控制器")
@RequestMapping(path = "/operation/log")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @PreAuthorize("hasAuthority('sys:log:operation')")
    @com.boot.annotation.OperationLog("查询所有操作日志并分页")
    @GetMapping(path = "/selectAllOperationLogByLimit")
    @ApiOperation("查询所有操作日志并分页")
    public ResponseResult<List<OperationLog>> selectAllOperationLogByLimit(@RequestParam("page") int page,
                                                                           @RequestParam("size") int size){
        try {
            page=(page-1)*size;
            List<OperationLog> operationLogs = operationLogService.selectAllOperationLogByLimit(page, size);
            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),operationLogs);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:log:operation')")
    @com.boot.annotation.OperationLog("查询所有操作日志数量")
    @GetMapping(path = "/selectAllOperationLogCount")
    @ApiOperation("查询所有操作日志数量")
    public ResponseResult selectAllOperationLogCount(){

        try {
            long count = operationLogService.selectAllOperationLogCount();

            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('sys:log:operation:delete')")
    @com.boot.annotation.OperationLog("删除操作日志")
    @DeleteMapping(path = "/deleteOperationLog")
    @ApiOperation("删除操作日志")
    public ResponseResult deleteOperationLog(@RequestParam("id") long id){
        try {
            LambdaQueryWrapper<OperationLog> operationLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
            operationLogLambdaQueryWrapper.eq(OperationLog::getId,id);
            boolean removeResult = operationLogService.remove(operationLogLambdaQueryWrapper);
            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),removeResult);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:log:operation')")
    @com.boot.annotation.OperationLog("根据username分页查询操作日志")
    @GetMapping(path = "/searchOperationLogByUserNameAndLimit")
    @ApiOperation("根据username分页查询操作日志")
    public ResponseResult<List<OperationLog>> searchOperationLogByUserNameAndLimit(@RequestParam("username") String username,
                                                                           @RequestParam("page") int page,
                                                                           @RequestParam("size") int size){
        try {
            List<OperationLog> operationLogList =
                    operationLogService.searchOperationLogByUserNameAndLimit(username, page, size);
            return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),operationLogList);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:log:operation')")
    @GetMapping(path = "/searchOperationLogCountByUserName")
    @ApiOperation("根据username查询符合条件的操作日志数量")
    public ResponseResult searchOperationLogCountByUserName(@RequestParam("username") String username){

        try {
            long count = operationLogService.searchOperationLogCountByUserName(username);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

}
