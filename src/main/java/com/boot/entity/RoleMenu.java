package com.boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
@EqualsAndHashCode
@Builder
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(name = "id",value = "主键")
    private Long id;

    @TableField("role_id")
    @ApiModelProperty("角色id")
    private Long roleId;

    @TableField("menu_id")
    @ApiModelProperty("菜单id")
    private Long menuId;


}
