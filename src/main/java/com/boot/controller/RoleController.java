package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.RoleFormDto;
import com.boot.dto.UserFormDto;
import com.boot.entity.Role;
import com.boot.enums.ResponseType;
import com.boot.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseResult addRole(@RequestBody @Valid RoleFormDto roleFormDto){
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
    public ResponseResult updateRole(@RequestBody @Valid RoleFormDto roleFormDto){

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
}
