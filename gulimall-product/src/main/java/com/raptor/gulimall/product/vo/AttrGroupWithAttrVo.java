package com.raptor.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.raptor.gulimall.product.entity.AttrEntity;
import com.raptor.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author raptor
 * @description AttrGroupWithAttrVo
 * @date 2021/10/18 20:31
 */
@Data
public class AttrGroupWithAttrVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    private List<AttrEntity> attrs;
}
