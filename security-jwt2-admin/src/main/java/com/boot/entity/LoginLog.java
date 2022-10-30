package com.boot.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.converter.DelFlagConverter;
import com.boot.converter.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志
 *
 * @author youzhengjie
 * @date 2022/10/21 17:25:52
 */
@TableName(value="sys_login_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class) //解决雪花算法生成的id过长导致前端js精度丢失问题（也就是js拿到的数据和后端不一致问题）
    @ApiModelProperty(name = "id",value = "主键")
    @ExcelProperty("id")
    private Long id;

    @TableField("username")
    @ApiModelProperty("登录用户的用户名")
    @ExcelProperty("登录用户的用户名")
    private String username;

    @TableField("ip")
    @ApiModelProperty("登录用户的ip")
    @ExcelProperty("登录用户的ip")
    private String ip;

    @TableField("address")
    @ApiModelProperty("登录用户的ip所在地")
    @ExcelProperty("登录用户的ip所在地")
    private String address;

    @TableField("browser")
    @ApiModelProperty("登录用户使用的浏览器")
    @ExcelProperty("登录用户使用的浏览器")
    private String browser;

    //登录使用的操作系统
    @TableField("os")
    @ApiModelProperty("登录用户使用的操作系统")
    @ExcelProperty("登录用户使用的操作系统")
    private String os;

    //登录时间
    @TableField("login_time")
    @ApiModelProperty("登录时间")
    @ExcelProperty(value = "登录时间",converter = LocalDateTimeConverter.class)
    private LocalDateTime loginTime;

    @TableLogic//逻辑删除
    @TableField("del_flag")
    @ApiModelProperty("删除标志（0代表未删除，1代表已删除）")
    @ExcelProperty(value = "删除标志",converter = DelFlagConverter.class)
    private Integer delFlag;

}
