package com.shop.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Cart {
    private Integer id;

    private Integer userId;

    private Integer goodsId;

    private BigDecimal price;

    private Integer quantity;

    private Integer selected;

    private Date createTime;

    private Date updateTime;

    public Cart(Integer id, Integer userId, Integer goodsId, BigDecimal price, Integer quantity, Integer selected, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.goodsId = goodsId;
        this.price = price;
        this.quantity = quantity;
        this.selected = selected;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Cart() {
        super();
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
}