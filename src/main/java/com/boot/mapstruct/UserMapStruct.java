package com.boot.mapstruct;

import com.boot.dto.UserFormDto;
import com.boot.dto.UserLoginDto;
import com.boot.entity.User;
import com.boot.security.LoginUser;
import com.boot.vo.TokenVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 拷贝User
 * @author youzhengjie 2022-09-29 18:08:09
 */

//---加上componentModel = "spring"之后，SpringBoot将自动整合MapStruct，我们可以通过@AutoWired或者@Resource注入这个接口。
@Mapper(componentModel = "spring")
public interface UserMapStruct {

//    /**
//     * 把User对象转换成UserDto
//     */
//    @Mappings({
//            //source里面的属性名是User对象的，target里面的属性名是生成的UserDto对象的
//            @Mapping(source = "userName",target = "userName"),
//            @Mapping(source = "password",target = "password")
//    })
//    UserLoginDto toUserDto(User user);

    /**
     * LoginUser拷贝到TokenVO
     *
     * @param loginUser
     * @return {@link TokenVO}
     */
    @Mappings({
            @Mapping(source = "loginUser.user.userName",target = "userName"),
            @Mapping(source = "loginUser.user.nickName",target = "nickName"),
            @Mapping(source = "loginUser.user.avatar",target = "avatar")
    })
    TokenVO loginUserToTokenVO(LoginUser loginUser);


    /**
     * UserFormDto拷贝到User
     *
     * @param userFormDto 用户表单dto
     * @return {@link User}
     */
    @Mappings({
            @Mapping(source = "userFormDto.id",target = "id"),
            @Mapping(source = "userFormDto.userName",target = "userName"),
            @Mapping(source = "userFormDto.nickName",target = "nickName"),
            @Mapping(source = "userFormDto.password",target = "password"),
            @Mapping(source = "userFormDto.email",target = "email"),
            //布尔值要进行排除映射。
            @Mapping(ignore = true,target = "status"),
            @Mapping(source = "userFormDto.phone",target = "phone"),
            @Mapping(ignore = true,target = "sex"),
            @Mapping(source = "userFormDto.avatar",target = "avatar"),
    })
    User userFormDtoToUser(UserFormDto userFormDto);

}
