package com.raptor.common.exception;

/**
 * @author raptor
 * @description BizCodeEnum
 * @date 2021/10/16 20:47
 */
public enum BizCodeEnum {
    VAILD_EXCEPTION(10001, "参数校验异常"),
    UNKNOW_ERROR(10000, "未知的错误");

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
