package com.boot.controller;

import com.boot.data.ResponseResult;
import com.boot.enums.ResponseType;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码控制器
 *
 * @author youzhengjie
 * @date 2022/10/10 16:56:28
 */
@RestController
@RequestMapping("/api")
public class CaptchaController {

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/captcha")
    public ResponseResult captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 拿到自动为我们生成的验证码
        String code = specCaptcha.text().toLowerCase();
        // 随机生成一个验证码id作为key，返回给前端，前端可以通过这个验证码key在redis中找到正确的验证码
        String key = UUID.randomUUID().toString().replaceAll("-","");
        // 将验证码存入Redis中,并设置有效期30分钟
        redisTemplate.opsForValue().set(key,code,30, TimeUnit.MINUTES);

        //拿到生成的验证码图片的base64.
        String imageBase64 = specCaptcha.toBase64();
        //将验证码的key和验证码图片的base64返回给前端
        Map<String, String> result = new ConcurrentHashMap<>();
        result.put("key",key);
        result.put("imageBase64",imageBase64);
        return new ResponseResult
                (ResponseType.SUCCESS.getCode(), ResponseType.SUCCESS.getMessage(),result);
    }

}
