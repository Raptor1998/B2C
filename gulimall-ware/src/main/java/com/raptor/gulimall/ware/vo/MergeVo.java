package com.raptor.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author raptor
 * @description MergeVo
 * @date 2021/10/19 16:01
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
