package com.shop.controller.portal;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.common.Const;
import com.shop.common.LoginCheck;
import com.shop.common.Message;
import com.shop.dao.OrderMapper;
import com.shop.pojo.Cart;
import com.shop.pojo.Order;
import com.shop.pojo.Shipping;
import com.shop.pojo.User;
import com.shop.service.IOrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private IOrderService iOrderService;
	@Autowired
	private LoginCheck loginCheck;
	@Autowired
	private OrderMapper orderMapper;
	
	// 对接生成订单的展示页面：商品信息、收货地址
	@RequestMapping("/preorder")
	@ResponseBody
	public Message preorder(HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iOrderService.preorder(user);
	}
	
	@RequestMapping("/generateOrder")
	@ResponseBody
	public Message generateOrder(Integer shippingId, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iOrderService.generateOrder(shippingId, user);
	}
	
	@RequestMapping("/saveAddress")
	@ResponseBody
	public Message saveAddress(Shipping shipping, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iOrderService.saveOrEditAddress(shipping, user);
	}
	
	@RequestMapping("/editAddress")
	@ResponseBody
	public Message editAddress(Shipping shipping, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iOrderService.saveOrEditAddress(shipping, user);
	}
	
	@RequestMapping("/listOrder")
	@ResponseBody
	public Message listOrder(HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iOrderService.listOrder(user, 1, 10);
	}
	
	@RequestMapping("/cancelOrder")
	@ResponseBody
	public Message cancelOrder(Integer orderId, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iOrderService.cancelOrder(orderId);
	}
	
	// 已付款未发货的情况，申请退款
	@RequestMapping("/applyRefund")
	@ResponseBody
	public Message applyRefund(Integer orderId, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return null;
	}
	
	// 已发货(已付款)的情况，申请退货(退款)
	@RequestMapping("/applyReturnGoods")
	@ResponseBody
	public Message applyReturnGoods(Integer orderId, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return null;
	}
}
