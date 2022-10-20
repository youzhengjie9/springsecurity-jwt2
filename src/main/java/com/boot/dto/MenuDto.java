package com.boot.dto;

import com.boot.entity.Menu;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 菜单dto
 *
 * @author youzhengjie
 * @date 2022/10/18 16:10:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class MenuDto implements Serializable {

    private Menu menu;

    //所属菜单id（也就是父菜单id）
    private Long parentId;

    //新增/修改菜单的类型（0目录、1菜单、2按钮）
    private int menuType;

}
