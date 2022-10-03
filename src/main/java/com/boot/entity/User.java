package com.boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户表(User)实体类
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
@EqualsAndHashCode
@Builder
public class User implements Serializable {

    /**
    * 主键
    * mp会自动为@TableId("id")属性生成id（默认是雪花算法生成的分布式id）。
    */
    @TableId("id")
    private Long id;
    /**
    * 用户名
    */
    @TableField("user_name")
    @ApiModelProperty(name = "userName",value = "用户名",example = "root")
    private String userName;
    /**
     * 昵称
     */
    @TableField("nick_name")
    @ApiModelProperty(name = "nickName",value = "昵称",example = "我的昵称")
    private String nickName;
    /**
    * 密码
    */
    @TableField("password")
    @ApiModelProperty(name = "password",value = "密码",example = "123456")
    private String password;
    /**
    * 账号状态（0正常 1停用）
    */
    @TableField("status")
    @ApiModelProperty(name = "status",value = "账号状态（0正常 1停用）",example = "0")
    private int status;
    /**
    * 邮箱
    */
    @TableField("email")
    @ApiModelProperty(name = "email",value = "用户邮箱",example = "1550324080@qq.com")
    private String email;
    /**
    * 手机号
    */
    @TableField("phone")
    @ApiModelProperty(name = "phone",value = "用户手机号",example = "18420161234")
    private String phone;
    /**
    * 用户性别（0男，1女，2未知）
    */
    @TableField("sex")
    @ApiModelProperty(name = "sex",value = "用户性别（0男，1女，2未知）",example = "0")
    private String sex;
    /**
    * 头像地址
    */
    @TableField("avatar")
    @ApiModelProperty(name = "status",value = "头像地址",example = "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF")
    private String avatar;
    /**
    * 创建时间
    */
    @TableField("create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间",example = "2022-01-10")
    private Date createTime;
    /**
    * 更新时间
    */
    @TableField("update_time")
    @ApiModelProperty(name = "updateTime",value = "更新时间",example = "2022-05-20")
    private Date updateTime;
    /**
    * 删除标志（0代表未删除，1代表已删除）
    */
    @TableLogic //逻辑删除（0代表未删除，1代表已删除）
    @ApiModelProperty(name = "delFlag",value = "删除标志（0代表未删除，1代表已删除）",example = "0")
    @TableField("del_flag")
    private Integer delFlag;
}