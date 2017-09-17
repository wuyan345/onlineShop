package com.shop.service;

import java.util.List;

import com.shop.common.Message;
import com.shop.pojo.Cart;
import com.shop.pojo.Shipping;
import com.shop.pojo.User;

public interface IOrderService {

	Message preorder(User user);
	
	Message generateOrder(Integer shippingId, User user);
	
	Message saveOrEditAddress(Shipping shipping, User user);
	
	Message listOrder(User user, int pageNum, int pageSize);
	
	Message cancelOrder(Integer orderId);
}
