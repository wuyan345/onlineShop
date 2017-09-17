package com.shop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.Const;
import com.shop.common.Message;
import com.shop.dao.CategoryMapper;
import com.shop.pojo.Category;
import com.shop.service.ICategoryService;

@Service("iCategoryService")
@Transactional
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public Message addCategory(int parentId, String name) {
		Category category = new Category();
		category.setParentId(parentId);
		category.setName(name);
		category.setStatus(Const.Category.NORMAL);
		int count = categoryMapper.insert(category);
		return null;
	}

	@Override
	public Message editCategoryName(int categoryId, String name) {
		Category category = new Category();
		category.setId(categoryId);
		category.setName(name);
		int count = categoryMapper.updateByPrimaryKeySelective(category);
		if(count <= 0)
			return Message.errorMsg("编辑分类名称失败");
		return Message.successMsg("编辑分类名称成功");
	}

	@Override
	public Message getChrildrenCategory(int categoryId) {
		List<Category> categoryList = new ArrayList<>();
		categoryList = categoryMapper.selectChildrenByParentId(categoryId);
		return Message.successData(categoryList);
	}

	@Override
	public Message getDeepCategory(int categoryId) {
		List<Category> allCategoryList = new ArrayList<>();
		allCategoryList = categoryMapper.selectAllCategory();
		List<Category> categoryList = new ArrayList<>();
		categoryList = categoryRecursion(allCategoryList, categoryList, categoryId);
		return Message.successData(categoryList);
	}
	
	private List<Category> categoryRecursion(List<Category> allCategoryList, List<Category> categoryList, int categoryId){
		// 唯一出口：没找到与parentId相同的，则for循环结束返回
		for (Category category : allCategoryList) {
			if(category.getParentId() == categoryId){
				categoryList.add(category);
				categoryRecursion(allCategoryList, categoryList, category.getId());
			}
		}
		return categoryList;
	}
	
}
