package com.raptor.gulimall.product.dao;

import com.raptor.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-12 21:23:51
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
