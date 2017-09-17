package com.shop.controller.backend;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shop.common.Const;
import com.shop.common.LoginCheck;
import com.shop.common.Message;
import com.shop.pojo.Goods;
import com.shop.service.IGoodsService;
import com.shop.utils.FTPUtil;

@Controller
@RequestMapping("/manage/goods")
public class GoodsManageController {

	@Autowired
	private IGoodsService iGoodsService;
	@Autowired
	private LoginCheck loginCheck;
	
	@RequestMapping("/addGoods")
	@ResponseBody
	public Message addGoods(Goods goods, HttpSession session){
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录");
		return null;
	}
	
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	@ResponseBody
	public Message uploadImage(@RequestParam("upload_image")MultipartFile[] multipartFiles, HttpSession session){
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录");
		return FTPUtil.upload(multipartFiles);
	}

	@RequestMapping(value = "/uploadRichText", method = RequestMethod.POST)
	@ResponseBody
	public Message uploadRichText(@RequestParam("upload_richText")MultipartFile[] multipartFiles, HttpSession session){
		if(!loginCheck.check(session, Const.MANAGER))
			return Message.errorMsg("未登录");
		FTPUtil.upload(multipartFiles);
		return null;
	}
}
