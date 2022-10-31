package com.boot.controller;

import com.boot.annotation.OperationLog;
import com.boot.data.ResponseResult;
import com.boot.dto.AssignMenuDTO;
import com.boot.dto.RoleFormDTO;
import com.boot.entity.Role;
import com.boot.entity.RoleMenu;
import com.boot.enums.ResponseType;
import com.boot.service.RoleService;
import com.boot.utils.SnowId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@Api("角色接口")
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
    @PreAuthorize("hasAuthority('sys:role:list')")
    @OperationLog("查询所有角色并分页")
    @GetMapping(path = "/selectAllRoleByLimit")
    @ApiOperation("查询所有角色并分页")
    public ResponseResult selectAllRoleByLimit(int page,int size){
        page=(page-1)*size;
        try {
            List<Role> roles = roleService.selectAllRoleByLimit(page, size);
            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),roles);
        }catch (Exception e){
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @OperationLog("查询所有角色数量")
    @GetMapping(path = "/selectAllRoleCount")
    @ApiOperation("查询所有角色数量")
    public ResponseResult selectAllRoleCount(){

        try {
            int count = roleService.selectAllRoleCount();
            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @OperationLog("查询所有角色")
    @GetMapping(path = "/selectAllRole")
    @ApiOperation("查询所有角色")
    public ResponseResult selectAllRole(){
        try {
            List<Role> roles = roleService.selectAllRole();
            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),roles);
        }catch (Exception e){
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @OperationLog("根据用户id查询用户已经选择的角色")
    @GetMapping(path = "/selectUserCheckedRoleByUserId")
    @ApiOperation("根据用户id查询用户已经选择的角色")
    public ResponseResult selectUserCheckedRoleByUserId(@RequestParam("id") String id){

        try {
            long userid = Long.parseLong(id);
            List<Role> roles = roleService.selectUserCheckedRoleByUserId(userid);
            return new ResponseResult
                    (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),roles);
        }catch (Exception e){
            return new ResponseResult
                    (ResponseType.ERROR.getCode(), ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:role:list:add')")
    @OperationLog("添加角色")
    @PostMapping("/addRole")
    @ApiOperation("添加角色")
    public ResponseResult addRole(@RequestBody @Valid RoleFormDTO roleFormDto){
        try {
            roleService.addRole(roleFormDto);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('sys:role:list:update')")
    @OperationLog("修改角色")
    @PostMapping(path = "/updateRole")
    @ApiOperation("修改角色")
    public ResponseResult updateRole(@RequestBody @Valid RoleFormDTO roleFormDto){

        try {
            roleService.updateRole(roleFormDto);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 删除角色
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:role:list:delete')")
    @OperationLog("删除角色")
    @DeleteMapping(path = "/deleteRole")
    @ApiOperation("删除角色")
    public ResponseResult deleteRole(@RequestParam("id") long id){
        ResponseResult<Object> result = new ResponseResult<>();
        try {
            roleService.deleteRole(id);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }

    }

    /**
     * 分配菜单
     *
     * @param assignMenuDto 分配菜单dto
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:role:list:assign-menu')")
    @OperationLog("分配菜单")
    @PostMapping(path = "/assignMenu")
    @ApiOperation("分配菜单")
    public ResponseResult assignMenu(@RequestBody @Valid AssignMenuDTO assignMenuDto){

        try {
            if(assignMenuDto.getMenuList()==null || assignMenuDto.getMenuList().length==0){
                return new ResponseResult(ResponseType.SUCCESS.getCode(),
                        ResponseType.SUCCESS.getMessage());
            }

            List<RoleMenu> roleMenuList=new CopyOnWriteArrayList<>();

            for (long menuId : assignMenuDto.getMenuList()) {
                RoleMenu roleMenu = RoleMenu
                        .builder()
                        //手动使用雪花算法生成分布式id
                        .id(SnowId.nextId())
                        .roleId(assignMenuDto.getRoleid())
                        .menuId(menuId)
                        .build();
                roleMenuList.add(roleMenu);
            }

            //调用分配角色业务类
            roleService.assignMenuToRole(roleMenuList);

            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }

    }


    /**
     * mysql通过role的name关键字搜索
     *
     * @param roleName 角色名
     * @param page     页面
     * @param size     大小
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:role:list')")
    @OperationLog("根据角色名搜索角色并分页")
    @GetMapping(path = "/searchRoleByRoleNameAndLimit")
    @ApiOperation("根据角色名搜索角色并分页")
    public ResponseResult searchRoleByRoleNameAndLimit(@RequestParam("roleName") String roleName,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size){
        page=(page-1)*size;

        try {
            List<Role> roles = roleService.searchRoleByRoleNameAndLimit(roleName, page, size);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),roles);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 按role的name搜索role数量
     *
     * @param roleName 角色名
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:role:list')")
    @OperationLog("按role的name搜索role数量")
    @GetMapping(path = "/searchRoleCountByRoleName")
    @ApiOperation("按role的name搜索role数量")
    public ResponseResult searchRoleCountByRoleName(@RequestParam("roleName") String roleName){

        try {
            int count = roleService.searchRoleCountByRoleName(roleName);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

}
