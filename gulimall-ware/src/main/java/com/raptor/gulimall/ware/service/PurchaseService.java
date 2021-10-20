package com.raptor.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.ware.entity.PurchaseEntity;
import com.raptor.gulimall.ware.vo.MergeVo;

import java.util.Map;

/**
 * 采购信息
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-13 14:46:11
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);
}

