package com.raptor.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author raptor
 * @description Bounds
 * @date 2021/10/18 20:58
 */
@Data
public class Bounds {
    /**
     * 购买积分
     */
    private BigDecimal buyBounds;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;

}