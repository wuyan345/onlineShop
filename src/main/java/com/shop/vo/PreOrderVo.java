package com.shop.vo;

import java.util.List;

import com.shop.bo.order.CartGoodsBo;
import com.shop.pojo.Shipping;

public class PreOrderVo {

	private List<CartGoodsBo> cartGoodsBoList;
	
	private List<Shipping> shippingList;

	public List<CartGoodsBo> getCartGoodsBoList() {
		return cartGoodsBoList;
	}

	public void setCartGoodsBoList(List<CartGoodsBo> cartGoodsBoList) {
		this.cartGoodsBoList = cartGoodsBoList;
	}

	public List<Shipping> getShippingList() {
		return shippingList;
	}

	public void setShippingList(List<Shipping> shippingList) {
		this.shippingList = shippingList;
	}
	
}
