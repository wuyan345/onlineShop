package com.shop.vo;

import java.math.BigDecimal;
import java.util.List;

import com.shop.bo.CartGoodsBo;

public class CartGoodsVo {

	private List<CartGoodsBo> cartGoodsBoList;
	
	private BigDecimal totalPrice;

	public List<CartGoodsBo> getCartGoodsBoList() {
		return cartGoodsBoList;
	}

	public void setCartGoodsBoList(List<CartGoodsBo> cartGoodsBoList) {
		this.cartGoodsBoList = cartGoodsBoList;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
