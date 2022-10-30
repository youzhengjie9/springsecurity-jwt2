package com.boot;

import com.boot.config.JwtProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author youzhengjie 2022-09-29 16:12:50
 */
@SpringBootApplication
@EnableKnife4j //开启knife4j美化
@EnableConfigurationProperties(
        {
                JwtProperties.class
        }
)
public class SecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtApplication.class,args);
    }

}
