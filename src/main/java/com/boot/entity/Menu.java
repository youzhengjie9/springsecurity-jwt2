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
import org.jetbrains.annotations.NotNull;

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
public class Menu implements Serializable,Comparable<Menu> {

    private static final long serialVersionUID = 1L;


    @TableId("id")
    @ApiModelProperty("主键id,mp会自动为@TableId(\"id\")属性生成id（默认是雪花算法生成的分布式id）")
    private Long id;


    @TableField("parent_id")
    @ApiModelProperty("后台侧边栏。父菜单ID,一级菜单为0")
    private Long parentId;


    @TableField("menu_name")
    @ApiModelProperty("菜单/权限名称")
    private String menuName;


    @TableField("path")
    @ApiModelProperty("vue路由地址（type=1才会生效，type=2不生效）")
    private String path;


    @TableField("component")
    @ApiModelProperty("动态路由要用到。views目录下的组件页面名,自动会补上前缀../views（type=1才会生效，type=2不生效）")
    private String component;

    @TableField("status")
    @ApiModelProperty("菜单状态（0正常 1停用）")
    private int status;

    @TableField("visible")
    @ApiModelProperty("菜单显示状态（0显示 1隐藏）（type=0或者1才会生效，type=2不生效）")
    private int visible;


    @TableField("perms")
    @ApiModelProperty("前后端的权限标识，比如sys:user:list")
    private String perms;

    @TableField("type")
    @ApiModelProperty("菜单类型。0：目录（点击后台侧边栏可以展开成下一级菜单的按钮）;1：菜单（点击后台侧边栏直接跳转vue路由组件的按钮）;2：普通后台服务器接口;（与后台侧边栏无关，仅仅是普通接口权限）")
    private int type;

    @TableField("icon")
    @ApiModelProperty("菜单图标（type=0或者1才会生效，type=2不生效）")
    private String icon;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField("update_time")
    @ApiModelProperty("最后一次修改时间")
    private String updateTime;


    @TableLogic//逻辑删除
    @TableField("del_flag")
    @ApiModelProperty("删除标志（0代表未删除，1代表已删除）")
    private Integer delFlag;


    @TableField("sort")
    @ApiModelProperty("前端菜单排序，默认值为1，1的优先级最高，排在最上面")
    private int sort;


    @TableField("remark")
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("当前菜单的下级菜单")
    private List<Menu> children;

    //实现排序接口
    @Override
    public int compareTo(@NotNull Menu o) {
        //升序
        return this.sort - o.getSort();
    }
}