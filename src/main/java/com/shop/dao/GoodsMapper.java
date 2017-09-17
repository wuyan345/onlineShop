package com.shop.dao;

import java.math.BigDecimal;
import java.util.List;

import com.shop.bo.order.CartGoodsBo;
import com.shop.pojo.Goods;
import com.shop.pojo.OrderItem;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
    
    List<Goods> selectByKeyword(String keyword);
    
    List<Goods> selectByCategoryId(int categoryId);
    
    BigDecimal selectPriceByGoodsId(int goodsId);
    
    int batchUpdateQuantity(List<CartGoodsBo> cartGoodsBoList);
    
    int batchUpdateQuantityForCancelOrder(List<OrderItem> orderItemList);
}