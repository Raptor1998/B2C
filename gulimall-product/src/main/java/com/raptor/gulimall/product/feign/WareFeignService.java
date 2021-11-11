package com.raptor.gulimall.product.feign;

import com.raptor.common.utils.R;
import com.raptor.gulimall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author raptor
 * @description WareFeignService
 * @date 2021/10/27 20:52
 */
@Component
@FeignClient(value = "gulimall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
