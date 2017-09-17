package com.shop.dao;

import java.util.List;

import com.shop.pojo.Category;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
    
    /**
     * 返回某一根节点下的所有二级子节点
     * @param parentId
     * @return
     */
    List<Category> selectChildrenByParentId(int categoryId);
    
    List<Category> selectAllCategory();
}