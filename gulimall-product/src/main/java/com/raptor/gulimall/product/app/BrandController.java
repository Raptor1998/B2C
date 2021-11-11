package com.raptor.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import com.raptor.common.valid.AddGroup;
import com.raptor.common.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raptor.gulimall.product.entity.BrandEntity;
import com.raptor.gulimall.product.service.BrandService;
import com.raptor.common.utils.PageUtils;
import com.raptor.common.utils.R;


/**
 * 品牌
 *
 * @author raptor
 * @email knightcwh@163.com
 * @date 2021-10-15 20:08:57
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand/*, BindingResult bindResult*/) {
        //Map<String,String> map=new HashMap<>();
        //if (bindResult.hasErrors()) {
        //    bindResult.getFieldErrors().forEach((item) -> {
        //        //错误信息
        //        String defaultMessage = item.getDefaultMessage();
        //        //错误的属性名
        //        String field = item.getField();
        //        map.put(field,defaultMessage);
        //    });
        //
        //    return R.error(400, "数据不合法").put("data", map);
        //} else {
        //    brandService.save(brand);
        //}
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update( @Validated({UpdateGroup.class})@RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
