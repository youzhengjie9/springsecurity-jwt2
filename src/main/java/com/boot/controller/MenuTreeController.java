package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.boot.service.MenuTreeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单树控制器
 *
 * @author youzhengjie
 * @date 2022/10/17 21:10:14
 */
@RestController
@RequestMapping(path = "/menutree")
@Api("菜单树接口")
public class MenuTreeController {

    @Autowired
    private MenuTreeService menuTreeService;

    /**
     * 根据用户的userid来构建前端的后台管理系统侧边栏菜单
     * @return 菜单的json串
     */
    @GetMapping(path = "/buildTreeByUserId")
    public ResponseResult<String> buildTreeByUserId(@RequestParam("userid") String userid){
        try {
            String menuTree = menuTreeService.buildTreeByUserId(Long.parseLong(userid));
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),menuTree);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }

    }

    /**
     * 将系统所有菜单权限构建成一棵树
     * @return
     */
    @GetMapping(path = "/buildAllMenuPermissionTree")
    public ResponseResult<String> buildAllMenuPermissionTree(){
        try {
            String menuTree = menuTreeService.buildAllMenuPermissionTree();
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),menuTree);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }

    }

    /**
     * 构建分配菜单的树（和上面buildAllMenuPermissionTree方法区别仅仅是这个方法只展示部分需要的字段、而buildAllMenuPermissionTree方法展示所有字段）
     *
     * @return {@link String}
     */
    @GetMapping(path = "/buildAssignMenuTree")
    public ResponseResult<String> buildAssignMenuTree(){
        try {
            String menuTree = menuTreeService.buildAssignMenuTree();
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),menuTree);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }

    }


}
