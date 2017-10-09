package com.shop.service;

import com.github.pagehelper.PageInfo;
import com.shop.common.Message;
import com.shop.pojo.Goods;

public interface IGoodsService {

	Message addGoods(Goods goods);
	
	Message<PageInfo> list(Integer categoryId, int pageNum, int pageSize);
	
	Message getGoodsDetail(int goodsId);
	
	Message<PageInfo> searchGoods(String keyword, int pageNum, int pageSize);
}
