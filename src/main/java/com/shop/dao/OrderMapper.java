package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shop.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    List<Order> selectByUserId(Integer userId);
    
    Order selectByOrderNo(Long orderNo);
    
    Order selectByOrderIdUserId(@Param("orderId")Integer orderId, @Param("userId")Integer userId);
}