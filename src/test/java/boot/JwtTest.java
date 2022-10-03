package boot;

import com.boot.SecurityJwtApplication;
import com.boot.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest(classes = SecurityJwtApplication.class)
public class JwtTest {

    @Test
    void createTokenTest(){

        String userid="165399880288699493";
        Map<String, String> accessTokenAndRefreshToken = JwtUtil.createAccessTokenAndRefreshToken(userid);

    }

    @Test
    void getSubjectTest(){



    }

    @Test
    void logoutTokenTest(){


    }



}
