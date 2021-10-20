package com.raptor.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author raptor
 * @description MemberPrice
 * @date 2021/10/18 21:00
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}