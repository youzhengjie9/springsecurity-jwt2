package com.boot.validate;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.boot.annotation.IsPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证手机号
 *
 * @author youzhengjie
 * @date 2022/10/26 00:03:44
 */
public class ValidatePhone implements ConstraintValidator<IsPhone, String> {

    @Override
    public void initialize(IsPhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isNotBlank(s)) {
            return PhoneUtil.isMobile(s);
        } else {
            return false;
        }
    }
}

