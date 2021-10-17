package com.raptor.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.raptor.gulimall.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raptor.gulimall.member.entity.MemberEntity;
import com.raptor.gulimall.member.service.MemberService;
import com.raptor.common.utils.PageUtils;
import com.raptor.common.utils.R;


/**
 * 会员
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-13 14:36:06
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    private MemberService memberService;
    private CouponFeignService couponFeignService;

    @Autowired
    public MemberController(MemberService memberService, CouponFeignService couponFeignService) {
        this.memberService = memberService;
        this.couponFeignService = couponFeignService;
    }

    @RequestMapping("/all")
    public R getAllMemberCoupon() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        R r = couponFeignService.memberCoupons();
        Object coupon = r.get("coupon");
        return R.ok().put("member", memberEntity).put("coupon", coupon);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}