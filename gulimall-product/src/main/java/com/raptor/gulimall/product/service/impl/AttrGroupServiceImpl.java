package com.raptor.gulimall.product.service.impl;

import com.raptor.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.raptor.gulimall.product.dao.AttrDao;
import com.raptor.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.raptor.gulimall.product.entity.AttrEntity;
import com.raptor.gulimall.product.service.AttrService;
import com.raptor.gulimall.product.vo.AttrGroupWithAttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.raptor.common.utils.PageUtils;
import com.raptor.common.utils.Query;

import com.raptor.gulimall.product.dao.AttrGroupDao;
import com.raptor.gulimall.product.entity.AttrGroupEntity;
import com.raptor.gulimall.product.service.AttrGroupService;
import org.springframework.util.ObjectUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
        if (!ObjectUtils.isEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        } else {
            wrapper.eq("catelog_id", catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }

    }

    @Override
    public List<AttrGroupWithAttrVo> getAttrWithAttrs(Long catelogId) {
        List<AttrGroupEntity> groupEntityList = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrVo> attrVoList = new ArrayList<>(100);
        for (int i = 0; i < groupEntityList.size(); i++) {
            AttrGroupEntity attrGroupEntity = groupEntityList.get(i);
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", attrGroupEntity.getAttrGroupId()));
            List<Long> attrIds = attrAttrgroupRelationEntityList.stream().map(item -> {
                return item.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrEntities = null;
            if (attrIds != null && attrIds.size() > 0) {
                attrEntities = attrDao.selectBatchIds(attrIds);
            }
            AttrGroupWithAttrVo attrGroupAndAttrVo = new AttrGroupWithAttrVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupAndAttrVo);
            attrGroupAndAttrVo.setAttrs(attrEntities);
            attrVoList.add(attrGroupAndAttrVo);
        }
        return attrVoList;
    }
}