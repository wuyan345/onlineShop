package com.shop.bo.order;

import java.math.BigDecimal;
import java.util.Date;

public class CartGoodsBo {

	// cart的字段
	private Integer cartId;

    private Integer userId;

    private BigDecimal cartPrice;	// 废弃，由下面的goodsPrice代替

    private Integer quantity;

    private Integer selected;

    private Date cartCreateTime;

    private Date cartUpdateTime;
    
    // goods的字段
    private Integer goodsId;

    private String name;

    private Integer categoryId;

    private String subtitle;

    private String mainImage;

    private String detail;

    private BigDecimal goodsPrice;

    private Integer stock;

    private Date goodsCreateTime;

    private Date goodsUpdateTime;

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(BigDecimal cartPrice) {
		this.cartPrice = cartPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	public Date getCartCreateTime() {
		return cartCreateTime;
	}

	public void setCartCreateTime(Date cartCreateTime) {
		this.cartCreateTime = cartCreateTime;
	}

	public Date getCartUpdateTime() {
		return cartUpdateTime;
	}

	public void setCartUpdateTime(Date cartUpdateTime) {
		this.cartUpdateTime = cartUpdateTime;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Date getGoodsCreateTime() {
		return goodsCreateTime;
	}

	public void setGoodsCreateTime(Date goodsCreateTime) {
		this.goodsCreateTime = goodsCreateTime;
	}

	public Date getGoodsUpdateTime() {
		return goodsUpdateTime;
	}

	public void setGoodsUpdateTime(Date goodsUpdateTime) {
		this.goodsUpdateTime = goodsUpdateTime;
	}

	@Override
	public String toString() {
		return "CartGoodsBo [cartId=" + cartId + ", userId=" + userId + ", cartPrice=" + cartPrice + ", quantity="
				+ quantity + ", selected=" + selected + ", cartCreateTime=" + cartCreateTime + ", cartUpdateTime="
				+ cartUpdateTime + ", goodsId=" + goodsId + ", name=" + name + ", categoryId=" + categoryId
				+ ", subtitle=" + subtitle + ", mainImage=" + mainImage + ", detail=" + detail + ", goodsPrice="
				+ goodsPrice + ", stock=" + stock + ", goodsCreateTime=" + goodsCreateTime + ", goodsUpdateTime="
				+ goodsUpdateTime + "]";
	}
	
}
