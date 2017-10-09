package com.shop.vo;

import java.util.List;

import com.shop.pojo.Order;
import com.shop.pojo.OrderItem;
import com.shop.pojo.Shipping;

public class OrderVo {

	private Order order;
	
	private List<OrderItem> orderItemList;
	
	private Shipping shipping;
	
	private String createTime;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
