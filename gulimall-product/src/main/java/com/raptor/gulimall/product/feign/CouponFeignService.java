package com.raptor.gulimall.product.feign;


import com.raptor.common.to.SkuReductionTo;
import com.raptor.common.to.SpuBoundsTo;
import com.raptor.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author hl
 * @Data 2020/7/28
 */
@Component
@FeignClient(value = "gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/saveSpuBoundTo")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(SkuReductionTo skuReductionTo);
}
