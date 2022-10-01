package com.boot.mapstruct;

import com.boot.dto.UserDto;
import com.boot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 拷贝User类
 * @author youzhengjie 2022-09-29 18:08:09
 */

//---加上componentModel = "spring"之后，SpringBoot将自动整合MapStruct，我们可以通过@AutoWired或者@Resource注入这个接口。
@Mapper(componentModel = "spring")
public interface UserMapStruct {

    /**
     * 把User对象转换成UserDto
     */
    @Mappings({
            //source里面的属性名是User对象的，target里面的属性名是生成的UserDto对象的
            @Mapping(source = "userName",target = "userName"),
            @Mapping(source = "password",target = "password")
    })
    UserDto toUserDto(User user);

}
