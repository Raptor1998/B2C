package com.raptor.gulimall.product.service.impl;

import com.raptor.gulimall.product.dao.CategoryDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.raptor.common.utils.PageUtils;
import com.raptor.common.utils.Query;

import com.raptor.gulimall.product.entity.CategoryEntity;
import com.raptor.gulimall.product.service.CategoryService;
import org.springframework.validation.annotation.Validated;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);
        List<CategoryEntity> categoryEntityTree = categoryEntityList.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(menu -> menu.setChildren(getChildren(menu, categoryEntityList)))
                .sorted((menu1, menu2) -> {
                    Integer sort1 = menu1.getSort();
                    Integer sort2 = menu2.getSort();
                    return (null == sort1 ? 0 : sort1) - (null == sort2 ? 0 : sort2);
                }).collect(Collectors.toList());
        return categoryEntityTree;
    }

    /**
     * 根据父节点获取子节点
     *
     * @param root
     * @param allNode
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> allNode) {
        List<CategoryEntity> childrens = allNode.stream().filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                .peek(menu -> menu.setChildren(getChildren(menu, allNode)))
                .sorted((menu1, menu2) -> {
                    Integer sort1 = menu1.getSort();
                    Integer sort2 = menu2.getSort();
                    return (null == sort1 ? 0 : sort1) - (null == sort2 ? 0 : sort2);
                }).collect(Collectors.toList());
        return childrens;
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        //TODO 1.检查当前删除的惨淡，是否被别的地方引用
        baseMapper.deleteBatchIds(catIds);
    }

    @Override
    public Long[] findCarelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();

        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[3]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), paths);
        }
        return paths;
    }
}