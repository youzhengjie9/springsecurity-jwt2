package com.boot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 前端用户表单传给后端接口接收
 * @author youzhengjie
 * @date 2022/10/13 10:08:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserFormDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "userName",value = "用户名",example = "root")
    private String userName;

    @ApiModelProperty(name = "nickName",value = "昵称",example = "我的昵称")
    private String nickName;

    @ApiModelProperty(name = "password",value = "密码",example = "123456")
    private String password;

    @ApiModelProperty(name = "status",value = "账号状态（0正常 1停用）",example = "0")
    private int status;

    @ApiModelProperty(name = "email",value = "用户邮箱",example = "1550324080@qq.com")
    private String email;

    @ApiModelProperty(name = "phone",value = "用户手机号",example = "18420161234")
    private String phone;

    @ApiModelProperty(name = "sex",value = "用户性别（0男，1女，2未知）",example = "0")
    private String sex;

    @ApiModelProperty(name = "status",value = "头像地址",example = "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF")
    private String avatar;

}
