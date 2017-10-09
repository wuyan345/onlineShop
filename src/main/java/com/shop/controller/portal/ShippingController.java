package com.shop.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.common.Const;
import com.shop.common.LoginCheck;
import com.shop.common.Message;
import com.shop.pojo.Shipping;
import com.shop.pojo.User;
import com.shop.service.IShippingService;

@Controller
@RequestMapping("/shipping")
public class ShippingController {

	@Autowired
	private LoginCheck loginCheck;
	@Autowired
	private IShippingService iShippingService;
	
	@RequestMapping(value = "/addShipping", method = RequestMethod.POST)
	@ResponseBody
	public Message addShipping(Shipping shipping, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.addShipping(shipping, user.getId());
	}
	
	@RequestMapping(value = "/list")
	@ResponseBody
	public Message list(HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.list(user.getId());
	}
}
