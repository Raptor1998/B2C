package com.raptor.gulimall.product.vo;

import com.raptor.common.valid.AddGroup;
import com.raptor.common.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author raptor
 * @description SpuSaveVo
 * @date 2021/10/18 20:57
 */
@Data
public class SpuSaveVo {

    @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "名称不能为空")
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
}
