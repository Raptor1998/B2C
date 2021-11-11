package com.raptor.common.to.es;

import lombok.Data;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raptor
 * @description SpuEsModel
 * @date 2021/10/27 19:58
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;

    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;

    private Long brandId;
    private Long catelogId;
    private String brandName;
    private String brandImg;
    private String catelogName;
    private List<Attrs> attrs;

    @Data
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
