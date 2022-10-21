package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.vo.ServerInfo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控控制器
 *
 * @author youzhengjie
 * @date 2022/10/21 15:50:17
 */
@RestController
@Api("服务器监控控制器")
@RequestMapping(path = "/server/monitor")
public class ServerMonitorController {


    /**
     * 获取该服务器所有信息
     *
     * @return {@link ResponseResult}<{@link ServerInfo}>
     */
    @GetMapping(path = "/getServerInfo")
    public ResponseResult<ServerInfo> getServerInfo(){

        try {
            ServerInfo serverInfo = ServerInfo.init();
            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),serverInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }


    }



}
