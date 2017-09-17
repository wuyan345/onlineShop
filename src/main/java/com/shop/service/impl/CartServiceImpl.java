package com.shop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.bo.CartGoodsBo;
import com.shop.common.Const;
import com.shop.common.Message;
import com.shop.dao.CartMapper;
import com.shop.dao.GoodsMapper;
import com.shop.pojo.Cart;
import com.shop.pojo.User;
import com.shop.service.ICartService;
import com.shop.utils.BigDecimalUtil;
import com.shop.vo.CartGoodsVo;

@Service("iCartService")
@Transactional
public class CartServiceImpl implements ICartService {

	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Override
	public Message addCart(User user, Integer goodsId) {
		Cart cart = new Cart();
		cart.setUserId(user.getId());
		cart.setGoodsId(goodsId);
		cart.setQuantity(1);
		cart.setSelected(Const.Cart.UNSELECT);
		int count = cartMapper.insert(cart);
		if(count <= 0)
			return Message.errorMsg("加入购物车失败");
		return Message.successMsg("已加入购物车");
	}

	@Override
	public Message changeQuantity(Integer cartId, int quantity, User user) {
		Cart cart = new Cart();
		cart.setId(cartId);
		cart.setQuantity(quantity);
		int count = cartMapper.updateByPrimaryKeySelective(cart);
		return list(user.getId());
	}

	@Override
	public Message listCart(User user) {
		return list(user.getId());
	}

	private Message list(int userId){
		// 数据库返回所有该用户已选中的购物车
		List<CartGoodsBo> CartGoodsBoList = cartMapper.selectAllCartGoods(userId);
		if(CartGoodsBoList.size() == 0)
			return Message.successMsg("该用户购物车为空");
		// 查看选中商品库存足够否？
		BigDecimal totalPrice = new BigDecimal("0");
		for (CartGoodsBo cartGoodsBo : CartGoodsBoList) {
			if(cartGoodsBo.getQuantity() > cartGoodsBo.getStock())
				return Message.errorMsgData("该商品库存数量不足", cartGoodsBo);
			// 计算单个商品总价
			cartGoodsBo.setTotalPrice(BigDecimalUtil.mul(cartGoodsBo.getPrice().doubleValue(), cartGoodsBo.getQuantity()));
			if(cartGoodsBo.getSelected() == Const.Cart.SELECTED){
				// 将已勾选的单个商品总价计入到整体总价里
				totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(), cartGoodsBo.getTotalPrice().doubleValue());
			}
		}
		CartGoodsVo cartGoodsVo = createCartGoodsVo(CartGoodsBoList, totalPrice);
		return Message.successData(cartGoodsVo);
	}
	
	private CartGoodsVo createCartGoodsVo(List<CartGoodsBo> CartGoodsBoList, BigDecimal totalPrice){
		CartGoodsVo cartGoodsVo = new CartGoodsVo();
		cartGoodsVo.setCartGoodsBoList(CartGoodsBoList);
		cartGoodsVo.setTotalPrice(totalPrice);
		return cartGoodsVo;
	}
	
	@Override
	public Message selectOrUnselect(User user, Integer cartId, int type) {
		if(cartId == null){
			// 前端要全选或全不选
			List<Cart> cartList = cartMapper.selectByUserId(user.getId());
			if(cartList.size() == 0)
				return Message.errorMsg("该用户购物车为空");
			List<Cart> cartListUpdate = new ArrayList<>();
			for (Cart cart : cartList) {
				Cart newCart = new Cart();
				newCart.setId(cart.getId());
				newCart.setSelected(type);
				cartListUpdate.add(newCart);
			}
			// 批量更新cart
			int count = cartMapper.batchUpdate(cartListUpdate);
			if(count <= 0)
				return Message.errorMsg("更新购物车失败");
		}else{
			// 前端要单选或单不选
			Cart cart = cartMapper.selectByPrimaryKey(cartId);
			if(cart == null)
				return Message.errorMsg("参数错误");
			Cart newCart = new Cart();
			newCart.setId(cart.getId());
			newCart.setSelected(type);
			int count = cartMapper.insertSelective(newCart);
			if(count <= 0)
				return Message.errorMsg("更新购物车失败");
		}
		return list(user.getId());
	}
	
}
