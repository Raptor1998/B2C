package com.raptor.gulimall.feign;

import com.raptor.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author raptor
 * @description ThridPartyServier
 * @date 2021/11/9 20:06
 */
@Component
@FeignClient("gulimall-third-party")
public interface ThridPartyService {

    @GetMapping(value = "/sms/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
