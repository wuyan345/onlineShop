package com.shop.dao;

import java.util.List;

import com.shop.pojo.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
    
    int batchInsert(List<OrderItem> orderItemList);
    
    List<OrderItem> selectQuantityByOrderNo(Long orderNo);
    
    List<OrderItem> selectByOrderNo(Long orderNo);
}