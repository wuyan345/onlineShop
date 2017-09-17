package com.shop.bo;

import java.math.BigDecimal;
import java.util.Date;

public class CartGoodsBo {

	// cart
	private Integer id;

	private Integer userId;

	private Integer goodsId;

	private Integer quantity;

	private Integer selected;

	private Date createTime;

	private Date updateTime;

	// goods
	private String name;

	private BigDecimal price;

	private Integer stock;

	// extension
	// 单个商品总价
	private BigDecimal totalPrice;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return "CartBo [id=" + id + ", userId=" + userId + ", goodsId=" + goodsId + ", quantity=" + quantity
				+ ", selected=" + selected + ", createTime=" + createTime + ", updateTime=" + updateTime + ", name="
				+ name + ", price=" + price + ", stock=" + stock + "]";
	}

}
