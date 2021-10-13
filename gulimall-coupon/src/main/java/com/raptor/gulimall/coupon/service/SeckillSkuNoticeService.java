package com.raptor.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-12 21:42:50
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

