package com.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author youzhengjie 2022-09-29 16:56:06
 */
@Configuration
public class SwaggerConfig { // 配置swagger2

  /**
   * Springfox提供Docket对象，为其设置相关属性，将其注册成为spring的bean后， 可以在接口文档中展示（可配置多个Docket的bean，对应不同分组的接口）
   */
  @Bean
  public Docket getUserDocket() {
    ApiInfo apiInfo =
        new ApiInfoBuilder()
            // api标题
            .title("springsecurity-jwt2")
            // api描述
            .description("springsecurity-jwt2")
            // 版本号
            .version("0.1.0")
            // 本API负责人的联系信息
            .contact(new Contact("youzhengjie",
                    "https://blog.csdn.net/weixin_50071998",
                    "1550324080@qq.com"))
            .build();
    // 文档类型（swagger2）
    return new Docket(DocumentationType.SWAGGER_2)
        // 设置包含在json ResourceListing响应中的api元信息
        .apiInfo(apiInfo)
        // 启动用于api选择的构建器
        .select()
        // 扫描接口的包
        .apis(RequestHandlerSelectors.basePackage("com.boot.controller"))
        // 路径过滤器（扫描所有路径）
        .paths(PathSelectors.any())
        .build();
  }
}
