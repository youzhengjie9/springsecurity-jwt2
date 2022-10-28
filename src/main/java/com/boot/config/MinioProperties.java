package com.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 高性能分布式存储-minio属性类
 *
 * @author youzhengjie
 * @date 2022/10/28 00:22:48
 */
@Component
@Data
@ConfigurationProperties(prefix = "minio")
@EnableConfigurationProperties({
        MinioProperties.class
})
public class MinioProperties {

    /**
     * minio的通信端点（url），和控制台端点不一样
     */
    private String endpoint;
    /**
     * 说白了就是minio的帐号
     */
    private String accessKey;
    /**
     * 说白了就是minio的密码
     */
    private String secretKey;
    /**
     * 指定操作哪一个桶
     */
    private String bucketName;
}
