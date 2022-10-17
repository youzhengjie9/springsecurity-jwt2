package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.dto.AssignRoleDto;
import com.boot.dto.UserFormDto;
import com.boot.entity.Role;
import com.boot.entity.User;
import com.boot.entity.UserRole;
import com.boot.enums.ResponseType;
import com.boot.service.UserService;
import com.boot.utils.SnowId;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户控制器
 *
 * @author youzhengjie
 * @date 2022/10/11 23:39:47
 */
@RestController
@RequestMapping(path = "/user")
@Api("用户控制器")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 选择所有用户数量
     *
     * @return {@link ResponseResult}
     */
    @GetMapping(path = "/selectAllUserCount")
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
    @GetMapping(path = "/selectAllUserByLimit")
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
    @PostMapping("/addUser")
    public ResponseResult addUser(@RequestBody @Valid UserFormDto userFormDto){

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
     * 更新用户
     *
     * @param userFormDto 用户表单dto
     * @return {@link ResponseResult}
     */
    @PostMapping(path = "/updateUser")
    public ResponseResult updateUser(@RequestBody @Valid UserFormDto userFormDto){
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
    @DeleteMapping(path = "/deleteUser")
    public ResponseResult deleteUser(@RequestParam("id") long id){
        ResponseResult<Object> result = new ResponseResult<>();
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
    @PostMapping(path = "/assignRole")
    public ResponseResult assignRole(@RequestBody @Valid AssignRoleDto assignRoleDto){

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


    @GetMapping(path = "/searchUserByUserNameAndLimit")
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

    @GetMapping(path = "/searchUserCountByUserName")
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
