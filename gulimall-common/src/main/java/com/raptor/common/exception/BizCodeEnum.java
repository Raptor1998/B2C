package com.raptor.common.exception;

/**
 * @author raptor
 * @description BizCodeEnum
 * @date 2021/10/16 20:47
 */
public enum BizCodeEnum {
    VAILD_EXCEPTION(10001, "参数校验异常"),
    UNKNOW_ERROR(10000, "未知的错误"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架错误"),
    SMS_CODE_EXCEPTION(20001,"发送验证码出错"),
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    TO_MANY_REQUEST(10002,"请求流量过大，请稍后再试"),
    USER_EXIST_EXCEPTION(15001,"存在相同的用户"),
    PHONE_EXIST_EXCEPTION(15002,"存在相同的手机号"),
    NO_STOCK_EXCEPTION(21000,"商品库存不足"),
    LOGINACCT_PASSWORD_EXCEPTION(15003,"账号或密码错误"),;

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
