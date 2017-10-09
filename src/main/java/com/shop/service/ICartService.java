package com.shop.service;

import com.shop.common.Const;
import com.shop.common.Message;
import com.shop.pojo.User;

public interface ICartService {

	Message addCart(User user, Integer goodsId, Integer goodsQuantity);
	
	Message changeQuantity(Integer cartId, int quantity, User user);
	
	Message listCart(User user);
	
	/**
	 * 全选、单选和全不选、单不选 2组
	 * @param user
	 * @param cartId
	 * @param type	Const.Cart.SELECTED, Const.Cart.UNSELECT
	 * @return
	 */
	Message selectOrUnselect(User user, Integer cartId, int type);
	
	Message delete(Integer cartId, User user);
}
