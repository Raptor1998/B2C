package com.raptor.gulimall.member.dao;

import com.raptor.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-13 14:36:06
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
