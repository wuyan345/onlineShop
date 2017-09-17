package com.shop.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.shop.pojo.Goods;

public class GoodsDetailVo {

	// Goods pojo
	private Goods goods;
	// extension
	private String imgHost;

	
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public String getImgHost() {
		return imgHost;
	}

	public void setImgHost(String imgHost) {
		this.imgHost = imgHost;
	}
	
	
}
