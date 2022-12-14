package com.boot.controller;

import com.boot.annotation.OperationLog;
import com.boot.data.ResponseResult;
import com.boot.dto.AssignRoleDTO;
import com.boot.dto.UserFormDTO;
import com.boot.entity.User;
import com.boot.entity.UserRole;
import com.boot.enums.ResponseType;
import com.boot.service.UserService;
import com.boot.utils.SnowId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 用户控制器
 *
 * @author youzhengjie
 * @date 2022/10/11 23:39:47
 */
@RestController
@RequestMapping(path = "/user")
@Api("用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 查询所有用户数量
     *
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping(path = "/selectAllUserCount")
    @ApiOperation("查询所有用户数量")
    public ResponseResult selectAllUserCount(){

        try {
            int count = userService.selectAllUserCount();
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
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
    @PreAuthorize("hasAuthority('sys:user:list')")
    @OperationLog("查询所有用户并分页")
    @GetMapping(path = "/selectAllUserByLimit")
    @ApiOperation("查询所有用户并分页")
    public ResponseResult selectAllUserByLimit(int page,int size){
        page=(page-1)*size;
        try {
            List<User> users = userService.selectAllUserByLimit(page, size);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),users);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 添加用户
     *
     * @param userFormDto 用户表单dto
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:user:list:add')")
    @OperationLog("添加用户")
    @PostMapping("/addUser")
    @ApiOperation("添加用户")
    public ResponseResult addUser(@RequestBody @Valid UserFormDTO userFormDto){

        try {
            //将密码进行加密
            String encodePassword = passwordEncoder.encode(userFormDto.getPassword());
            userFormDto.setPassword(encodePassword);
            userService.addUser(userFormDto);

            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 修改用户
     *
     * @param userFormDto 用户表单dto
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:user:list:update')")
    @OperationLog("修改用户")
    @PostMapping(path = "/updateUser")
    @ApiOperation("修改用户")
    public ResponseResult updateUser(@RequestBody @Valid UserFormDTO userFormDto){
        try {
            //如果密码不为空，则进行加密再存储到数据库中
            if(StringUtils.hasText(userFormDto.getPassword())){
                //将密码进行加密
                String encodePassword = passwordEncoder.encode(userFormDto.getPassword());
                userFormDto.setPassword(encodePassword);
            }
            userService.updateUser(userFormDto);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:user:list:delete')")
    @OperationLog("删除用户")
    @DeleteMapping(path = "/deleteUser")
    @ApiOperation("删除用户")
    public ResponseResult deleteUser(@RequestParam("id") long id){
        try {
            userService.deleteUser(id);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }

    }

    /**
     * 分配角色
     * 前端通过这个格式来传给这个接口：let jsonData={
     *         roles:this.assignRoleSelectedList,
     *         userid:this.currentAssignRoleUserId
     *       }
     * @param assignRoleDto 分配角色dto
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:user:list:assign-role')")
    @OperationLog("分配角色")
    @PostMapping(path = "/assignRole")
    @ApiOperation("分配角色")
    public ResponseResult assignRole(@RequestBody @Valid AssignRoleDTO assignRoleDto){

        try {
            if(assignRoleDto.getRoles()==null || assignRoleDto.getRoles().size()==0){
                return new ResponseResult(ResponseType.SUCCESS.getCode(),
                        ResponseType.SUCCESS.getMessage());
            }
            //通过stream流把role的id组成一个新的集合
            List<Long> roleIds = assignRoleDto
                    .getRoles()
                    .stream()
                    .map(role -> role.getId())
                    //要进行去重
                    .distinct()
                    .collect(Collectors.toList());
            long userid = Long.parseLong(assignRoleDto.getUserid());

            List<UserRole> userRoleList=new CopyOnWriteArrayList<>();
            for (Long roleId : roleIds) {
                UserRole userRole = UserRole
                        .builder()
                        //手动使用雪花算法生成分布式id
                        .id(SnowId.nextId())
                        .roleId(roleId)
                        .userId(userid)
                        .build();
                userRoleList.add(userRole);
            }
            //调用分配角色业务类
            userService.assignRoleToUser(userRoleList);

            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage());
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }

    }


    /**
     * mysql通过userName关键字搜索
     *
     * @param userName 用户名
     * @param page     页面
     * @param size     大小
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @OperationLog("根据用户名搜索用户并分页")
    @GetMapping(path = "/searchUserByUserNameAndLimit")
    @ApiOperation("根据用户名搜索用户并分页")
    public ResponseResult searchUserByUserNameAndLimit(@RequestParam("userName") String userName,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size){
        page=(page-1)*size;

        try {
            List<User> users = userService.searchUserByUserNameAndLimit(userName, page, size);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),users);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

    /**
     * 按用户名搜索用户数量
     *
     * @param userName 用户名
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping(path = "/searchUserCountByUserName")
    @ApiOperation("按用户名搜索用户数量")
    public ResponseResult searchUserCountByUserName(@RequestParam("userName") String userName){

        try {
            int count = userService.searchUserCountByUserName(userName);
            return new ResponseResult(ResponseType.SUCCESS.getCode(),
                    ResponseType.SUCCESS.getMessage(),count);
        }catch (Exception e){
            return new ResponseResult(ResponseType.ERROR.getCode(),
                    ResponseType.ERROR.getMessage());
        }
    }

}
