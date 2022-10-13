package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.UserFormDto;
import com.boot.enums.ResponseType;
import com.boot.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author youzhengjie
 * @date 2022/10/11 23:39:47
 */
@RestController
@RequestMapping(path = "/user")
@Api("用户控制器")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/selectAllUserCount")
    public ResponseResult selectAllUserCount(){

        return new ResponseResult(ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),userService.selectAllUserCount());
    }

    /**
     *
     * @param page
     * @param size
     * @return
     */
    //1 8 = 0 8
    //2 8 = 8 8
    //3 8 = 16 8
    @GetMapping(path = "/selectAllUserByLimit")
    public ResponseResult selectAllUserByLimit(int page,int size){
        page=(page-1)*size;
        return new ResponseResult(ResponseType.SUCCESS.getCode(),
                ResponseType.SUCCESS.getMessage(),userService.selectAllUserByLimit(page, size));
    }

    @PostMapping("/addUser")
    public ResponseResult addUser(@RequestBody UserFormDto userFormDto){

        try {
            userService.addUser(userFormDto);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

}
