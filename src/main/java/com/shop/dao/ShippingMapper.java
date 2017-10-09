package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shop.pojo.Order;
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
    
    /**
     * 检查该用户是否有指定的shippingId
     * @param shippingId
     * @param userId
     * @return
     */
    int checkShippingId(@Param("shippingId")Integer shippingId, @Param("userId")Integer userId);
    
    /**
     * 根据order里的shippingId返回相应的shipping
     * @param orderList
     * @return
     */
    List<Shipping> batchSelectByOrderList(List<Order> orderList);
}