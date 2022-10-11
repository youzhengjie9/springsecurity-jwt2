package com.boot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UserLoginDto 用于前端登录表单传给controller用的
 *
 * @author youzhengjie
 * @date 2022/10/11 10:01:07
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @ApiModelProperty(name = "username",value = "用户名",example = "root")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(name = "password",value = "密码",example = "123456")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(name = "code",value = "验证码")
    private String code;

    /**
     * 存储在redis中的正确的验证码的key，通过这个key能找到正确的验证码
     */
    @ApiModelProperty(name = "codeKey",value = "存储在redis中的正确的验证码的key，通过这个key能找到正确的验证码")
    private String codeKey;


}
