package com.boot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RoleFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id",value = "主键")
    private Long id;

    @ApiModelProperty("角色名称，比如管理员")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty("角色关键字，比如admin")
    @NotBlank(message = "角色关键字不能为空")
    private String roleKey;

    @ApiModelProperty(name = "status",value = "角色状态（true正常 false停用）",example = "true")
    private boolean status;

    @ApiModelProperty("备注")
    @Length(max = 300,message = "备注的字数不能大于300位")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
