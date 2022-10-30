//package boot;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.boot.SecurityJwtApplication;
//import com.boot.dto.UserDto;
//import com.boot.entity.User;
//import com.boot.mapper.UserMapper;
//import com.boot.mapstruct.UserMapStruct;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//@SpringBootTest(classes = SecurityJwtApplication.class)
//public class MapStructTest {
//
//    @Resource
//    private UserMapStruct userMapStruct;
//
//    @Autowired
//    private UserMapper userMapper;
//
//
//    @Test
//    void toUserDtoTest(){
//
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(User::getId,1001);
//        User user = userMapper.selectOne(lambdaQueryWrapper);
//        System.out.println(user);
//
//        //转换
//        UserDto userDto = userMapStruct.toUserDto(user);
//        System.out.println(userDto);
//    }
//
//}
