package com.shop.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	@RequestMapping(value = "/addGoods", method = RequestMethod.POST)
	@ResponseBody
	public Message addGoods(Goods goods, HttpSession session){
//		if(!loginCheck.check(session, Const.MANAGER))
//			return Message.errorMsg("未登录");
		return iGoodsService.addGoods(goods);
	}
	
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	@ResponseBody
	public Message uploadImage(@RequestParam("upload_image")MultipartFile[] multipartFiles, HttpSession session){
//		if(!loginCheck.check(session, Const.MANAGER))
//			return Message.errorMsg("未登录");
		return FTPUtil.upload(multipartFiles);
	}

	@RequestMapping(value = "/uploadRichText", method = RequestMethod.POST)
	@ResponseBody
	public Map uploadRichText(@RequestParam("upload_richText")MultipartFile[] multipartFiles, HttpSession session){
//		if(!loginCheck.check(session, Const.MANAGER))
//			return Message.errorMsg("未登录");
		Message<List<String>> message = FTPUtil.upload(multipartFiles);
		if(message.isSuccess()){
			Map map = new HashMap<>();
			map.put("success", true);
			map.put("msg", "上传成功");
			map.put("file_path", Const.HTTP_IMAGE_PREFIX + message.getData().get(0));
			return map;
		}else {
			Map map = new HashMap<>();
			map.put("success", false);
			map.put("msg", "上传失败");
			return map;
		}
	}
}
