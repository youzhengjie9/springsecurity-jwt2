package boot;

import com.boot.SecurityJwtApplication;
import com.boot.config.JwtProperties;
import com.boot.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;

@SpringBootTest(classes = SecurityJwtApplication.class)
public class JwtTest {

    @Autowired
    private JwtProperties jwtProperties;

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

    @Test
    void test()
    {
        String ac="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxYWU5MDhmNjc0MjI0ZjJjYTc5ZTE0NWI2YTRhN2Y3YSIsInN1YiI6IjEwMDEiLCJpc3MiOiJ5b3V6aGVuZ2ppZSIsImlhdCI6MTY2NDc4NTQzNCwiZXhwIjoxNjY0Nzk2MjM0fQ.iBCaDK0kc3nQlw8W2b1U-XYMXExvdV4MZyyBKVhF7tk";

        String rf="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMDgzZTg5ZGYxMzk0NWFkYTRkNDE3YmM5YzE0ZDlhNiIsInN1YiI6IjEwMDEiLCJpc3MiOiJ5b3V6aGVuZ2ppZSIsImlhdCI6MTY2NDc4NTQzNCwiZXhwIjoxNjY0ODA3MDM0fQ.Ogee9A437nnZMIHmDNXzK-nNlgo4Y53H69K1jUg6w-M";

        //解析accessToken
        Claims claims = JwtUtil.parseAccessToken(ac);
        //获取accessToken最开始生成的时间
        Date accessTokenIssuedAt = claims.getIssuedAt();
        //获取accessToken最开始生成的时间的毫秒值
        long accessTokenIssuedAtMillis = accessTokenIssuedAt.getTime();
        //获取当前时间的毫秒值
        long currentTimeMillis = System.currentTimeMillis();

        //计算accessToken已经用了多久（当前时间毫秒值-accessToken最开始生成的时间的毫秒值）
        long accessTokenTimed=currentTimeMillis-accessTokenIssuedAtMillis;

        //获取refreshToken最大过期时间
        long refreshTokenExpired = jwtProperties.getRefreshTokenExpired();

        //计算出黑名单key过期时间最好为多少毫秒
        long blackListExpiredMillis=refreshTokenExpired-accessTokenTimed;

        System.out.println(blackListExpiredMillis);

    }


}
