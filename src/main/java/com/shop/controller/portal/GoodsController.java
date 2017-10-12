package com.shop.controller.portal;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.shop.common.Message;
import com.shop.service.IGoodsService;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private IGoodsService iGoodsService;
	
	@RequestMapping("/test")
	@ResponseBody
	public void test(String a, String b){
		System.out.println(new Date());
		System.out.println("a: " + a + ", b: " + b);
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public Message<PageInfo> list(Integer categoryId){
		return iGoodsService.list(categoryId, 1, 10);
	}
	
	@RequestMapping("/getGoodsDetail")
	@ResponseBody
	public Message getGoodsDetail(int goodsId){
		return iGoodsService.getGoodsDetail(goodsId);
	}
	
	// keyword模糊搜索或id精确搜索，keyword和id只能有其中一个
	@RequestMapping(value = "/searchGoods", method = RequestMethod.POST)
	@ResponseBody
	public Message<PageInfo> searchGoods(String keyword, Integer goodsId){
		return iGoodsService.searchGoods(keyword, 1, 10);
	}
}
