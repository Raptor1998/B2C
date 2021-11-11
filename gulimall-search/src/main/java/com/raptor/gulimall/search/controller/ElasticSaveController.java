package com.raptor.gulimall.search.controller;

import com.raptor.common.exception.BizCodeEnum;
import com.raptor.common.to.es.SkuEsModel;
import com.raptor.common.utils.R;
import com.raptor.gulimall.search.service.ProductSaveService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author raptor
 * @description ElasticSaveController
 * @date 2021/10/28 20:36
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class ElasticSaveController {

    @Autowired
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStateUp(@RequestBody List<SkuEsModel> skuEsModels) {
        Boolean b = false;
        try {
            productSaveService.productStateUp(skuEsModels);
        } catch (Exception e) {
            log.error("商品上架错误{}", e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (!b) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }


}
