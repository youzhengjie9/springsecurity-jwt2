package com.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ElasticSearch属性类
 *
 * @author youzhengjie
 * @date 2022/10/28 23:24:05
 */
@Component
@Data
@ConfigurationProperties(prefix = "elasticsearch")
@EnableConfigurationProperties({
        ElasticSearchProperties.class
})
public class ElasticSearchProperties {

    /**
     * elasticsearch主机名
     */
    private String hostname;

    /**
     * elasticsearch端口
     */
    private int port;

    /**
     * 指定协议
     */
    private String scheme;

}
