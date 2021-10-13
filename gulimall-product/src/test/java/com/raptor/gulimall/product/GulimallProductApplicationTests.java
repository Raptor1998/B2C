package com.raptor.gulimall.product;

import com.raptor.gulimall.product.entity.BrandEntity;
import com.raptor.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {

        //BrandEntity brandEntity = new BrandEntity();
        //brandEntity.setBrandId(1L);
        ////brandEntity.setName("华为");
        ////brandService.save(brandEntity);
        ////System.out.println("保存成功");
        //brandEntity.setName("小米");
        //brandService.updateById(brandEntity);
        BrandEntity brandEntity = brandService.getById(1L);
        System.out.println(brandEntity);
    }

}
