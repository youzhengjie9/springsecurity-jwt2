package com.boot.enums;

/**
 * 响应类型枚举类
 * @author youzhengjie 2022-09-22 22:47:21
 */
public enum ResponseType {
    /**
     * 响应类型
     */
    SUCCESS(200,"请求成功"),
    ERROR(500,"请求失败"),
    NOT_LOGIN(401,"用户未登录，请重新登录"),
    NOT_PERMISSION(403,"用户没有权限"),
    LOGIN_ERROR(601,"登录失败，请检查帐号或者密码是否正确"),
    TOKEN_ERROR(602,"解析token失败，请检查token是否正确"),
    USERNAME_PASSWORD_ERROR(603,"用户名或者密码不正确"),
    REFRESH_TOKEN_SUCCESS(701,"刷新token成功"),
    REFRESH_TOKEN_ERROR(702,"刷新token失败")
    ;


    private int code;
    private String message;

    ResponseType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
