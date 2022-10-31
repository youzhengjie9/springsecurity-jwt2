package com.boot.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import java.io.Serializable;
/**
 * 操作日志canal中转类。解决OperationLog类中的LocalDatetime类型的字段无法被canal接收导致报错
 *
 * @author youzhengjie
 * @date 2022/10/30 21:16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class OperationLogCanal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String type;

    private String uri;

    private String time;

    private String ip;

    private String address;

    private String browser;

    private String os;

    //canal+springboot当属性名和数据库字段不一致时，要用@Column去指定数据库字段名，否则会接收不到canal数据
    @Column(name = "oper_time")
    private String operTime;

    //canal+springboot当属性名和数据库字段不一致时，要用@Column去指定数据库字段名，否则会接收不到canal数据
    @Column(name = "del_flag")
    private Integer delFlag;

}
