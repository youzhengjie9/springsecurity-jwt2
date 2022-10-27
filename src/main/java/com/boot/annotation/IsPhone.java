package com.boot.annotation;

import com.boot.validate.ValidatePhone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 判断是否是手机号注解
 * @author youzhengjie
 * @date 2022/10/25 23:56:46
 */
@Documented
@Constraint(validatedBy= ValidatePhone.class)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsPhone {

    String message() default "手机号错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
