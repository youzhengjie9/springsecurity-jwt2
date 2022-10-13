package com.boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 角色
 * @author youzhengjie
 * @date 2022/10/13 12:04:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
@EqualsAndHashCode
@Builder
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * mp会自动为@TableId("id")属性生成id（默认是雪花算法生成的分布式id）。
     */
    @TableId("id")
    @ApiModelProperty(name = "id",value = "主键")
    private Long id;

    @TableField("name")
    @ApiModelProperty("角色名称，比如管理员")
    private String name;

    @TableField("role_key")
    @ApiModelProperty("角色关键字，比如admin")
    private String roleKey;

    @TableField("status")
    @ApiModelProperty("角色状态（0正常 1停用）")
    private int status;

    @TableLogic
    @TableField("del_flag")
    @ApiModelProperty("删除标志（0代表未删除，1代表已删除）")
    private int delFlag;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @ApiModelProperty("最后一次更新时间")
    private LocalDateTime updateTime;

    @TableField("remark")
    @ApiModelProperty("备注")
    private String remark;

}
