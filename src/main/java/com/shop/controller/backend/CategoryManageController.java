package com.shop.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.common.Const;
import com.shop.common.LoginCheck;
import com.shop.common.Message;
import com.shop.service.ICategoryService;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

	@Autowired
	private ICategoryService iCategoryService;
	@Autowired 
	private LoginCheck loginCheck;
	
	@RequestMapping("/addCategory")
	@ResponseBody
	public Message addCategory(int parentId, String name, HttpSession session){
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录或无权限");
		return null;
	}
	
	@RequestMapping("/editName")
	@ResponseBody
	public Message editCategoryName(int categoryId, String name, HttpSession session){
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录或无权限");
		return null;
	}
	
	// 返回某一根节点下的所有二级子节点，三级子节点不返回（若有的话）
	@RequestMapping("/getChrildrenCategory")
	@ResponseBody
	public Message getChrildrenCategory(int categoryId, HttpSession session) {
//		if(!loginCheck.check(session, Const.MANAGER))
//			return Message.errorMsg("未登录或无权限");
		return iCategoryService.getChrildrenCategory(categoryId);
	}

	// 返回某一根节点下的所有子节点
	@RequestMapping("/getDeepCategory")
	@ResponseBody
	public Message getDeepCategory(int categoryId, HttpSession session) {
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录或无权限");
		return iCategoryService.getDeepCategory(categoryId);
	}
}
