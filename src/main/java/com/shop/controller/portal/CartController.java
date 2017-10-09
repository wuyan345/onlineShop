package com.shop.controller.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.bo.order.CartGoodsBo;
import com.shop.common.Const;
import com.shop.common.LoginCheck;
import com.shop.common.Message;
import com.shop.common.Const.Cart;
import com.shop.dao.CartMapper;
import com.shop.dao.GoodsMapper;
import com.shop.dao.OrderItemMapper;
import com.shop.pojo.OrderItem;
import com.shop.pojo.User;
import com.shop.service.ICartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private ICartService iCartService;
	@Autowired
	private LoginCheck loginCheck;
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired 
	private OrderItemMapper orderItemMapper;
	
	@RequestMapping("/test")
	@ResponseBody
	public Message test(){
//		List<OrderItem> orderItemList = new ArrayList<>();
//		OrderItem orderItem1 = new OrderItem();
//		OrderItem orderItem2 = new OrderItem();
//		orderItem1.setGoodsId(201);
//		orderItem1.setQuantity(2);
//		orderItem2.setGoodsId(202);
//		orderItem2.setQuantity(2);
//		orderItemList.add(orderItem1);
//		orderItemList.add(orderItem2);
//		int count = goodsMapper.batchUpdateQuantityForCancelOrder(orderItemList);
//		System.out.println(count);
		String str = "1504409920497";
		
		List<OrderItem> orderItemList = orderItemMapper.selectQuantityByOrderNo(new Long(str));
		return null;
	}
	
	@RequestMapping("/addCart")
	@ResponseBody
	public Message addCart(Integer goodsId, Integer goodsQuantity, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.addCart(user, goodsId, goodsQuantity);
	}
	
	@RequestMapping("/listCart")
	@ResponseBody
	public Message listCart(HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.listCart(user);
	}
	
	@RequestMapping("/changeQuantity")
	@ResponseBody
	public Message changeQuantity(Integer cartId, int quantity, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.changeQuantity(cartId, quantity, user);
	}
	
	// 单选或全选
	@RequestMapping("/select")
	@ResponseBody
	public Message select(Integer cartId, HttpSession session){
		if(!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.selectOrUnselect(user, cartId, Const.Cart.SELECTED);
	}
	
	// 单不选或全不选
	@RequestMapping("/unselect")
	@ResponseBody
	public Message unselect(Integer cartId, HttpSession session) {
		if (!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.selectOrUnselect(user, cartId, Const.Cart.UNSELECT);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Message delete(Integer cartId, HttpSession session) {
		if (!loginCheck.check(session, Const.NORMAL_USER))
			return Message.errorMsg("未登录或无权限");
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.delete(cartId, user);
	}
}
