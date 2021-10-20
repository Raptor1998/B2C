package com.raptor.gulimall.product.vo;

import lombok.Data;

/**
 * @author raptor
 * @description BaseAttrs
 * @date 2021/10/18 20:58
 */
@Data
public class BaseAttrs {

    private Long attrId;
    private String attrValues;
    /**
     * 是否快速展示
     */
    private int showDesc;

}