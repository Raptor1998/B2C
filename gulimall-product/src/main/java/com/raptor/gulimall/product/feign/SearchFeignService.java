package com.raptor.gulimall.product.feign;

import com.raptor.common.to.es.SkuEsModel;
import com.raptor.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author raptor
 * @description SearchFeignService
 * @date 2021/10/28 20:52
 */
@Component
@FeignClient(value ="gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/product")
    public R productStateUp(@RequestBody List<SkuEsModel> skuEsModels);
}
