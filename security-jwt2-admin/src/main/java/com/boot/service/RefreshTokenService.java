package com.boot.service;

import com.boot.data.ResponseResult;
import com.boot.vo.TokenVO;

/**
 * @author youzhengjie
 */
public interface RefreshTokenService {

    ResponseResult<TokenVO> refresh(String refreshToken);

}
