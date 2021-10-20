package com.raptor.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.to.SkuReductionTo;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-12 21:42:50
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

