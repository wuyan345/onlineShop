package com.shop.dao;

import java.util.List;

import com.shop.pojo.Order;
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
    
    /**
     * 根据orderList的订单号返回相应的子订单
     * @param orderList
     * @return
     */
    List<OrderItem> batchSelectByOrderList(List<Order> orderList);
}