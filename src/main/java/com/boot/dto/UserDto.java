package com.boot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UserDto 用于前端传给controller或者是controller传给service用的
 * @author youzhengjie 2022-09-29 16:59:47
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName",value = "用户名",example = "root")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(name = "password",value = "密码",example = "123456")
    private String password;



}
