package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.entity.Menu;
import com.boot.enums.ResponseType;
import com.boot.service.MenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单控制器
 *
 * @author youzhengjie
 * @date 2022/10/13 23:10:31
 */
@RestController
@RequestMapping(path = "/menu")
@Api("菜单接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping(path = "/selectRoleCheckedMenuByRoleId")
    public ResponseResult selectRoleCheckedMenuByRoleId(@RequestParam("id") String id){

        try {
            long roleid = Long.parseLong(id);
            List<Menu> menus = menuService.selectRoleCheckedMenuByRoleId(roleid);

            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),menus);
        }catch (Exception e){
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }


}
