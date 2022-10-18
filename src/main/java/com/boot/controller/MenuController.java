package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.MenuDto;
import com.boot.dto.UserFormDto;
import com.boot.entity.Menu;
import com.boot.enums.ResponseType;
import com.boot.service.MenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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


    /**
     * 添加菜单
     *
     * @param menuDto 菜单dto
     * @return {@link ResponseResult}
     */
    @PostMapping("/addMenu")
    public ResponseResult addMenu(@RequestBody @Valid MenuDto menuDto){

        try {
            System.out.println(menuDto.getMenu());
            System.out.println(menuDto.getParentMenuName());

            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 更新菜单
     *
     * @param menuDto 菜单dto
     * @return {@link ResponseResult}
     */
    @PostMapping("/updateMenu")
    public ResponseResult updateMenu(@RequestBody @Valid MenuDto menuDto){

        try {
            System.out.println(menuDto.getMenu());
            System.out.println(menuDto.getParentMenuName());

            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 通过菜单id查询菜单名称
     *
     * @param menuid menuid
     * @return {@link String}
     */
    @GetMapping(path = "/selectMenuNameByMenuId")
    public ResponseResult selectMenuNameByMenuId(@RequestParam("menuid") long menuid){
        try {
            //说明是顶层菜单
            String menuName=null;
            if(menuid==0){
                menuName="顶层菜单";
            }else {
                menuName = menuService.selectMenuNameByMenuId(menuid);
            }
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),menuName);
        } catch (Exception e) {
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }


}
