package com.raptor.gulimall.search.service;

import com.raptor.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author raptor
 * @description ProductSaveService
 * @date 2021/10/28 20:38
 */
public interface ProductSaveService {

    Boolean productStateUp(List<SkuEsModel> skuEsModels) throws IOException;

}
