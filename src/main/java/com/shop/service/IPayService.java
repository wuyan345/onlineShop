package com.shop.service;

import java.util.Map;

import com.shop.common.Message;

public interface IPayService {

	Message pay(Integer orderId, String path);
	
	Message refund(Integer orderId, Integer orderItemId);
	
	Message alipayCallback(Map<String, String> map);
}
