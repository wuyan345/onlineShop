package com.shop.service;

import com.shop.common.Message;
import com.shop.pojo.Shipping;

public interface IShippingService {

	Message addShipping(Shipping shipping, Integer userId);
	
	Message list(Integer userId);
}
