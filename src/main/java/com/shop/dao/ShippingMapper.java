package com.shop.dao;

import java.util.List;

import com.shop.pojo.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    
    /**
     * 返回所有该用户的收货地址
     * @param userId
     * @return
     */
    List<Shipping> selectByUserId(Integer userId);
}