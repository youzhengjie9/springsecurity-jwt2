package com.boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单（权限）表实体类
 * @author youzhengjie 2022-10-05 15:40:55
 */
@TableName(value="sys_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * mp会自动为@TableId("id")属性生成id（默认是雪花算法生成的分布式id）。
     */
    @TableId("id")
    private Long id;

    /**
     * 父节点id
     */
    @TableField("parent_id")
    private Long parentId;
    /**
    * 菜单名
    */
    @TableField("menu_name")
    private String menuName;
    /**
    * 路由地址
    */
    @TableField("path")
    private String path;
    /**
    * 组件路径
    */
    @TableField("component")
    private String component;
    /**
    * 菜单状态（0显示 1隐藏）
    */
    @TableField("visible")
    private String visible;
    /**
    * 权限标识
    */
    @TableField("perms")
    private String perms;
    /**
    * 菜单图标
    */
    @TableField("icon")
    private String icon;

    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;
    /**
    * 是否删除（0未删除 1已删除）
    */
    @TableLogic//逻辑删除
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 菜单状态（0正常 1停用）
     */
    @TableField("status")
    private String status;
    /**
    * 备注
    */
    @TableField("remark")
    private String remark;

    @ApiModelProperty("当前菜单的下级菜单")
    private List<Menu> children;
}