package com.raptor.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raptor.common.utils.PageUtils;
import com.raptor.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-13 14:36:06
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

