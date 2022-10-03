package com.boot.vo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 封装Token
 * @author youzhengjie 2022-10-01 23:46:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Builder
public class TokenVO implements Serializable {

    private String accessToken;

    private String refreshToken;

}
