package com.raptor.gulimall.member.feign;

import com.raptor.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author raptor
 * @description CouponFeignService
 * @date 2021/10/13 17:33
 */
@FeignClient("gulimall-coupon")
@Component
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/all")
    public R memberCoupons();
}
