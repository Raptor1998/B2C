package com.raptor.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.product.entity.SpuInfoEntity;
import com.raptor.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-15 20:08:56
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuSaveInfo(SpuSaveVo spuInfo);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

