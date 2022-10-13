package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     *
     * @param page
     * @param size
     * @return
     */
    //1 8 = 0 8
    //2 8 = 8 8
    //3 8 = 16 8
    @GetMapping(path = "/selectAllRoleByLimit")
    public ResponseResult selectAllRoleByLimit(int page,int size){
        page=(page-1)*size;
        return new ResponseResult
                (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),roleService.selectAllRoleByLimit(page, size));
    }

    @GetMapping(path = "/selectAllRoleCount")
    public ResponseResult selectAllRoleCount(){

        return new ResponseResult
                (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),roleService.selectAllRoleCount());
    }

}
