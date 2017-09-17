package com.shop.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.common.Message;
import com.shop.service.ICategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private ICategoryService iCategoryService;
	
}
