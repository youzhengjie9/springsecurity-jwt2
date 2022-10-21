package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.AssignMenuDTO;
import com.boot.dto.RoleFormDTO;
import com.boot.entity.Role;
import com.boot.entity.RoleMenu;
import com.boot.enums.ResponseType;
import com.boot.service.RoleService;
import com.boot.utils.SnowId;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping(path = "/selectAllRoleByLimit")
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

    @GetMapping(path = "/selectAllRoleCount")
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

    @GetMapping(path = "/selectAllRole")
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

    @GetMapping(path = "/selectUserCheckedRoleByUserId")
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

    @PostMapping("/addRole")
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

    @PostMapping(path = "/updateRole")
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
    @DeleteMapping(path = "/deleteRole")
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
    @PostMapping(path = "/assignMenu")
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
    @GetMapping(path = "/searchRoleByRoleNameAndLimit")
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
    @GetMapping(path = "/searchRoleCountByRoleName")
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
