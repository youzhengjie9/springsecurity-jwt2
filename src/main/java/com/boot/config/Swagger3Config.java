package com.boot.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.ArrayList;

/**
 * swagger3配置类
 * 解决springboot2.7之后使用swagger2会造成启动报错，故升级为swagger3
 * @author youzhengjie 2022-10-04 15:23:27
 */
@Configuration
@EnableOpenApi
public class Swagger3Config {

  @Bean
  public Docket createRestApi(){
    // 文档类型为swagger3
    return new Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            //是否开启 (true=开启,false=隐藏。生产环境建议设置为false)
            .enable(true)
            .select()
            //扫描的路径包
            .apis(RequestHandlerSelectors.basePackage("com.boot.controller"))
            .paths(PathSelectors.any())
            .build();
  }

  private ApiInfo apiInfo(){
    return new ApiInfoBuilder()
            .title("security-jwt2的接口文档")
            .description("security-jwt2的接口文档")
            // 本API负责人的联系信息
            .contact(new Contact("youzhengjie",
                    "https://blog.csdn.net/weixin_50071998",
                    "1550324080@qq.com"))
            .version("1.0")
            .build();
  }

}
