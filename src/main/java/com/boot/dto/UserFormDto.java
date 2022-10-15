package com.boot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 前端用户表单传给后端接口接收
 * @author youzhengjie
 * @date 2022/10/13 10:08:30
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserFormDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id",value = "主键")
    private Long id;

    @Length(min = 3,max = 15,message = "帐号长度要在3-15位之间")
    @ApiModelProperty(name = "userName",value = "用户名",example = "root")
    private String userName;

    @Length(min = 3,max = 15,message = "昵称长度要在3-15位之间")
    @ApiModelProperty(name = "nickName",value = "昵称",example = "我的昵称")
    private String nickName;

    //这里密码不要进行校验，因为编辑用户如果不输入密码则默认是不修改密码，所以这里可以为null
    @ApiModelProperty(name = "password",value = "密码",example = "123456")
    private String password;

    @ApiModelProperty(name = "status",value = "账号状态（true正常 false停用）",example = "true")
    private boolean status;

    @Email(message = "邮箱格式不合理")
    @ApiModelProperty(name = "email",value = "用户邮箱",example = "1550324080@qq.com")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(name = "phone",value = "用户手机号",example = "18420161234")
    private String phone;

    @NotBlank
    @ApiModelProperty(name = "sex",value = "用户性别（0男，1女，2未知）",example = "0")
    private String sex;

//    @NotBlank(message = "头像不能为空")
    @ApiModelProperty(name = "status",value = "头像地址",example = "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF")
    private String avatar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
