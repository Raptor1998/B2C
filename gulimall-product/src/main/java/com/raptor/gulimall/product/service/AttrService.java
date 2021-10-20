package com.raptor.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.product.entity.AttrEntity;
import com.raptor.gulimall.product.vo.AttrGroupWithAttrVo;
import com.raptor.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-15 20:08:56
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(AttrVo attr);

    PageUtils queryBaseattr(Map<String, Object> params, Long catelogId,String attrType);

}

