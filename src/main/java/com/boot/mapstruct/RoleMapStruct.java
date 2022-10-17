package com.boot.mapstruct;

import com.boot.dto.RoleFormDto;
import com.boot.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 拷贝Role
 *
 * @author youzhengjie
 * @date 2022/10/17 12:21:54
 */
//---加上componentModel = "spring"之后，SpringBoot将自动整合MapStruct，我们可以通过@AutoWired或者@Resource注入这个接口。
@Mapper(componentModel = "spring")
public interface RoleMapStruct {


    /**
     * roleFormDto拷贝到Role
     *
     * @param roleFormDto dto作用形式
     * @return {@link Role}
     */
    @Mappings({
            @Mapping(source = "roleFormDto.id",target = "id"),
            @Mapping(source = "roleFormDto.name",target = "name"),
            @Mapping(source = "roleFormDto.roleKey",target = "roleKey"),
            //排除status映射，否则默认是会映射上去的，即使不写@Mapping
            @Mapping(ignore = true,target = "status"),
            @Mapping(source = "roleFormDto.remark",target = "remark")
    })
    Role roleFormDtoToRole(RoleFormDto roleFormDto);


}
