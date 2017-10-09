package com.shop.service;

import java.util.Map;

import com.shop.common.Message;

public interface IPayService {

	Message pay(Integer orderId, Integer userId, String path);
	
	Message refund(Integer orderId, Integer orderItemId);
	
	Message refundForOrder(Integer orderId);
	
	Message alipayCallback(Map<String, String> map);
}
