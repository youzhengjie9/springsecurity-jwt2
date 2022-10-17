package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.AssignMenuDto;
import com.boot.dto.AssignRoleDto;
import com.boot.dto.RoleFormDto;
import com.boot.dto.UserFormDto;
import com.boot.entity.Role;
import com.boot.entity.UserRole;
import com.boot.enums.ResponseType;
import com.boot.service.RoleService;
import com.boot.utils.SnowId;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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

    /**
     * 分配菜单
     *
     * @param assignMenuDto 分配菜单dto
     * @return {@link ResponseResult}
     */
    @PostMapping(path = "/assignMenu")
    public ResponseResult assignMenu(@RequestBody @Valid AssignMenuDto assignMenuDto){

        System.out.println(assignMenuDto.getMenus());
        System.out.println(assignMenuDto.getRoleid());
        return null;
//        try {
//            if(assignMenuDto.getMenus()==null || assignMenuDto.getMenus().size()==0){
//                return new ResponseResult(ResponseType.SUCCESS.getCode(),
//                        ResponseType.SUCCESS.getMessage());
//            }
//            //通过stream流把role的id组成一个新的集合
//            List<Long> roleIds = assignRoleDto
//                    .getRoles()
//                    .stream()
//                    .map(role -> role.getId())
//                    //要进行去重
//                    .distinct()
//                    .collect(Collectors.toList());
//            long userid = Long.parseLong(assignRoleDto.getUserid());
//
//            List<UserRole> userRoleList=new CopyOnWriteArrayList<>();
//            for (Long roleId : roleIds) {
//                UserRole userRole = UserRole
//                        .builder()
//                        //手动使用雪花算法生成分布式id
//                        .id(SnowId.nextId())
//                        .roleId(roleId)
//                        .userId(userid)
//                        .build();
//                userRoleList.add(userRole);
//            }
//            //调用分配角色业务类
//            userService.assignRoleToUser(userRoleList);
//
//            return new ResponseResult(ResponseType.SUCCESS.getCode(),
//                    ResponseType.SUCCESS.getMessage());
//        }catch (Exception e){
//            return new ResponseResult(ResponseType.ERROR.getCode(),
//                    ResponseType.ERROR.getMessage());
//        }

    }

}
