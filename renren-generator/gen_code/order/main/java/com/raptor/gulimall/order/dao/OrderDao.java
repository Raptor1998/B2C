package com.raptor.gulimall.order.dao;

import com.raptor.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-13 14:44:39
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
