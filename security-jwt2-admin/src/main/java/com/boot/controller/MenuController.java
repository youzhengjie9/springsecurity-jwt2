package com.boot.controller;

import com.boot.annotation.OperationLog;
import com.boot.data.ResponseResult;
import com.boot.dto.MenuDTO;
import com.boot.entity.Menu;
import com.boot.enums.ResponseType;
import com.boot.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('sys:menu:list')")
    @OperationLog("根据角色id去查询已选择的角色菜单")
    @GetMapping(path = "/selectRoleCheckedMenuByRoleId")
    @ApiOperation("根据角色id去查询已选择的角色菜单")
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
    @PreAuthorize("hasAuthority('sys:menu:list:add')")
    @OperationLog("添加菜单")
    @PostMapping("/addMenu")
    @ApiOperation("添加菜单")
    public ResponseResult addMenu(@RequestBody @Valid MenuDTO menuDto){

        try {
            menuService.addMenu(menuDto);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 修改菜单
     *
     * @param menuDto 菜单dto
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:menu:list:update')")
    @OperationLog("修改菜单")
    @PostMapping("/updateMenu")
    @ApiOperation("修改菜单")
    public ResponseResult updateMenu(@RequestBody @Valid MenuDTO menuDto){

        try {
            menuService.updateMenu(menuDto);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 删除菜单
     *
     * @param menuid menuid
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:menu:list:delete')")
    @OperationLog("删除菜单")
    @DeleteMapping(path = "/deleteMenu")
    @ApiOperation("删除菜单")
    public ResponseResult deleteMenu(@RequestParam("menuid") long menuid){
        try {
            menuService.deleteMenu(menuid);
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
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @OperationLog("通过菜单id查询菜单名称")
    @GetMapping(path = "/selectMenuNameByMenuId")
    @ApiOperation("通过菜单id查询菜单名称")
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
