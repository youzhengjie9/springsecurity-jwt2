package boot;

import com.boot.SecurityJwtApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = SecurityJwtApplication.class)
public class PassWordTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void test(){

        String encodePassword = passwordEncoder.encode("123456");

        System.out.println(encodePassword);
    }


}
