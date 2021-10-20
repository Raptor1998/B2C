package com.raptor.gulimall.ware.service.impl;

import com.raptor.common.constant.WareConstant;
import com.raptor.gulimall.ware.entity.PurchaseDetailEntity;
import com.raptor.gulimall.ware.service.PurchaseDetailService;
import com.raptor.gulimall.ware.service.WareSkuService;
import com.raptor.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.raptor.common.utils.PageUtils;
import com.raptor.common.utils.Query;

import com.raptor.gulimall.ware.dao.PurchaseDao;
import com.raptor.gulimall.ware.entity.PurchaseEntity;
import com.raptor.gulimall.ware.service.PurchaseService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> {
                w.eq("id", key).or().like("assignee_name", key);
            });
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper.eq("status", 0).or().eq("status", 1)
        );
        return new PageUtils(page);
    }

    @Override
    public void mergePurchase(MergeVo mergeReqVo) {
        Long purchaseId = mergeReqVo.getPurchaseId();
        if (purchaseId == null) {
            // 新建的采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATE.getCode());
            baseMapper.insert(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            // 确认采购单状态为0或者1
            PurchaseEntity purchaseEntity = baseMapper.selectById(purchaseId);
            System.out.println(purchaseEntity.getStatus());
            if (purchaseEntity.getStatus() != WareConstant.PurchaseStatusEnum.CREATE.getCode() && purchaseEntity.getStatus() != WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                log.error("采购单状态不对，无法合并");
                return;
            }
        }
        List<Long> items = mergeReqVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.saveBatch(purchaseDetailEntityList);
    }
}