package com.boot.dto;

import com.boot.entity.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 分配菜单dto
 *
 * @author youzhengjie
 * @date 2022/10/17 23:34:10
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
public class AssignMenuDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("该角色新的菜单列表的id数组")
    private long[] menuList;


    private long roleid;

}
