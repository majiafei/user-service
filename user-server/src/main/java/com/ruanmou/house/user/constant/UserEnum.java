package com.ruanmou.house.user.constant;

/**
 * @ProjectName: house
 * @Package: com.ruanmou.house.user.constant
 * @ClassName: UserEnum
 * @Author: majiafei
 * @Description: 用户枚举
 * @Date: 2018/12/18 17:50
 */
public enum UserEnum {
    ACTIVE(1, "已激活"),
    DISABLE(0, "未激活");

    private int code;

    private String message;

    UserEnum(int code, String message) {
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
