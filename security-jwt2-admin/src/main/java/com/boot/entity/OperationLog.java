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
 * 操作日志
 *
 * @author youzhengjie
 * @date 2022/10/21 17:40:43
 */
@TableName(value="sys_oper_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class) //解决雪花算法生成的id过长导致前端js精度丢失问题（也就是js拿到的数据和后端不一致问题）
    @ApiModelProperty(name = "id",value = "主键")
    @ExcelProperty("id")
    private Long id;

    @TableField("username")
    @ApiModelProperty("执行操作的用户名")
    @ExcelProperty("执行操作的用户名")
    private String username;

    @TableField("type")
    @ApiModelProperty("操作类型")
    @ExcelProperty("操作类型")
    private String type;

    @TableField("uri")
    @ApiModelProperty("访问的接口uri")
    @ExcelProperty("访问的接口uri")
    private String uri;

    @TableField("time")
    @ApiModelProperty("访问接口耗时")
    @ExcelProperty("访问接口耗时")
    private String time;

    @TableField("ip")
    @ApiModelProperty("执行操作的用户的ip")
    @ExcelProperty("执行操作的用户的ip")
    private String ip;

    @TableField("address")
    @ApiModelProperty("执行操作的用户的ip对应的地址")
    @ExcelProperty("执行操作的用户的ip对应的地址")
    private String address;

    @TableField("browser")
    @ApiModelProperty("执行操作的用户所使用的浏览器")
    @ExcelProperty("执行操作的用户所使用的浏览器")
    private String browser;

    @TableField("os")
    @ApiModelProperty("执行操作的用户所使用的操作系统")
    @ExcelProperty("执行操作的用户所使用的操作系统")
    private String os;

    @TableField("oper_time")
    @ApiModelProperty("操作时间")
    @ExcelProperty(value = "操作时间",converter = LocalDateTimeConverter.class)
    private LocalDateTime operTime;

    @TableLogic//逻辑删除
    @TableField("del_flag")
    @ApiModelProperty("删除标志（0代表未删除，1代表已删除）")
    @ExcelProperty(value = "删除标志",converter = DelFlagConverter.class)
    private Integer delFlag;

}
