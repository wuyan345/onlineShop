package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shop.bo.CartGoodsBo;
import com.shop.pojo.Cart;
import com.shop.pojo.Goods;
import com.shop.vo.PreOrderVo;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    
    /**
     * 返回用户所有购物车
     * @param userId
     * @return
     */
    List<Cart> selectByUserId(Integer userId);
    
    /**
     * 返回用户所有勾选或未勾选的购物车
     * @param userId
     * @param selected
     * @return
     */
    List<Cart> selectByUserIdSelected(@Param("userId")Integer userId, @Param("selected")int selected);
    
    /**
     * 返回指定用户所有购物车以及对应商品
     * @param userId
     * @return
     */
    List<CartGoodsBo> selectAllCartGoods(Integer userId);
    
    int batchUpdate(List<Cart> cartList);
    
    int batchUpdateTest(List<Cart> cartList);
    
    /**
     * 返回用户所有勾选的购物车对应的商品信息
     * @param userId
     * @return
     */
    List<Goods> selectGoodsByUserId(Integer userId);
    
    List<com.shop.bo.order.CartGoodsBo> querySelectedCartGoods(Integer userId);
    
    int batchDelete(List<com.shop.bo.order.CartGoodsBo> cartGoodsBoList);
    
}