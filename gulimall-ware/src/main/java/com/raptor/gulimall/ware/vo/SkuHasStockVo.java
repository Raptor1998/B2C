package com.raptor.gulimall.ware.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

/**
 * @author raptor
 * @description SkuHasStockVo
 * @date 2021/10/27 20:38
 */
@Data
public class SkuHasStockVo {

    private Long skuId;
    private Boolean hasStock;
}
