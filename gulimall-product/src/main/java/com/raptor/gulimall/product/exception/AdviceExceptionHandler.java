package com.raptor.gulimall.product.exception;

import com.raptor.common.exception.BizCodeEnum;
import com.raptor.common.utils.R;
import com.sun.applet2.AppletParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author raptor
 * @description AdviceExceptionHandler
 * @date 2021/10/16 20:39
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.raptor.gulimall.product.controller")
public class AdviceExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public R handleVaildException(MethodArgumentNotValidException e) {
        log.error("数据校验异常{},异常类型{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item) -> {

            map.put(item.getField(), item.getDefaultMessage());

        });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        return R.error(BizCodeEnum.UNKNOW_ERROR.getCode(), BizCodeEnum.UNKNOW_ERROR.getMsg());
    }

}
