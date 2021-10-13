package com.raptor.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-12 21:42:50
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

