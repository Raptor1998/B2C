package com.raptor.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.raptor.gulimall.product.service.CategoryService;
import com.raptor.gulimall.product.vo.AttrGroupWithAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raptor.gulimall.product.entity.AttrGroupEntity;
import com.raptor.gulimall.product.service.AttrGroupService;
import com.raptor.common.utils.PageUtils;
import com.raptor.common.utils.R;



/**
 * 属性分组
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-15 20:08:57
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;


    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);


        PageUtils page =  attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }

    @RequestMapping("/{catelogId}/withattr")
    public R getAttrWithAttrs(@PathVariable Long catelogId) {
        List<AttrGroupWithAttrVo> vos = attrGroupService.getAttrWithAttrs(catelogId);
        return R.ok().put("data", vos);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

		Long catelogPath = attrGroup.getCatelogId();
        Long[] path = categoryService.findCarelogPath(catelogPath);
		attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
