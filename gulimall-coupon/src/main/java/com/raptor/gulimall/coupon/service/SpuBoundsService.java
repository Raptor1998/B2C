package com.raptor.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.to.SpuBoundsTo;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-12 21:42:50
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuBounds(SpuBoundsTo spuBoundsTo);
}

