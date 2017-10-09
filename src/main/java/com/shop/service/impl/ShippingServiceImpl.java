package com.shop.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.Message;
import com.shop.dao.ShippingMapper;
import com.shop.pojo.Shipping;
import com.shop.service.IShippingService;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

	@Autowired
	private ShippingMapper shippingMapper;
	
	@Override
	public Message addShipping(Shipping shipping, Integer userId) {
		if(StringUtils.isBlank(shipping.getName()) || StringUtils.isBlank(shipping.getProvince()) || StringUtils.isBlank(shipping.getCity())){
			return Message.errorMsg("必填项不能为空");
		}
		shipping.setUserId(userId);
		int count = shippingMapper.insert(shipping);
		if(count <= 0)
			return Message.errorMsg("新增收货信息失败");
		return Message.successMsg("新增收货信息成功");
	}

	@Override
	public Message list(Integer userId) {
		List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
		if(shippingList.isEmpty())
			return Message.errorMsg("该用户没有收货信息");
		return Message.successData(shippingList);
	}

}
