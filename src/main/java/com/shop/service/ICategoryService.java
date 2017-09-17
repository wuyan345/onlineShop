package com.shop.service;

import com.shop.common.Message;

public interface ICategoryService {

	Message addCategory(int parentId, String name);
	
	Message editCategoryName(int categoryId, String name);
	
	Message getChrildrenCategory(int categoryId);
	
	Message getDeepCategory(int categoryId);
}
