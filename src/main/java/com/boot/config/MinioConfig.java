package com.boot.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置类
 *
 * @author youzhengjie
 * @date 2022/10/28 00:30:31
 */
@Configuration
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;


    /**
     * minio-client bean
     *
     * @return {@link MinioClient}
     */
    @Bean
    public MinioClient minioClient(){

        return MinioClient.builder()
                //配置minio的通信端点（url），和控制台端点不一样
                .endpoint(minioProperties.getEndpoint())
                //配置minio的帐号密码
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

}
